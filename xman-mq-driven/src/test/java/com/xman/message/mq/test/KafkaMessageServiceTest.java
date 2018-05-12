package com.xman.message.mq.test;

import com.xman.message.AbstractTest;
import com.xman.message.spi.kafka.KafkaMessageService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yangxiang@
 * @Date 2015/11/27
 * @Time 19:45
 */
public class KafkaMessageServiceTest extends AbstractTest {

    KafkaMessageService kafkaMessageService = new KafkaMessageService();

    @Before
    public void init() {
        kafkaMessageService.setBrokerList("10.10.33.23:9092");
        kafkaMessageService.setPartitions(2);
        kafkaMessageService.setGroupId("push_token");
        kafkaMessageService.setZookeeper("10.10.33.23:3181");
    }

    @Test
    public void testPublish() {
        kafkaMessageService.publish("test", "Hello world service");
    }

    @Test
    public void testSubscribe() {
        Set<String> topics = new HashSet<>();
        topics.add("test");
//        EventListener listener = new MessageListener();
//        kafkaMessageService.subscribe(topics, listener);
    }
}
