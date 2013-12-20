package framework.retrieval.engine.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;

import framework.base.snoic.base.util.PathUtil;
import framework.base.snoic.base.util.StringClass;
import framework.base.snoic.base.util.properties.ReadProperties;
import framework.retrieval.engine.common.RetrievalUtil;
import framework.retrieval.engine.index.all.database.impl.rdAbstract.DefaultRDatabaseIndexAllImpl;

public class DefaultRetrievalProperties {
	private static Log log=RetrievalUtil.getLog(DefaultRetrievalProperties.class);
	private static final String RETRIEVAL_PROPERTY_FILE_NAME="retrieval-default.properties";
	
	/**
	 * 单线程默认运行数
	 */
	private static final int DEFAULT_RETRIEVAL_SINGLE_THREAD_DEALNUMS=200;
	private static final String PROPERTIES_RETRIEVAL_SINGLE_THREAD_DEALNUMS="RETRIEVAL_SINGLE_THREAD_DEALNUMS";
	
	/**
	 * 最大并发量
	 */
	private static final int DEFAULT_RETRIEVAL_CONCURRENT_THREAD_MAXNUM=50;
	private static final String PROPERTIES_RETRIEVAL_CONCURRENT_THREAD_MAXNUM="RETRIEVAL_CONCURRENT_THREAD_MAXNUM";
	
	private static final String DEFALUT_RETRIEVAL_WORKSPACE = PathUtil.getDefaultIndexPath();
	private static final String PROPERTIES_RETRIEVAL_WORKSPACE="PROPERTIES_RETRIEVAL_WORKSPACE";
	
	/**
	 * 生成索引路径
	 */
	private static  int default_single_thread_dealnums=DEFAULT_RETRIEVAL_SINGLE_THREAD_DEALNUMS;
	private static  int default_thread_maxnum=DEFAULT_RETRIEVAL_CONCURRENT_THREAD_MAXNUM; 
	private static String default_retrieval_workspace=DEFALUT_RETRIEVAL_WORKSPACE;
	
	/**
	 * 开机启动redis
	 */
	public static final String NO = "no";
	public static final String YES = "yes";
	private static final String DEFAULT_RETRIEVAL_REDIS_START = NO;
	private static final String PROPERTIES_RETRIEVAL_REDIS_START = "PROPERTIES_RETRIEVAL_REDIS_START";
	
	private static final String DEFAULT_RETRIEVAL_REDIS_SERVER_PATH = PathUtil.getDefaultRedisPath();
	private static final String PROPERTIES_RETRIEVAL_REDIS_SERVER_PATH = "PROPERTIES_RETRIEVAL_REDIS_SERVER_PATH";
	
	private static String default_retrieval_redis_start = DEFAULT_RETRIEVAL_REDIS_START;
	private static String default_retrieval_redis_server_path = DEFAULT_RETRIEVAL_REDIS_SERVER_PATH;
	
	
	public DefaultRetrievalProperties(){
		
		ReadProperties readProperties=new ReadProperties();
		
		InputStream inputStream=DefaultRDatabaseIndexAllImpl.class.getResourceAsStream("/"+RETRIEVAL_PROPERTY_FILE_NAME);
		
		if(inputStream!=null){
			
			RetrievalUtil.debugLog(log, "发现DefaultRDatabaseIndexAllImpl属性配置文件:"+RETRIEVAL_PROPERTY_FILE_NAME+"，载入数据库配置");

			Properties properties=new Properties();
			
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				throw new RetrievalLoadException(e);
			}
			
			readProperties.setProperties(properties);
			readProperties.parse();
			String taskparam = null;
			taskparam = StringClass.getString(readProperties.readValue(PROPERTIES_RETRIEVAL_SINGLE_THREAD_DEALNUMS));
			if(!taskparam.equals("")){
				default_single_thread_dealnums = Integer.parseInt(taskparam);
			}
			taskparam = StringClass.getString(readProperties.readValue(PROPERTIES_RETRIEVAL_CONCURRENT_THREAD_MAXNUM));
			if(!taskparam.equals("")){
				default_thread_maxnum = Integer.parseInt(taskparam);
			}
			taskparam = StringClass.getString(readProperties.readValue(PROPERTIES_RETRIEVAL_WORKSPACE));
			if(!taskparam.equals("")){
				default_retrieval_workspace = taskparam;
			}
			taskparam = StringClass.getString(readProperties.readValue(PROPERTIES_RETRIEVAL_REDIS_START));
			if(!taskparam.equals("")){
				default_retrieval_redis_start = taskparam;
			}
			taskparam = StringClass.getString(readProperties.readValue(PROPERTIES_RETRIEVAL_REDIS_SERVER_PATH));
			if(!taskparam.equals("")){
				default_retrieval_redis_server_path = taskparam;
			}
			readProperties.close();
		}
	}

	public static int getDefault_single_thread_dealnums() {
		return default_single_thread_dealnums;
	}

	public static void setDefault_single_thread_dealnums(
			int default_single_thread_dealnums) {
		DefaultRetrievalProperties.default_single_thread_dealnums = default_single_thread_dealnums;
	}

	public static int getDefault_thread_maxnum() {
		return default_thread_maxnum;
	}

	public static void setDefault_thread_maxnum(int default_thread_maxnum) {
		DefaultRetrievalProperties.default_thread_maxnum = default_thread_maxnum;
	}

	public static String getDefault_retrieval_workspace() {
		return default_retrieval_workspace;
	}

	public static void setDefault_retrieval_workspace(
			String default_retrieval_workspace) {
		DefaultRetrievalProperties.default_retrieval_workspace = default_retrieval_workspace;
	}

	public static String getDefault_retrieval_redis_start() {
		return default_retrieval_redis_start;
	}

	public static void setDefault_retrieval_redis_start(
			String default_retrieval_redis_start) {
		DefaultRetrievalProperties.default_retrieval_redis_start = default_retrieval_redis_start;
	}

	public static String getDefault_retrieval_redis_server_path() {
		return default_retrieval_redis_server_path;
	}

	public static void setDefault_retrieval_redis_server_path(
			String default_retrieval_redis_server_path) {
		DefaultRetrievalProperties.default_retrieval_redis_server_path = default_retrieval_redis_server_path;
	}
	

}