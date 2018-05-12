package com.xman.message;

import com.xman.message.mq.kafka.KafkaProducer;
import com.xman.message.mq.kafka.KafkaProducerFactory;
import org.junit.Test;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 22:57
 */
public class KafkaProducerTest extends AbstractTest {
    public final static String TOPIC = "topic-test-json";
    public static final String brokerList = "127.0.0.1:9092";
    private String message = "{\"data\": \"yx\", " +
            "\"status\": \"1\"," +
            "\"order\": \"2\"" +
            "}";

    @Test
    public void testProduce() throws Exception {
        KafkaProducerFactory factory = new KafkaProducerFactory(this.brokerList);
        KafkaProducer producer = new KafkaProducer(factory.createNew());
        producer.setTopic(TOPIC);
        producer.setMessage(message);
        // 启动线程执行
        Thread t1 = new Thread(producer);
        t1.start();
        t1.join();
    }

}