package com.xman.message;

import com.xman.message.mapping.test.SpringConfiguration;
import com.xman.message.mq.kafka.KafkaMessageDrivenStarter;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yangxiang@
 * @Date 2015/11/2
 * @Time 12:11
 */
public class StarterTest {
    private static final String brokerList = "10.10.33.23:9092";
    private static final String zookeeper = "10.10.33.23:3181";
    private static final String groupId = "push_token";
    public static final String[] scanPackages = {"com.yiche.common.service.md"};

    @Test
    public void testStarter() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        KafkaMessageDrivenStarter starter = new KafkaMessageDrivenStarter();
        starter.setContext(context);
        starter.setKafkaZookeeper(zookeeper);
        starter.setKafkaGroupId(groupId);
        starter.setScanPackages(scanPackages);
        starter.setKafkaBrokerList(brokerList);
        starter.setCorePoolSize(4);
        starter.setMaxPoolSize(8);
        starter.setIdlTimeout(3000l);
        starter.setWorkQueueSize(100);
        starter.setKafkaPartitions(6);
        try {
            starter.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // stop
        try {
            Thread.sleep(10000);
            starter.stop();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
