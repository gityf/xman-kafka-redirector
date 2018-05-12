package com.xman.message.spi.kafka;

import com.xman.message.common.Constant;
import com.xman.message.exception.ProduceException;
import com.xman.message.exception.SubscribeException;
import com.xman.message.listener.EventListener;
import com.xman.message.mq.kafka.KafkaProducer;
import com.xman.message.pool.Pool;
import com.xman.message.spi.MessageService;
import com.xman.message.mq.kafka.KafkaConsumerGroup;
import kafka.javaapi.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 16:30
 */
public class KafkaMessageService implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageService.class);

    private String brokerList;
    private String zookeeper;
    private String groupId;
    private boolean isOffsetReset;
    private int partitions = Constant.defaultPartitions;
    private Integer internalMs;
    // default 10 threads
    ExecutorService executorPublish = Executors.newFixedThreadPool(10);
    ExecutorService executorSubscribe = Executors.newSingleThreadExecutor();
    private KafkaConsumerGroup kafkaConsumerGroup;
    private long publishWaitTimeout = 3000;
    private Pool<Producer<String, String>> producerPool;

    @Override
    public void publish(String topic, String message) {
        if (StringUtils.isEmpty(this.brokerList)) {
            throw new ProduceException("publish消息失败，未指定brokerList");
        }
        if (this.producerPool == null) {
            throw new ProduceException("publish消息失败，produce pool is null");
        }
        Producer<String, String> kafProducer = producerPool.get();
        if (kafProducer != null) {
            KafkaProducer producer = new KafkaProducer(kafProducer);
            producer.setTopic(topic);
            producer.setMessage(message);
            // 启动线程执行
            Future future = executorPublish.submit(producer);
            try {
                future.get();
            } catch (InterruptedException e) {
                logger.warn("发布消息线程被中断，" + e.getCause());
            } catch (ExecutionException e) {
                logger.warn("发布消息线程执行异常，" + e.getCause());
            }

            // return to the pool
            producerPool.release(kafProducer);
        }
    }

    @Override
    public void subscribe(Set<String> topics, EventListener listener) {
        if (topics == null || topics.size() <= 0) {
            throw new SubscribeException("订阅失败,请指定需要订阅的topics");
        }
        if (StringUtils.isEmpty(this.zookeeper) || StringUtils.isEmpty(this.groupId)) {
            throw new SubscribeException("订阅" + topics + ",请指定zookeeper地址和消费组groupId");
        }
        kafkaConsumerGroup = new KafkaConsumerGroup(this.zookeeper, this.groupId, this.partitions, topics, this.isOffsetReset);
        kafkaConsumerGroup.setListener(listener);
        // 如果设置了pull间隔时间，则设置；否则使用默认间隔时间
        if (this.internalMs != null) {
            kafkaConsumerGroup.setInternalMs(this.internalMs);
        }

        // 启动线程开始pull
        Future future = executorSubscribe.submit(kafkaConsumerGroup);
        try {
            future.get();
        } catch (InterruptedException e) {
            logger.warn("订阅消息线程被中断，" + e.getCause());
        } catch (ExecutionException e) {
            logger.warn("订阅消息线程执行异常，" + e.getCause());
        }
    }

    public void stop() throws Exception {
        if (kafkaConsumerGroup != null) {
            kafkaConsumerGroup.exit = true;
            kafkaConsumerGroup.shutdown();
        }
        if (executorSubscribe != null) {
            executorSubscribe.shutdown();
        }

        if (producerPool != null) {
            try {
                producerPool.shutdown();
            } catch (Exception e) {
                logger.warn("shutdown produce pool failed! cause: " + e.getMessage());
            }
        }
    }

    @Override
    public String getMq() {
        return Constant.MQKafka;
    }

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isOffsetReset() {
        return isOffsetReset;
    }

    public void setOffsetReset(boolean offsetReset) {
        isOffsetReset = offsetReset;
    }

    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    public void setInternalMs(Integer internalMs) {
        this.internalMs = internalMs;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    public void setPublishWaitTimeout(long publishWaitTimeout) {
        this.publishWaitTimeout = publishWaitTimeout;
    }

    public void setProducerPool(Pool<Producer<String, String>> producerPool) {
        this.producerPool = producerPool;
    }
}
