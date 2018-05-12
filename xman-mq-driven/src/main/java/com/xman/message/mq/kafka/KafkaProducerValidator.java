package com.xman.message.mq.kafka;

import com.xman.message.pool.Pool;
import kafka.javaapi.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KafkaProducerValidator implements Pool.Validator<Producer<String, String>> {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerValidator.class);


    @Override
    public boolean isValid(Producer<String, String> stringStringProducer) {
        return stringStringProducer != null;
    }

    @Override
    public void invalidate(Producer<String, String> stringStringProducer) {
        try {
            long startTime = System.currentTimeMillis();
            stringStringProducer.close();
            long endTime = System.currentTimeMillis();
            logger.debug("Kafka Producer close costs {} ms", endTime - startTime);
        } catch (Exception e) {
            logger.warn("[MGD]Kafka Producer close failed, cause:" + e.getMessage());
        }

    }
}