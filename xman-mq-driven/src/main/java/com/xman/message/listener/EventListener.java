package com.xman.message.listener;

import com.xman.message.handler.AbstractMessageHandler;
import com.xman.message.reflect.Invocation;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangxiang@
 * @Date 2015/11/29
 * @Time 16:10
 */
public interface EventListener {

    void receiveMessage(String topic, byte[] data);

    void fireEvent(MessageEvent event);

    void setMessageHandler(AbstractMessageHandler messageHandler);

    void setMappings(ConcurrentHashMap<String, List<Invocation>> mappings);
}
