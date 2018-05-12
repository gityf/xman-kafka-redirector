package com.xman.message.listener.test;

import com.xman.message.handler.json.DefaultMessageHandler;
import com.xman.message.AbstractTest;
import com.xman.message.handler.AbstractMessageHandler;
import com.xman.message.listener.EventListener;
import com.xman.message.listener.MessageEvent;
import com.xman.message.mapping.test.NotifyMappingTest;
import com.xman.message.mq.kafka.KafkaMessageDrivenStarter;
import com.xman.message.mq.test.kafka.KafkaProducerTest;
import com.xman.message.reflect.Invocation;
import com.xman.message.spi.MessageService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangxiang@
 * @Date 2015/11/30
 * @Time 12:22
 */
public class MessageListenerTest extends AbstractTest {
    private String message = "{\"data\": \"yx\", " +
            "\"status\": \"1\"," +
            "\"order\": \"2\"" +
            "}";
    public static final String zooKeeper = "10.10.33.23:3181";
    public static final String groupId = "push_token";
    public static final String topic = "test";

//    EventListener listener = new MessageListener();
    EventListener listener = null;

    @Before
    public void init() throws Exception {
        ConcurrentHashMap<String, List<Invocation>> mappings = NotifyMappingTest.getMappings();
        listener.setMappings(mappings);
        // 创建消息处理器：JSON格式
        AbstractMessageHandler messageHandler = new DefaultMessageHandler();
        // get messageService
        KafkaMessageDrivenStarter starter = new KafkaMessageDrivenStarter();
        starter.setKafkaZookeeper(zooKeeper);
        starter.setKafkaGroupId(groupId);
        starter.setKafkaBrokerList(KafkaProducerTest.brokerList);
        MessageService messageService = starter.spiLoadService();
        messageHandler.setMessageService(messageService);
        listener.setMessageHandler(messageHandler);
    }

    @Test
    public void testFireEvent() {
        MessageEvent event = new MessageEvent();
        event.setTopic("test");
        event.setAttribute("data", "yx");
        event.setAttribute("status", 1);
        event.setAttribute("order", 2);

        listener.fireEvent(event);
    }

    @Test
    public void testReceiveMessage() throws Exception {
        listener.receiveMessage(topic, message.getBytes());
    }


}
