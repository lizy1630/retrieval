/**
 * Copyright 2010 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package framework.retrieval.engine.index.all.database.impl.rdAbstract;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.logging.Log;

import framework.base.snoic.base.util.properties.ReadProperties;
import framework.retrieval.engine.common.RetrievalUtil;
import framework.retrieval.engine.context.RetrievalLoadException;
import framework.retrieval.engine.index.RetrievalIndexException;

/**
 * 内置对数据库中的记录批量创建索引接口默认实现
 * 
 * @author 
 *
 */
public class DefaultRDatabaseIndexAllImpl extends
		AbstractDefaultRDatabaseIndexAll {

	private static Log log=RetrievalUtil.getLog(DefaultRDatabaseIndexAllImpl.class);
	
	private static final String JDBC_PROPERTY_FILE_NAME="retrieval-default-jdbc.properties";
	
	private static final String PARAM_NAME_JDBC_DRIVER="database.indexall.jdbc.driver";
	private static final String PARAM_NAME_JDBC_URL="database.indexall.jdbc.url";
	private static final String PARAM_NAME_JDBC_USER="database.indexall.jdbc.user";
	private static final String PARAM_NAME_JDBC_PASSWORD="database.indexall.jdbc.password";
	
	private static String jdbcDriver="";
	private static String jdbcUrl="";
	private static String jdbcUser="";
	private static String jdbcPassword="";
	
	static{
		
		ReadProperties readProperties=new ReadProperties();
		
		InputStream inputStream=DefaultRDatabaseIndexAllImpl.class.getResourceAsStream("/"+JDBC_PROPERTY_FILE_NAME);
		
		if(inputStream!=null){
			
			RetrievalUtil.debugLog(log, "发现DefaultRDatabaseIndexAllImpl属性配置文件:"+JDBC_PROPERTY_FILE_NAME+"，载入数据库配置");

			Properties properties=new Properties();
			
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				throw new RetrievalLoadException(e);
			}
			
			readProperties.setProperties(properties);
			readProperties.parse();
			
			jdbcDriver=readProperties.readValue(PARAM_NAME_JDBC_DRIVER);
			jdbcUrl=readProperties.readValue(PARAM_NAME_JDBC_URL);
			jdbcUser=readProperties.readValue(PARAM_NAME_JDBC_USER);
			jdbcPassword=readProperties.readValue(PARAM_NAME_JDBC_PASSWORD);
			
			readProperties.close();
			
		}
		
	}
	
	
	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	private Connection getConnection() {
		Connection conn = null;
		try {
			DbUtils.loadDriver(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
		} catch (SQLException e) {
			throw new RetrievalIndexException(e);
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	private void close(Connection conn) {
		DbUtils.closeQuietly(conn);
	}

	/**
	 * 根据分页SQL获取数据
	 * @param limitSql			分页SQL
	 * @param params			SQL参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getResult(String limitSql,Object[] params) {
		Connection conn = getConnection();

		QueryRunner qRunner = new QueryRunner();
		ResultSetHandler rsh = new MapListHandler();
		List<Map> result = null;

		try {
			if (params != null && params.length > 0) {
				result = (List<Map>) qRunner.query(conn, limitSql, rsh, params);
			} else {
				result = (List<Map>) qRunner.query(conn, limitSql, rsh);
			}
		} catch (SQLException e) {
			throw new RetrievalIndexException(e);
		} finally {
			close(conn);
		}

		return result;
	}
}
