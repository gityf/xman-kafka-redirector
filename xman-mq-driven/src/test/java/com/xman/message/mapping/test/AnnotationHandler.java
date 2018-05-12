package com.xman.message.mapping.test;

import com.xman.message.annotation.MQHanlder;
import com.xman.message.annotation.RegisterMQ;
import com.xman.message.common.ConditionRelation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangxiang@
 * @Date 2015/11/27
 * @Time 16:41
 */
@MQHanlder
@Component
public class AnnotationHandler {

    @RegisterMQ(topic = "test", to = "test1", keys = {"status", "order"}, values = {"1", "2"}, cr = ConditionRelation.AND)
    public String test(String data) {
        return "HelloWorld:" + data;
    }

    @RegisterMQ(topic = "newsfeed.transfer.pre", to = "test1")
    public Map<String,Object> rMsg(String data) {
        MessageBean bean = new MessageBean();
        bean.setEmail("ss@163.com");
        bean.setUserName("sdfs");
        bean.setPassword("zzz");

        Map<String, Object> s = new HashMap<>();
        s.put("messageBean", bean);
        return s;
    }

//    @RegisterMQ(topic = "test1", to = "test")
//    public MessageBean rrMsg(MessageBean messageBean) {
//        return messageBean;
//    }
}
