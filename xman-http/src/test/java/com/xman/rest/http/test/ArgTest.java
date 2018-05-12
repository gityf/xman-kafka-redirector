package com.xman.rest.http.test;

import com.alibaba.fastjson.JSON;
import com.xman.service.impl.Request;
import com.xman.service.impl.ReturnMsg;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by yx on 2015/10/19.
 */
public class ArgTest extends HttpTest {

    private static final String url = "http://127.0.0.1:8081/";
    private static final String appId = "100001";
    private static final String token = "101927sjdldhyxbsk";

    @Test
    public void testEx() {
        Map<String, Object> ma = new HashMap<>();
        reInitMessageConverter();
        Request request = new Request();
        request.setUsername("username");
        request.setPassword("password");
        ma.put("request", request);
        ma.put("appId", appId);
        ma.put("token", token);
        String data = JSON.toJSONString(ma);
        System.out.println(data);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url + "test01", data, String.class);
            System.out.println(responseEntity.getHeaders());
            System.out.println(responseEntity.getBody());
            assertEquals("response code error", HttpStatus.OK, responseEntity.getStatusCode());
            ReturnMsg returnMsg = JSON.parseObject(responseEntity.getBody(), ReturnMsg.class);
            assertEquals(returnMsg.getErrorMsg(), 0, returnMsg.getErrorCode());
            System.out.println(returnMsg.getData());
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getResponseBodyAsString());
        }

    }

    @Test
    public void testPostKV() {
        Map<String, String> params=new HashMap<String,String>();
        params.put("appId", "xxxxx");
        params.put("token", "token&");
        params.put("password", "password");

        HttpClient.HttpResult result=HttpClient.post(url + "test001", params);

        System.out.println("回调:" + result.getStatusCode());
        System.out.println("返回字符串:" + result.getBody());
    }

    @Test
    public void test() {
        Map<String, String> params=new HashMap<String,String>();
        params.put("appId", "xxxxx");
        params.put("token", "token&");
        params.put("password", "password");

        HttpClient.HttpResult result=HttpClient.post(url + "test", params);

        System.out.println("回调:" + result.getStatusCode());
        System.out.println("返回字符串:" + result.getBody());

    }

    public static void main(String[] args) {
    }
}
