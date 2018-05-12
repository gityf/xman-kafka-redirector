package com.xman.rest.http.test;

import com.xman.StartServer;
import org.junit.runner.RunWith;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by yx on 2015/10/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {StartServer.class})
public class HttpTest {
    protected RestTemplate restTemplate = new RestTemplate();

    /*
    *初始化RestTemplate，RestTemplate会默认添加HttpMessageConverter
    * 添加的StringHttpMessageConverter非UTF-8
    * 所以先要移除原有的StringHttpMessageConverter，
    * 再添加一个字符集为UTF-8的StringHttpMessageConvert
    * */
    public void reInitMessageConverter() {
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        HttpMessageConverter<?> converterTarget = null;
        for (HttpMessageConverter<?> item : converterList) {
            if (item.getClass() == StringHttpMessageConverter.class) {
                converterTarget = item;
                break;
            }
        }
        if (converterTarget != null) {
            converterList.remove(converterTarget);
        }
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converterList.add(converter);
        restTemplate.setMessageConverters(converterList);
    }
}
