package com.thinkgem.jeesite.modules.retrieval.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * <b>function:</b>RestTemplate调用REST资源
 * @author hoojo
 * @createDate 2011-6-9 上午11:56:16
 * @file RESTClient.java
 * @package com.hoo.client
 * @project SpringRestWS
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
@Component
public class RESTClient {
    
    @Autowired
    private RestTemplate template;
    
    private final static String url = "http://localhost:8080/jeesite/a/restful/";
    
    public String show() {
        return template.getForObject(url + "show/", String.class, new String[]{});
    }
    
    public String getUserById(String id) {
        return template.getForObject(url + "get/{id}/", String.class, id); 
    }
    
    public String addUser(String user) {
        return template.postForObject(url + "add/?user={user}", null, String.class, user);
    }
    
    public String editUser(String user) {
        template.put(url + "edit/?user={user}", null, user);
        return user;
    }
    
    public String removeUser(String id) {
        template.delete(url + "/remove/{id}.do", id);
        return id;
    }
}