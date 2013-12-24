package framework.retrieval.engine.index.all.database;

public class DatabaseLink {
	/**
	 * 数据库驱动
	 */
	private String jdbcDriver="";
	/**
	 * 数据库地址
	 */
	private String jdbcUrl="";
	/**
	 * 数据库用户名
	 */
	private String jdbcUser="";
	/**
	 * 数据库密码
	 */
	private String jdbcPassword="";
	
	public String getJdbcDriver() {
		return jdbcDriver;
	}
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getJdbcUser() {
		return jdbcUser;
	}
	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}
	public String getJdbcPassword() {
		return jdbcPassword;
	}
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}
	
}
