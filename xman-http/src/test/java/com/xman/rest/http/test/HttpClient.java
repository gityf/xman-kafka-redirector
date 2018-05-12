package com.xman.rest.http.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhouwei
 * 
 *
 */
public class HttpClient {
	
	 private static Logger log = LoggerFactory.getLogger(HttpClient.class);  
     
	    public static HttpResult post(String url, Map<String, String> params) 
	    {  
	        DefaultHttpClient httpclient = new DefaultHttpClient();  
	        String body = null;  
	          
	        log.info("HttpClient create httppost:" + url);  
	        HttpPost post = postForm(url, params);  
	          
	        HttpResult result = invoke(httpclient, post);  
	          
	        httpclient.getConnectionManager().shutdown();  
	          
	        return result;  
	    }  
	      
	    public static HttpResult get(String url) {  
	        DefaultHttpClient httpclient = new DefaultHttpClient();  
	        String body = null;  
	          
	        log.info("create httppost:" + url);  
	        HttpGet get = new HttpGet(url);  
	        HttpResult result = invoke(httpclient, get);  
	          
	        httpclient.getConnectionManager().shutdown();  
	          
	        return result;  
	    }  
	          
	      
	    private static HttpResult invoke(DefaultHttpClient httpclient,  
	            HttpUriRequest httpost) {  
	          
	        HttpResponse response = sendRequest(httpclient, httpost);  
	        HttpResult result = paseResponse(response);  
	          
	        return result;  
	    }  
	  
	    private static HttpResult paseResponse(HttpResponse response) { 
	    	
	    	HttpResult result = new HttpResult();
	    	if(response==null || response.getStatusLine()==null || response.getEntity()==null)
	    	{
	    		result.setStatusCode(500);
	    		result.setBody("response return null.");
	    		return result;
	    	}
	    	result.setStatusCode(response.getStatusLine().getStatusCode());
	        log.info("get response from http server.."+" response code:"+result.getStatusCode());  
	        HttpEntity entity = response.getEntity();  
	          
	        log.info("response status: " + response.getStatusLine());  
	        String charset = EntityUtils.getContentCharSet(entity);  
	        log.info(charset);  
	          
	        String body = null;  
	        try {  
	            body = EntityUtils.toString(entity); 
	            result.setBody(body);
	            log.info("paseResponse body:"+body);  
	        } catch (ParseException e) {  
	        	log.error(" paseResponse failed due to ParseException,",e);
	        } catch (IOException e) {  
	        	log.error(" paseResponse failed due to IOException,",e);
	        }  
	        return result;  
	    }  
	  
	    private static HttpResponse sendRequest(DefaultHttpClient httpclient,  
	            HttpUriRequest httpost) {  
	        log.info("execute post...");  
	        HttpResponse response = null;  
	          
	        try {  
	            response = httpclient.execute(httpost);  
	        } catch (ClientProtocolException e) {  
	           log.error(" sendRequest failed due to ClientProtocolException,",e);
	        } catch (IOException e) {  
	           log.error(" sendRequest failed due to IOException,",e);
	        }  
	        return response;  
	    }  
	  
	    private static HttpPost postForm(String url, Map<String, String> params){  
	          
	        HttpPost httpost = new HttpPost(url);  
	        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
	          
	        Set<String> keySet = params.keySet();  
	        for(String key : keySet) {  
	            nvps.add(new BasicNameValuePair(key, params.get(key)));  
	        }  
	          
	        try {  
	            log.info("set utf-8 form entity to httppost");  
	            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
	        } catch (UnsupportedEncodingException e) {  
	           log.error(" postForm failed,",e);
	        }  
	          
	        return httpost;  
	    }
	    
	     public static class HttpResult
	    {
	    	private Integer statusCode;
	    	
	    	private String body;

			public Integer getStatusCode() {
				return statusCode;
			}

			public void setStatusCode(Integer statusCode) {
				this.statusCode = statusCode;
			}

			public String getBody() {
				return body;
			}

			public void setBody(String body) {
				this.body = body;
			}
	    	
	    }

}
