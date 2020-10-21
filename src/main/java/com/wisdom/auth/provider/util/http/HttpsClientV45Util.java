package com.wisdom.auth.provider.util.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpsClientV45Util { 
  /**
     * Post请求，外部setBody解析Body
     * @param httpClient
     * @param url
     * @param paramHeader
     * @param paramBody
     * @return
     * @throws Exception
     */ 
  public static String doPost(HttpClient httpClient, String url, Map<String, String> paramHeader,
                              Map<String, String> paramBody) throws Exception {
    String result = null; 
    HttpPost httpPost = new HttpPost(url);
    setHeader(httpPost, paramHeader); 
    setBody(httpPost, paramBody, "UTF-8");  
    HttpResponse response = httpClient.execute(httpPost); 
    if (response != null) { 
      HttpEntity resEntity = response.getEntity(); 
      if (resEntity != null) { 
        result = EntityUtils.toString(resEntity, "UTF-8"); 
      } 
    } 
    return result; 
  } 
  /**
     * Post请求，内置解析Body
     * @param httpClient
     * @param url
     * @param paramHeader
     * @param paramBody
     * @return
     * @throws Exception
     */
  public static String doPostString(HttpClient httpClient, String url, Map<String, String> paramHeader,
                                    String paramBody) throws Exception {
    String result = null; 
    HttpPost httpPost = new HttpPost(url);
    setHeader(httpPost, paramHeader); 
    StringEntity entity = new StringEntity(paramBody, ContentType.APPLICATION_JSON);
    httpPost.setEntity(entity); 
    HttpResponse response = httpClient.execute(httpPost); 
    if (response != null) { 
      HttpEntity resEntity = response.getEntity(); 
      if (resEntity != null) { 
        result = EntityUtils.toString(resEntity, "UTF-8"); 
      } 
    } 
    return result; 
  } 
  
  
  public static String doPostGetHeader(HttpClient httpClient, String url, Map<String, String> paramHeader,
                                       String paramBody) throws Exception {
    String result = null; 
    HttpPost httpPost = new HttpPost(url);
    setHeader(httpPost, paramHeader); 
    StringEntity entity = new StringEntity(paramBody, ContentType.APPLICATION_JSON);
    System.out.format("entity:"+entity+"\n");
    httpPost.setEntity(entity);
    HttpResponse response = httpClient.execute(httpPost);
    // System.out.format("all headers"+response.getAllHeaders()+"\n"); 
    // System.out.format("all headers"+response.getAllHeaders()+"\n");
    Header[] headersAll = response.getAllHeaders();
    // for(Header header:headersAll){
    //   System.out.format("header all value:"+header.getValue()+"\n");
    // }
    Header[] headersPart = response.getHeaders("X-Subject-Token");
    String resultHeader = headersPart[0].getValue();
    return resultHeader;
    // for(Header header:headersPart){
    //   System.out.format("header part:"+header.getValue()+"\n");
    // }
    // if (response != null) { 
    //   HttpEntity resEntity = response.getEntity(); 
    //   if (resEntity != null) { 
    //     result = EntityUtils.toString(resEntity, "UTF-8"); 
    //   } 
    // } 
  
    // return result; 
  } 
    
  public static String doGet(HttpClient httpClient, String url, Map<String, String> paramHeader,
                             Map<String, String> paramBody) throws Exception {
//   url为拼接后的完整路径
    String result = null; 
    HttpGet httpGet = new HttpGet(url);
    setHeader(httpGet, paramHeader); 
  
    HttpResponse response = httpClient.execute(httpGet); 
    if (response != null) { 
      HttpEntity resEntity = response.getEntity(); 
      if (resEntity != null) { 
        result = EntityUtils.toString(resEntity, "UTF-8"); 
      } 
    } 
  
    return result; 
  } 
  
  private static void setHeader(HttpRequestBase request, Map<String, String> paramHeader) {
    // 设置Header 
    if (paramHeader != null) { 
      Set<String> keySet = paramHeader.keySet(); 
      for (String key : keySet) { 
        request.addHeader(key, paramHeader.get(key)); 
      } 
    } 
  } 
  
  private static void setBody(HttpPost httpPost, Map<String, String> paramBody, String charset) throws Exception {
    // 设置参数 
    if (paramBody != null) { 
      System.out.format("param body: "+paramBody+"\n");
      List<NameValuePair> list = new ArrayList<NameValuePair>();    
      Set<String> keySet = paramBody.keySet(); 
      for (String key : keySet) { 
        list.add(new BasicNameValuePair(key, paramBody.get(key))); 
      } 
      if (list.size() > 0) { 
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
        httpPost.setEntity(entity); 
      } 
    } 
  } 
} 
