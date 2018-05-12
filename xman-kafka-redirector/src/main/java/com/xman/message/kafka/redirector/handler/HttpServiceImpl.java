package com.xman.message.kafka.redirector.handler;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangyaofu on 2018/4/25.
 */
@Service
public class HttpServiceImpl {

    public final static int CONNECT_TIMEOUT =2000;
    public final static int READ_TIMEOUT=2000;
    public final static int WRITE_TIMEOUT=3000;

    public String httpGet(String url) {
            final Request request = new Request.Builder()
                    .url(url).header("User-Agent", "okhttp3.client")
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .build();
        Response response;
        try {
            response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
        }
        return null;
    }

    public String httpPost(String url, String body) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .build();
            Response response=okHttpClient.newCall(request).execute();
            if (response != null && response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
        }
        return null;
    }
}