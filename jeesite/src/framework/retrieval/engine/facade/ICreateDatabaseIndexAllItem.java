package framework.retrieval.engine.facade;

import framework.retrieval.engine.context.RetrievalApplicationContext;
import framework.retrieval.engine.index.doc.database.RDatabaseIndexAllItem;

public interface ICreateDatabaseIndexAllItem{
	public RDatabaseIndexAllItem generateApplicationData(RetrievalApplicationContext retrievalApplicationContext);
}
