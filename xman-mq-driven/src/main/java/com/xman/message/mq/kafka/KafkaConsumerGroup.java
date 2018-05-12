package com.xman.message.mq.kafka;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xman.message.common.Constant;
import com.xman.message.exception.SubscribeException;
import com.xman.message.listener.EventListener;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class KafkaConsumerGroup implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerGroup.class);

    private final ConsumerConnector consumer;
    private ExecutorService executorService;
    private EventListener listener;
    private String zookeeper;
    private String groupId = "default-topic-group";
    private boolean isOffsetReset;
    private int threadNum;
    private Set<String> topics;
    /* pull间隔时间 */
    private long internalMs = 1000;
    public volatile boolean exit = false;

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public KafkaConsumerGroup(String zookeeper, String groupId, int threadNum, Set<String> topics, boolean offsetReset) {
        this.zookeeper = zookeeper;
        this.groupId = groupId;
        this.isOffsetReset = offsetReset;
        if (threadNum <= 0) {
            threadNum = Constant.defaultThreadNum;
        }
        this.threadNum = threadNum;
        // TODO 优化 使用对象连接池管理consumerConnector
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
        this.topics = topics;
    }

    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executorService != null) executorService.shutdown();
    }

    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        for (String topic : this.topics) {
            if (StringUtils.isEmpty(topic)) {
                continue;
            }
            topicCountMap.put(topic, new Integer(this.threadNum));
        }
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap =
                consumer.createMessageStreams(topicCountMap);

        // 启动所有线程
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("[MGD] Watcher-%d")
//                .setDaemon(true)
                .build();
        executorService = Executors.newFixedThreadPool(this.threadNum * this.topics.size(), threadFactory);
        try {
            List<Callable<Object>> tasks = new ArrayList<>();
            for (String topic : topics) {
                List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
                // 开始消费消息
                int threadNumber = 0;
                for (final KafkaStream stream : streams) {
                    KafkaConsumerRunnable runnable = new KafkaConsumerRunnable(stream, threadNumber, this.listener);
                    runnable.setInternalMs(this.internalMs);
                    tasks.add(runnable);
                    threadNumber++;
                }
            }
            executorService.invokeAll(tasks);
        } catch (Exception e) {
            logger.warn("获取consumer消息失败。topics:" + this.topics);
            e.printStackTrace();
        }

    }

    private ConsumerConfig createConsumerConfig() {
        if (StringUtils.isEmpty(this.zookeeper)) {
            throw new SubscribeException("创建KafkaConsumerGroup失败，未指定zookeeper地址");
        }
        Properties props = new Properties();
        //zookeeper 配置
        props.put("zookeeper.connect", this.zookeeper);
        //group 代表一个消费组
        props.put("group.id", this.groupId);
        //zk连接超时
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        if (this.isOffsetReset) {
            props.put("auto.offset.reset", "smallest");
        }
        // consumer
//        props.put("consumer.timeout.ms", "1000");
//        props.put("auto.offset.reset", "smallest");
        //序列化类
//        props.put("serializer.class", "kafka.serializer.StringEncoder");

        return new ConsumerConfig(props);
    }

    public void setInternalMs(long internalMs) {
        this.internalMs = internalMs;
    }
}