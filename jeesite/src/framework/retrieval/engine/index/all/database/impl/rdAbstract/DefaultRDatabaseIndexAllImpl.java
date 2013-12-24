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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.logging.Log;

import framework.base.snoic.base.util.ReflectUtil;
import framework.retrieval.engine.common.RetrievalUtil;
import framework.retrieval.engine.context.DefaultRetrievalProperties;
import framework.retrieval.engine.index.RetrievalIndexException;
import framework.retrieval.engine.index.all.database.DatabaseLink;

/**
 * 内置对数据库中的记录批量创建索引接口默认实现
 * 
 * @author 
 *
 */
public class DefaultRDatabaseIndexAllImpl extends
		AbstractDefaultRDatabaseIndexAll {

	private static Log log=RetrievalUtil.getLog(DefaultRDatabaseIndexAllImpl.class);
	
	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	private Connection getConnection() {
		Connection conn = null;
		try {
			String default_retrieval_database_choose_class = DefaultRetrievalProperties.getDefault_retrieval_database_choose_class();
			String[] classAndMethod = default_retrieval_database_choose_class.split(":");
			String className = classAndMethod[0];
			String methodName = null;
			if (classAndMethod.length == 1) {
				methodName = "loadDatabaseLink";
			} else {
				methodName = classAndMethod[1];
			}
			// 反射调用
			DatabaseLink databaseLink = (DatabaseLink) ReflectUtil.invokeMethod(className, methodName);
			DbUtils.loadDriver(databaseLink.getJdbcDriver());
			conn = DriverManager.getConnection(databaseLink.getJdbcUrl(), databaseLink.getJdbcUser(), databaseLink.getJdbcPassword());
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
