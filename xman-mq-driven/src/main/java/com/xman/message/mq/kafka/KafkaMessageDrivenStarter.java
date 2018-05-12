package com.xman.message.mq.kafka;

import com.xman.message.common.Constant;
import com.xman.message.exception.SPILoadServiceException;
import com.xman.message.handler.json.DefaultMessageHandler;
import com.xman.message.handler.AbstractMessageHandler;
import com.xman.message.pool.Pool;
import com.xman.message.exception.ExceptionCode;
import com.xman.message.exception.MessageDrivenExcpetiion;
import com.xman.message.listener.MessageListener;
import com.xman.message.notify.NotifyMapping;
import com.xman.message.pool.PoolFactory;
import com.xman.message.reflect.Invocation;
import com.xman.message.spi.MessageService;
import com.xman.message.spi.kafka.KafkaMessageService;
import kafka.javaapi.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 12:55
 */
public class KafkaMessageDrivenStarter {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageDrivenStarter.class);

    // 支持多包扫描
    private String[] scanPackages;
    private ApplicationContext context;
    private String mqAddress;
    private String kafkaBrokerList;
    private String kafkaZookeeper;
    private Integer kafkaPartitions;
    private String kafkaGroupId;
    private MessageService messageService;
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer workQueueSize;
    private Integer producePoolSize;
    private Long idlTimeout;
    private boolean isOffsetReset;
    private Pool<Producer<String, String>> producerPool;

    public void start() throws Exception {
        logger.info("[MQD]starting message driven .........");

        // SPI加载Service
        messageService = spiLoadService();
        if (messageService == null) {
            logger.warn("[MQD]MQ没有实现类");
            throw new SPILoadServiceException("MQ没有实现类.");
        }
        NotifyMapping notifyMapping = new NotifyMapping(scanPackages, context);
        ConcurrentHashMap<String, List<Invocation>> mappings = notifyMapping.getMappings();
        if (mappings == null || mappings.size() == 0) {
            // log
            return;
        }

        // 创建消息处理器：默认处理器
        AbstractMessageHandler messageHandler = new DefaultMessageHandler();
        messageHandler.setMessageService(messageService);

        // 绑定监听器
        final MessageListener listener = new MessageListener(this.corePoolSize, this.maxPoolSize,
                this.workQueueSize, this.idlTimeout);
        listener.setMappings(mappings);
        listener.setMessageHandler(messageHandler);

        final Set<String> topics = new HashSet<>();
        Enumeration<String> keyEnum = mappings.keys();
        while (keyEnum.hasMoreElements()) {
            topics.add(keyEnum.nextElement());
        }
        // 订阅监听
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                messageService.subscribe(topics, listener);
            }
        });
        thread.start();
        logger.info("[MQD]started message driven success!!!");
    }

    public void stop() throws Exception {
        logger.info("[MQD]stop message driven ....");
        messageService.stop();
        logger.info("[MQD]stopped message driven success!!!");
    }

    /**
     * SPI机制，加载实现的MessageService
     *
     * @return
     */
    public MessageService spiLoadService() {
        ServiceLoader<MessageService> messageServices = ServiceLoader.load(MessageService.class);
        Iterator<MessageService> messageServiceIterator = messageServices.iterator();
        while (messageServiceIterator.hasNext()) {
            MessageService messageService = messageServiceIterator.next();
            // kafka
            if (Constant.MQKafka.equals(messageService.getMq())) {
                logger.info("[MQD]正在配置Kafka...... ");
                if (StringUtils.isEmpty(this.kafkaBrokerList) || StringUtils.isEmpty(this.kafkaGroupId)
                        || StringUtils.isEmpty(this.kafkaZookeeper)) {
                    logger.warn("[MQD]初始化KafkaService失败。请检查brokerList/groupId/zookeeper是否已配置");
                    throw new MessageDrivenExcpetiion(ExceptionCode.KafkaConfigError,
                            "初始化KafkaService失败。请检查brokerList/groupId/zookeeper是否已配置");
                }
                KafkaMessageService kafkaMessageService = (KafkaMessageService) messageService;
                kafkaMessageService.setBrokerList(this.kafkaBrokerList);
                kafkaMessageService.setGroupId(this.kafkaGroupId);
                kafkaMessageService.setOffsetReset(this.isOffsetReset);
                if (this.kafkaPartitions != null && this.kafkaPartitions > 0) {
                    kafkaMessageService.setPartitions(this.kafkaPartitions);
                }

                kafkaMessageService.setZookeeper(this.kafkaZookeeper);
                logger.info("[MQD]绑定Kafka配置brokerList:{" + this.kafkaBrokerList + "}, " +
                        "zookeeper:{" + this.kafkaZookeeper + "}, " +
                        "groupId:{" + this.kafkaGroupId + "}");

                logger.info("[MQD]init Kafka worker pool...");
                // init produce pool
                producerPool = PoolFactory.newBoundedBlockingPool(
                        this.producePoolSize,
                        new KafkaProducerFactory(this.kafkaBrokerList),
                        new KafkaProducerValidator());
                logger.info("[MQD]init Kafka worker pool success!!! [brokerList:{}, pool size:{}]",
                        this.kafkaBrokerList, this.producePoolSize);
                kafkaMessageService.setProducerPool(producerPool);

                return kafkaMessageService;
            }
        }
        return null;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public void setMqAddress(String mqAddress) {
        this.mqAddress = mqAddress;
    }

    public void setKafkaBrokerList(String kafkaBrokerList) {
        this.kafkaBrokerList = kafkaBrokerList;
    }

    public void setKafkaZookeeper(String kafkaZookeeper) {
        this.kafkaZookeeper = kafkaZookeeper;
    }

    public void setKafkaGroupId(String kafkaGroupId) {
        this.kafkaGroupId = kafkaGroupId;
    }

    public void setKafkaPartitions(Integer kafkaPartitions) {
        this.kafkaPartitions = kafkaPartitions;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setWorkQueueSize(Integer workQueueSize) {
        this.workQueueSize = workQueueSize;
    }

    public void setProducePoolSize(Integer producePoolSize) {
        this.producePoolSize = producePoolSize;
    }

    public void setIdlTimeout(Long idlTimeout) {
        this.idlTimeout = idlTimeout;
    }

    public void setOffsetReset(boolean offsetReset) {
        isOffsetReset = offsetReset;
    }

    public MessageService getMessageService() {
        return messageService;
    }
}
