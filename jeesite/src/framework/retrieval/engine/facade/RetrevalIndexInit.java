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
package framework.retrieval.engine.facade;

import org.apache.commons.logging.Log;

import framework.retrieval.engine.analyzer.IRAnalyzerFactory;
import framework.retrieval.engine.common.RetrievalUtil;
import framework.retrieval.engine.context.LuceneProperties;
import framework.retrieval.engine.facade.impl.RIndexOperatorFacade;



/**
 * 初始化索引
 * @author 
 *
 */
public class RetrevalIndexInit {
	private Log log=RetrievalUtil.getLog(this.getClass());

	private static Object object=new Object();
	private volatile static boolean loadFlag=false;
	
	public RetrevalIndexInit(){
		
	}

	/**
	 * 初始化
	 */
	public void init(IRAnalyzerFactory analyzerFactory,
			LuceneProperties luceneProperties,
			String[] indexPathTypes) {
		synchronized(object){
			if(!loadFlag){
				loadFlag=true;
				if(indexPathTypes!=null){
					int length=indexPathTypes.length;
					for(int i=0;i<length;i++){
						String indexPathType=indexPathTypes[i].toUpperCase();
						try{
							IRIndexOperatorFacade indexOperatorFacade=new RIndexOperatorFacade(analyzerFactory,luceneProperties,indexPathType);
							indexOperatorFacade.createIndex();
						}catch(Exception e){
							RetrievalUtil.errorLog(log, e);
						}
					}
				}
			}
		}
	}
	
}
