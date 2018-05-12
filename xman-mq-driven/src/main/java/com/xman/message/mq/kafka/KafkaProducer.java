package com.xman.message.mq.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 22:57
 */
public class KafkaProducer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private Producer<String, String> producer;
    private String topic;
    private String message;

    public KafkaProducer(Producer<String, String> producer) {
        this.producer = producer;
    }

    public void run() {
        String messageNo = UUID.randomUUID().toString();
        String key = messageNo;
        long startTime = System.currentTimeMillis();
        producer.send(new KeyedMessage<String, String>(this.topic, key, this.message));
        long endTime = System.currentTimeMillis();
        logger.debug("[MGD]publish message costs " + (endTime - startTime) + " ms");
        logger.info("[MGD]publish message topic[{}] key[{}] - {}", this.topic, key, this.message);
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}