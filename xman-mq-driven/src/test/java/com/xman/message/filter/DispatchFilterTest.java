package com.xman.message.filter;

import com.xman.message.handler.json.DefaultMessageHandler;
import com.xman.message.AbstractTest;
import com.xman.message.mq.kafka.KafkaMessageDrivenStarter;
import com.xman.message.handler.AbstractMessageHandler;
import com.xman.message.listener.MessageEvent;
import com.xman.message.mapping.test.NotifyMappingTest;
import com.xman.message.mq.test.kafka.KafkaProducerTest;
import com.xman.message.reflect.Invocation;
import com.xman.message.spi.MessageService;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangxiang@
 * @Date 2015/11/30
 * @Time 12:23
 */
public class DispatchFilterTest extends AbstractTest {
    public static final String zooKeeper = "10.10.33.23:3181";
    public static final String groupId = "push_token";

    DispatchFilter filter = new DispatchFilter();

    @Test
    public void testFitCondition() throws Exception {
        ConcurrentHashMap<String, List<Invocation>> mappings = NotifyMappingTest.getMappings();
        Invocation invocation = mappings.get("test").get(0);
        MessageEvent event = new MessageEvent();
        event.setAttribute("status", 1);
        event.setAttribute("order", 3);

        boolean fit = filter.fitCondition(invocation, event);
        assertTrue(fit);
    }

    @Test
    public void testDoFilter() throws Exception {
        KafkaMessageDrivenStarter starter = new KafkaMessageDrivenStarter();
        starter.setKafkaZookeeper(zooKeeper);
        starter.setKafkaGroupId(groupId);
        starter.setKafkaBrokerList(KafkaProducerTest.brokerList);
        MessageService messageService = starter.spiLoadService();
        AbstractMessageHandler messageHandler = new DefaultMessageHandler();
        messageHandler.setMessageService(messageService);
        filter.setMessageHandler(messageHandler);

        ConcurrentHashMap<String, List<Invocation>> mappings = NotifyMappingTest.getMappings();
        List<Invocation> invocations = mappings.get("test");
        MessageEvent event = new MessageEvent();
        event.setAttribute("data", "yx");
        event.setAttribute("status", 1);
        event.setAttribute("order", 2);

        filter.doFilter(invocations, event);
    }
}
