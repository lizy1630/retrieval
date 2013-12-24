package framework.retrieval.test.dbindexall;

import framework.retrieval.engine.RetrievalType;
import framework.retrieval.engine.RetrievalType.RDatabaseType;
import framework.retrieval.engine.context.RetrievalApplicationContext;
import framework.retrieval.engine.facade.ICreateDatabaseIndexAllItem;
import framework.retrieval.engine.index.doc.database.RDatabaseIndexAllItem;

public class DatabaseIndexAllItemImpl implements ICreateDatabaseIndexAllItem{

	@Override
	public RDatabaseIndexAllItem generateApplicationData(RetrievalApplicationContext retrievalApplicationContext) {
		String tableName = "TEST_WEB";
		String keyField = "GUID";
		String sql = "SELECT GUID,TITLE,CONTENT,CREATETIME,TIME,PAGE_URL,PIC_URL FROM TEST_WEB ORDER BY GUID ASC";

		RDatabaseIndexAllItem databaseIndexAllItem = retrievalApplicationContext.getFacade().createDatabaseIndexAllItem(false);
		databaseIndexAllItem.setIndexPathType("BS/DB/TEST_WEB");
		databaseIndexAllItem.setIndexInfoType("TEST_WEB");
		databaseIndexAllItem.setDatabaseType(RDatabaseType.SQLSERVER);

		// 如果无论记录是否存在，都新增一条索引内容，则使用RetrievalType.RIndexOperatorType.INSERT，
		// 如果索引中记录已经存在，则只更新索引中的对应的记录，否则新增记录,则使用RetrievalType.RIndexOperatorType.UPDATE
		databaseIndexAllItem.setIndexOperatorType(RetrievalType.RIndexOperatorType.INSERT);

		databaseIndexAllItem.setTableName(tableName);
		databaseIndexAllItem.setKeyField(keyField);
		databaseIndexAllItem.setDefaultTitleFieldName("TITLE");
		databaseIndexAllItem.setDefaultResumeFieldName("CONTENT");
		databaseIndexAllItem.setPageSize(500);
		databaseIndexAllItem.setSql(sql);
		databaseIndexAllItem.setParam(new Object[] {});

		databaseIndexAllItem.setDatabaseRecordInterceptor(new TestDatabaseRecordInterceptor());
		
		return databaseIndexAllItem;
	}

}
