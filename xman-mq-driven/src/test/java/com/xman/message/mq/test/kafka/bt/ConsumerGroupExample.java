package com.xman.message.mq.test.kafka.bt;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerGroupExample {
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;
    private Set<String> topics;

    public ConsumerGroupExample(String a_zookeeper, String a_groupId, String a_topic, Set<String> topics) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
        this.topics = topics;
    }

    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
    }

    public void run(int a_numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
//        topicCountMap.put(topic, new Integer(a_numThreads));
        for (String topic : this.topics) {
            if (StringUtils.isEmpty(topic)) {
                continue;
            }
            topicCountMap.put(topic, new Integer(a_numThreads));
        }
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // 启动所有线程  
        executor = Executors.newFixedThreadPool(a_numThreads);

        // 开始消费消息  
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerTest(stream, threadNumber));
            threadNumber++;
        }
    }

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", "10.10.33.23:3181");
//        props.put("zookeeper.connect", "192.168.2.225:2183/config/mobile/mq/mafka");
        props.put("group.id", "push-token1");
        props.put("zookeeper.session.timeout.ms", "60000");
        props.put("zookeeper.sync.time.ms", "2000");
        props.put("auto.commit.interval.ms", "1000");

        return new ConsumerConfig(props);
    }

    public static void main(String[] args) {
        Set<String> topics = new HashSet<>();
        topics.add("test");
        ConsumerGroupExample example = new ConsumerGroupExample("", "", "test", topics);
        example.run(1);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {

        }
        example.shutdown();
    }
}  