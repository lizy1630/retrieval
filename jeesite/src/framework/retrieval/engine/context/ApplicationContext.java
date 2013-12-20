package framework.retrieval.engine.context;


public class ApplicationContext {
	private static RetrievalApplicationContext retrievalApplicationContext=null;
	
	public static RetrievalApplicationContext getApplicationContent(){
		if(retrievalApplicationContext==null)
			return new RetrievalApplicationContext();
		else
			return retrievalApplicationContext;
	}
	
	public static void initIndex(String[] indexPathTypes){
		getApplicationContent().getFacade().initIndex(indexPathTypes);
	}
	
	public static void initIndex(String indexPathType){
		initIndex(new String[]{indexPathType});
	}
	
	public static void main(String[] args) {
		ApplicationContext.initIndex("DB");
	}
	
}
