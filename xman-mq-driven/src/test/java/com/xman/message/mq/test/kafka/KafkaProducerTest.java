package com.xman.message.mq.test.kafka;

import com.xman.message.AbstractTest;
import com.xman.message.mq.kafka.KafkaProducerFactory;
import com.xman.message.pool.Pool;
import com.xman.message.mq.kafka.KafkaProducer;
import com.xman.message.mq.kafka.KafkaProducerValidator;
import com.xman.message.pool.PoolFactory;
import kafka.javaapi.producer.Producer;
import org.junit.Test;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 22:57
 */
public class KafkaProducerTest extends AbstractTest {
    public final static String TOPIC = "test";
    public static final String brokerList = "10.10.33.23:9092";
    //    public static final String brokerList = "10.10.35.208:9092";
    private String message = "{\"data\": \"yx\", " +
            "\"status\": \"1\"," +
            "\"order\": \"2\"" +
            "}";

    @Test
    public void testProduce() throws Exception {
        Pool<Producer<String, String>> producerPool = PoolFactory.newBoundedBlockingPool(
                1,
                new KafkaProducerFactory(brokerList),
                new KafkaProducerValidator());
        Producer<String, String> kPro = producerPool.get();
        KafkaProducer producer = new KafkaProducer(kPro);
        producer.setTopic(TOPIC);
        producer.setMessage(message);
        // 启动线程执行
        Thread t1 = new Thread(producer);
        t1.start();
        t1.join();
        producerPool.shutdown();
    }

}