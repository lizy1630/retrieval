package framework.retrieval.engine.facade;

import java.util.List;

import org.apache.lucene.index.IndexWriter;

import framework.retrieval.engine.context.ApplicationContext;
import framework.retrieval.engine.context.RetrievalApplicationContext;
import framework.retrieval.engine.index.doc.database.RDatabaseIndexAllItem;
import framework.retrieval.test.init.TestInit;

/**
 * 索引操作接口
 * 
 * @author sxjun
 *
 */
public abstract class AbstractIndexOperatorFacade {
	private RetrievalApplicationContext retrievalApplicationContext=ApplicationContext.getApplicationContent();
	public long indexAll(){
		long startTime = System.currentTimeMillis();
		IndexWriter indexWriter = null;
		long indexCount = 0;
		try {
			RDatabaseIndexAllItem databaseIndexAllItem = deal(retrievalApplicationContext);
			ApplicationContext.initIndex(databaseIndexAllItem.getIndexPathType());
			indexWriter = retrievalApplicationContext.getFacade().createIndexWriter(databaseIndexAllItem.getIndexPathType());
			IRDocOperatorFacade docOperatorFacade = retrievalApplicationContext.getFacade().createDocOperatorFacade();
			indexCount = docOperatorFacade.createAll(databaseIndexAllItem,indexWriter);
			retrievalApplicationContext.getFacade().createIndexOperatorFacade(databaseIndexAllItem.getIndexPathType()).optimize();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			retrievalApplicationContext.getFacade().closeIndexWriter(indexWriter);
		}
		System.out.println("TABLE1 耗时："+ (((System.currentTimeMillis() - startTime) / 1000))+ " 秒,共完成：" + indexCount + " 条索引");
		return indexCount;
	}
	
	public abstract RDatabaseIndexAllItem deal(RetrievalApplicationContext retrievalApplicationContext);
}
