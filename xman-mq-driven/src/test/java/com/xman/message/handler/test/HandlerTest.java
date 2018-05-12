package com.xman.message.handler.test;

import com.xman.message.handler.json.DefaultMessageHandler;
import com.xman.message.AbstractTest;
import com.xman.message.handler.AbstractMessageHandler;
import com.xman.message.listener.MessageEvent;
import com.xman.message.mapping.test.NotifyMappingTest;
import com.xman.message.mq.kafka.KafkaMessageDrivenStarter;
import com.xman.message.mq.test.kafka.KafkaProducerTest;
import com.xman.message.reflect.Invocation;
import com.xman.message.spi.MessageService;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 23:19
 */
public class HandlerTest extends AbstractTest {
    private String message = "{\"data\": \"yx\"}";
    public static final String zooKeeper = "10.10.33.23:3181";
    public static final String groupId = "push_token";

    @Test
    public void testDohandle() throws Exception {
        ConcurrentHashMap<String, List<Invocation>> mappings = NotifyMappingTest.getMappings();
        // 创建消息处理器：JSON格式
        AbstractMessageHandler messageHandler = new DefaultMessageHandler();
        Invocation invocation = mappings.get(KafkaProducerTest.TOPIC).get(0);
        Object[] args = {"yx"};
        Object result = messageHandler.doHandle(invocation, args);
        assertNotNull(result);
    }

    @Test
    public void testDoPushlish() {
        AbstractMessageHandler messageHandler = new DefaultMessageHandler();
        // get messageService
        KafkaMessageDrivenStarter starter = new KafkaMessageDrivenStarter();
        starter.setKafkaZookeeper(zooKeeper);
        starter.setKafkaGroupId(groupId);
        starter.setKafkaBrokerList(KafkaProducerTest.brokerList);
        MessageService messageService = starter.spiLoadService();
        messageHandler.setMessageService(messageService);

        messageHandler.doPublish("test1", "Hello world");
    }

    @Test
    public void testHandle() throws Exception {
        ConcurrentHashMap<String, List<Invocation>> mappings = NotifyMappingTest.getMappings();
        // 创建消息处理器：JSON格式
        AbstractMessageHandler messageHandler = new DefaultMessageHandler();
        // get messageService
        KafkaMessageDrivenStarter starter = new KafkaMessageDrivenStarter();
        starter.setKafkaZookeeper(zooKeeper);
        starter.setKafkaGroupId(groupId);
        starter.setKafkaBrokerList(KafkaProducerTest.brokerList);
        MessageService messageService = starter.spiLoadService();
        messageHandler.setMessageService(messageService);

        Invocation invocation = mappings.get(KafkaProducerTest.TOPIC).get(0);

        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("data", "yx");
        MessageEvent event = new MessageEvent(requestParam);

        messageHandler.handle(invocation, event);

    }

}
