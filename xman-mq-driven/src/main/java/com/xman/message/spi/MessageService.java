package com.xman.message.spi;

import com.xman.message.listener.EventListener;

import java.util.Set;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 13:01
 */
public interface MessageService {

    /**
     * 发布消息
     * @param topic
     * @param message
     */
    void publish(String topic, String message);

    /**
     * 订阅消息
     * @param topics
     */
    void subscribe(Set<String> topics, EventListener listener);

    /**
     * 停止Mq订阅
     * @throws Exception
     */
    void stop() throws Exception;

    String getMq();
}
