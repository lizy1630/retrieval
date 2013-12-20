/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.retrieval.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.BooleanClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.retrieval.entity.SimpleItem;
import com.thinkgem.jeesite.modules.retrieval.entity.SimpleItem.QueryType;
import com.thinkgem.jeesite.modules.retrieval.entity.SimpleQuery;

import framework.retrieval.engine.RetrievalType;
import framework.retrieval.engine.context.ApplicationContext;
import framework.retrieval.engine.context.RetrievalApplicationContext;
import framework.retrieval.engine.query.item.QueryItem;
import framework.retrieval.helper.RetrievalPage;
import framework.retrieval.helper.RetrievalPageQuery;
import framework.retrieval.helper.RetrievalPageQueryHelper;
import framework.retrieval.helper.RetrievalPages;
import framework.retrieval.oth.ik.IKWordsUtil;
import framework.retrieval.oth.mapper.MapperUtil;

/**
 * 文章Controller
 * @author sxjun
 * @version 2013-12-2
 */
@Controller
@RequestMapping(value = "${frontPath}/retrieval/search")
public class SearchController extends BaseController {
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	private RetrievalApplicationContext retrievalApplicationContext = ApplicationContext.getApplicationContent();
	
	public QueryItem createQueryItem(RetrievalType.RDocItemType docItemType,Object name,String value,Float score){
		QueryItem queryItem=retrievalApplicationContext.getFacade().createQueryItem(docItemType, String.valueOf(name), value, score);
		return queryItem;
	}
	
	public List<RetrievalPage> getRetrievalPage(RetrievalPageQuery retrievalPageQuery,QueryItem queryItem){
		RetrievalPageQueryHelper retrievalPageQueryHelper=new RetrievalPageQueryHelper(retrievalApplicationContext,new String[]{"BS/DB/TEST_WEB"},queryItem);
		return retrievalPageQueryHelper.getResults(retrievalPageQuery);
		
	}
	
	public int getRetrievalCount(RetrievalPageQuery retrievalPageQuery,QueryItem queryItem){
		RetrievalPageQueryHelper retrievalPageQueryHelper=new RetrievalPageQueryHelper(retrievalApplicationContext,new String[]{"BS/DB/TEST_WEB"},queryItem);
		return retrievalPageQueryHelper.getResultCount(retrievalPageQuery);
	}
	
	/*@RequestMapping(value = {"list", ""})
	public String list(Model model, HttpServletRequest request, HttpServletResponse response,SimpleQuery simpleQuery) {
		if(simpleQuery.getKeyword()!=null){
			long startTime = System.currentTimeMillis();
			Page<RetrievalPage> page = search(simpleQuery,new Page<RetrievalPage>(request, response));
			long endTime = System.currentTimeMillis();
			System.out.println("用时："+String.format("%.2f",(double)(endTime-startTime)/1000)+"秒");
			model.addAttribute("page", page);
			model.addAttribute("simpleQuery", simpleQuery);
		}
		return "retrieval/search";
	}
	
	public Page<RetrievalPage> search(SimpleQuery simpleQuery, Page<RetrievalPage> page){
		simpleQuery.setPageSize(page.getPageSize());
		simpleQuery.setNowStartPage(page.getPageNo()-1);
		List<RetrievalPage> retrievalPageList = search(simpleQuery);
		page.setCount(searchCount(simpleQuery));
		page.setList(retrievalPageList);
		return page;
	}*/
	@RequestMapping(value = {""})
	public String list(){
		return "retrieval/search";
	}
	@RequestMapping(value = "auto")
	@ResponseBody 
	public List<String> autoComplete(@RequestBody String keyword){
		List<String> l = new ArrayList<String>();
		l.add(keyword);
		l.add("我们");
		return l;
	} 
	/**
	 * 返回json或xml
	 * @param request
	 * @param response
	 * @param simpleQuery
	 * @param type
	 */
	@RequestMapping(value = "/list/{type}",method = RequestMethod.POST)
	@ResponseBody 
	public void search(HttpServletRequest request, HttpServletResponse response, @RequestBody SimpleQuery simpleQuery,@PathVariable String type) {
		RetrievalPages pages = null;
		long startTime = System.currentTimeMillis();
		pages = search(simpleQuery);
		long endTime = System.currentTimeMillis();
		String time = String.format("%.2f",(double)(endTime-startTime)/1000);
		pages.setTime(time);
		if("json".equals(type)){
			try {
				String json = new MapperUtil().toJson(pages);
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.print(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if("xml".equals(type)){
			try {
				Map<String,Class> map = new HashMap<String,Class>();
				map.put("RetrievalPages", RetrievalPages.class);
				map.put("RetrievalPage", RetrievalPage.class);
				String xml = new MapperUtil(map).toXml(pages);
				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.print(xml);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
		}
	}
	
	
	/**
	 * 搜索
	 * @param simpleQuery
	 * @return
	 */
	public RetrievalPages search(SimpleQuery simpleQuery){
		RetrievalPageQuery retrievalPageQuery = generateRetrievalPageQuery(simpleQuery);
		QueryItem queryItem = composeQuerys(simpleQuery);
		RetrievalPages retrievalPages = new RetrievalPages();
		if(queryItem!=null){
			List<RetrievalPage> retrievalPageList = searchBody(retrievalPageQuery, queryItem);
			int count = getRetrievalCount(retrievalPageQuery, queryItem);
			retrievalPages.setRetrievalPageList(retrievalPageList);
			retrievalPages.setCount(count);
		}
		return retrievalPages;
	}
	
	/**
	 * 搜索主体
	 * @param retrievalPageQuery
	 * @param queryItem
	 * @return
	 */
	public List<RetrievalPage> searchBody(RetrievalPageQuery retrievalPageQuery,QueryItem queryItem){
		List<RetrievalPage> retrievalPageList = getRetrievalPage(retrievalPageQuery, queryItem);
		return retrievalPageList;
	}
	
	/**
	 * 搜索总数目
	 * @param simpleQuery
	 * @return
	 */
	public int searchCount(RetrievalPageQuery retrievalPageQuery,QueryItem queryItem){
		int count = getRetrievalCount(retrievalPageQuery, queryItem);
		return count;
	}
	
	/**
	 * 得到分页查询语句
	 * @param simpleQuery
	 * @return
	 */
	public RetrievalPageQuery generateRetrievalPageQuery(SimpleQuery simpleQuery){
		RetrievalPageQuery retrievalPageQuery = new RetrievalPageQuery();
		if(!StringUtils.isBlank(simpleQuery.getTitleField())){
			retrievalPageQuery.setTitleFieldName(simpleQuery.getTitleField());
			retrievalPageQuery.setTitleLength(simpleQuery.getTitleLength());
		}
		if(!StringUtils.isBlank(simpleQuery.getResumeField())){
			retrievalPageQuery.setResumeFieldName(simpleQuery.getResumeField());
			retrievalPageQuery.setResumeLength(simpleQuery.getResumeLength());
		}
		retrievalPageQuery.setPageSize(simpleQuery.getPageSize());
		retrievalPageQuery.setNowStartPage(simpleQuery.getNowStartPage()-1);
		if(simpleQuery.getQueryFields()!=null){
			int size = simpleQuery.getQueryFields().size();
			String[] queryFields = (String[]) simpleQuery.getQueryFields().toArray(new String[size]);
			retrievalPageQuery.setQueryFields(queryFields);
		}
		return retrievalPageQuery;
	}
	
	/**
	 * 查询语句
	 * @param simpleQuery
	 * @return
	 */
	public QueryItem composeQuerys(SimpleQuery simpleQuery){
		List<SimpleItem> simpleItems = simpleQuery.getSimpleItems();
		QueryItem queryitem = null;
		BooleanClause.Occur upRelationType = null;
		if(simpleItems!=null){
			int itemCount = simpleItems.size();
			if(itemCount>0){
				for(SimpleItem item : simpleItems){
					String kw = null;
					if(!StringUtils.isBlank(item.getKeyword()))
						kw = item.getKeyword();
					else
						kw = simpleQuery.getKeyword();
					if(!StringUtils.isBlank(kw)){
						QueryItem q = createQueryItem(item.getFieldType(),item.getField(),kw,null);
						if(queryitem!=null){
							if(QueryType.OR.equals(item.getRelationType()))
								queryitem.should(upRelationType,q);
							if(QueryType.AND.equals(item.getRelationType()))
								queryitem.must(upRelationType,q);
							if(QueryType.NOT.equals(item.getRelationType()))
								queryitem.mustNot(upRelationType,q);
							else
								queryitem.should(upRelationType,q);
						}else{
							queryitem = q;
						}
						upRelationType = item.getClauseRelationType();
					}
				}
			}
		}
		return queryitem;
	}
	
	/*public QueryItem composeItems(QueryItem queryitem,BooleanClause.Occur upRelationType,SimpleItem simpleItem,String defaultKeyword){
		if(!StringUtils.isBlank(simpleItem.getKeyword()))
			defaultKeyword = simpleItem.getKeyword();
		QueryItem q = createQueryItem(simpleItem.getFieldType(),simpleItem.getField(),defaultKeyword,null);
		if(QueryType.OR.equals(simpleItem.getRelationType()))
			queryitem.should(upRelationType,q);
		if(QueryType.AND.equals(simpleItem.getRelationType()))
			queryitem.must(upRelationType,q);
		if(QueryType.NOT.equals(simpleItem.getRelationType()))
			queryitem.mustNot(upRelationType,q);
		else
			queryitem.should(upRelationType,q);
		if(simpleItem.getSimpleItem()!=null){
			return composeItems(queryitem, upRelationType, simpleItem, defaultKeyword);
		}
		return queryitem;
	}
	*/
	public static void main(String[] args) throws IOException {  
		long startTime = System.currentTimeMillis();
		/*List<String> extList = DefaultConfig.getInstance().getExtDictionarys();
		File file = new File("D:\\Workspace\\jeesite\\jeesite\\WebContent\\WEB-INF\\classes\\a.dir");
		System.out.println(file.exists());
		Dictionary dictionary = Dictionary.initial(DefaultConfig.getInstance());
		ArrayList<String> words = new ArrayList<String>();
		//words.add("习近平是我们的主席");
		words.add("我是小军");
		words.add("asdfasd");
		words.add("人民");
		dictionary.addWords(words);
		//new SnoicsFileHelper().appendStringToFile(words, "D:\\Workspace\\jeesite\\jeesite\\WebContent\\WEB-INF\\classes\\a.dir");
        //String text="江苏省江阴市华西村";  
        //String text="高辉"; */
		String text="习近平是我们的主席,他的妻子是彭丽媛";
		for(String s :IKWordsUtil.ikTokens(text))
			System.out.print(s+"|");
        System.out.println();
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("%.2f",(double)(endTime-startTime)/1000));  
    }  

}
