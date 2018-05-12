package com.xman.message.mq.kafka;

import com.xman.message.listener.EventListener;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class KafkaConsumerRunnable implements Callable {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerRunnable.class);

    private KafkaStream mStream;
    private int mThreadNumber;
    private EventListener listener;
    private long internalMs = 1000;
    public volatile boolean exit = false;
    private long lastOffsetPrintTime = 0;
    private long offsetPrintTimeInterval = 2000;

    public KafkaConsumerRunnable(KafkaStream aStream, int aThreadNumber, EventListener listener) {
        this.mThreadNumber = aThreadNumber;
        this.mStream = aStream;
        this.listener = listener;
    }

    public void setInternalMs(long internalMs) {
        this.internalMs = internalMs;
    }

    @Override
    public Object call() throws Exception {
        ConsumerIterator<byte[], byte[]> it = this.mStream.iterator();
        while (it.hasNext()) {
            // 监听器接收消息

            try {
                MessageAndMetadata<byte[], byte[]> messageAndMetadata = it.next();
                listener.receiveMessage(messageAndMetadata.topic(), messageAndMetadata.message());
                long startTime = System.currentTimeMillis();
                long endTime = System.currentTimeMillis();
                logger.debug("receive message dispatch event costs {} ms", endTime - startTime);
                if (lastOffsetPrintTime + offsetPrintTimeInterval < startTime) {
                    lastOffsetPrintTime = startTime + offsetPrintTimeInterval;
                    logger.info(Thread.currentThread().getThreadGroup() + "-" + Thread.currentThread().getName() +
                                    " receive message from partition [{}, {}] offset[{}]",
                            messageAndMetadata.topic(), messageAndMetadata.partition(), messageAndMetadata.offset());
                }
            } catch (Exception e) {
                logger.error(Thread.currentThread().getThreadGroup()
                        + "-" + Thread.currentThread().getName()
                        + " err=" + e.getCause());
            }
        }
        return null;
    }
}