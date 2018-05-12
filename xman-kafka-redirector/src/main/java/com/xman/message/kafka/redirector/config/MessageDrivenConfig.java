package com.xman.message.kafka.redirector.config;

import com.xman.message.mq.kafka.KafkaMessageDrivenStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangyaofu on 2018/5/5.
 */
@Configuration
public class MessageDrivenConfig {

    @Autowired
    ApplicationContext context;

    @Value("${kafka.zookeeper}")
    String kafkaZookeeper;
    @Value("${kafka.brokerList}")
    String kafkaBrokerList;
    @Value("${kafka.consumer.groupId}")
    String kafkaConsumerGroupId;
    @Value("${message.driven.scanPackages}")
    String messageDrivenScanPackages;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public KafkaMessageDrivenStarter starter() throws Exception {
        KafkaMessageDrivenStarter starter = new KafkaMessageDrivenStarter();
        starter.setContext(context);
        starter.setKafkaZookeeper(kafkaZookeeper);
        starter.setKafkaBrokerList(kafkaBrokerList);
        starter.setKafkaGroupId(kafkaConsumerGroupId);
        starter.setOffsetReset(true);

        starter.setCorePoolSize(10);
        starter.setMaxPoolSize(10);
        starter.setProducePoolSize(10);
        starter.setWorkQueueSize(10);
        starter.setIdlTimeout(10L);

        starter.setScanPackages(messageDrivenScanPackages.split(","));
        //starter.setWorkerThread(10);
        starter.setKafkaPartitions(4);
        return starter;
    }
}
