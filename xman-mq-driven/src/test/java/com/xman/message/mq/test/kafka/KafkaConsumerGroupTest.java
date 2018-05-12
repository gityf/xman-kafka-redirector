package com.xman.message.mq.test.kafka;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xman.message.AbstractTest;
import com.xman.message.mq.kafka.KafkaConsumerGroup;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author yangxiang@
 * @Date 2015/11/27
 * @Time 14:48
 */
public class KafkaConsumerGroupTest extends AbstractTest {
    public static final String zookeeper = "10.10.33.23:3181";
    public static final String groupId = "push_token";
    public static final int threadNum = 3;

    @Test
    public void testConsumerKafkaMessage() {
        Set<String> topics = new HashSet<>();
        topics.add(KafkaProducerTest.TOPIC);
        final KafkaConsumerGroup example = new KafkaConsumerGroup(zookeeper, groupId, threadNum, topics, true);

        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Test_kafkaConsumerGroup-%d")
                .setDaemon(false)
                .build();
        final ExecutorService executor = Executors.newFixedThreadPool(5, threadFactory);
        Future ff = executor.submit(example);
//        executor.submit(() -> {
//            final Thread currentThread = Thread.currentThread();
//            final String oldName = currentThread.getName();
//            currentThread.setName("Processing-" + "提交");
//            try {
//                example.run();
//            } finally {
//                currentThread.setName(oldName);
//            }
//        });
        try {
//            Thread.sleep(10000);
            ff.get();
//            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        example.shutdown();
        example.exit = true;
        executor.shutdown();
    }

    private void process(String messageId) {

    }

    @Test
    public void stopConsumerGroup() {

    }
}
