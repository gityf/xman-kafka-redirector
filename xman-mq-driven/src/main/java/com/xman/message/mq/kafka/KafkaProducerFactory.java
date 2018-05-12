package com.xman.message.mq.kafka;

import com.xman.message.exception.ProduceException;
import com.xman.message.pool.ObjectFactory;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import org.springframework.util.StringUtils;

import java.util.Properties;

public class KafkaProducerFactory implements ObjectFactory<Producer<String, String>> {
    private String brokerList;

    public KafkaProducerFactory(String brokerList) {
        this.brokerList = brokerList;
    }

    public Producer<String, String> createNew() {
        try {
            return new Producer<String, String>(createProducerConfig());
        } catch (Exception se) {
            throw new IllegalArgumentException(
                    "Unable to create new Kafka producer", se);
        }
    }

    private ProducerConfig createProducerConfig() {
        if (StringUtils.isEmpty(this.brokerList)) {
            throw new ProduceException("创建KafkaProducer失败，未指定brokerList");
        }
        Properties props = new Properties();
        //此处配置的是kafka的端口
        props.put("metadata.broker.list", this.brokerList);
        //配置value的序列化类
        props.put("serializer.class", "kafka.serializer.StringEncoder");
//        props.put("producer.type", "async");
//        props.put("compression.type", "gzip");
        //配置key的序列化类
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        //request.required.acks
        //0, which means that the producer never waits for an acknowledgement from the broker (the same behavior as 0.7). This option provides the lowest latency but the weakest durability guarantees (some data will be lost when a server fails).
        //1, which means that the producer gets an acknowledgement after the leader replica has received the data. This option provides better durability as the client waits until the server acknowledges the request as successful (only messages that were written to the now-dead leader but not yet replicated will be lost).
        //-1, which means that the producer gets an acknowledgement after all in-sync replicas have received the data. This option provides the best durability, we guarantee that no messages will be lost as long as at least one in sync replica remains.
        props.put("request.required.acks", "1");

        return new ProducerConfig(props);
    }

}