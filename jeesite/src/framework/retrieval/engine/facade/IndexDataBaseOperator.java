package framework.retrieval.engine.facade;

import java.lang.reflect.Proxy;

import framework.retrieval.engine.context.RetrievalApplicationContext;
import framework.retrieval.engine.index.doc.database.RDatabaseIndexAllItem;
import framework.retrieval.test.dbindexall.DatabaseIndexAllItemImpl;

public class IndexDataBaseOperator extends AbstractIndexOperatorFacade{
	private ICreateDatabaseIndexAllItem createDatabaseIndexAllItem;
	public IndexDataBaseOperator (ICreateDatabaseIndexAllItem createDatabaseIndexAllItem){
		this.createDatabaseIndexAllItem = createDatabaseIndexAllItem;
	}
	@Override
	public RDatabaseIndexAllItem deal(RetrievalApplicationContext retrievalApplicationContext) {
		ICreateDatabaseIndexAllItem proxy = createDatabaseIndexAllItem;
		return proxy.generateApplicationData(retrievalApplicationContext);
	}

}
