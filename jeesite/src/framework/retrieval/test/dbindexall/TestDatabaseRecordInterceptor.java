package framework.retrieval.test.dbindexall;

import java.util.Map;

import framework.base.snoic.base.util.RegexUtil;
import framework.retrieval.engine.index.all.database.IIndexAllDatabaseRecordInterceptor;

public class TestDatabaseRecordInterceptor implements IIndexAllDatabaseRecordInterceptor{

	/**
	 * 将数据库中查询中的记录进行加工处理
	 * @param record
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map interceptor(Map record) {
		
		record.put("DOC_CREATE_TIME", String.valueOf(System.currentTimeMillis()));
		record.put("content",RegexUtil.Html2Text((String)record.get("content")));//去掉html标签
		return record;
	}

	@SuppressWarnings("unchecked")
	public Map getFieldsType() {
		return null;
	}

}
