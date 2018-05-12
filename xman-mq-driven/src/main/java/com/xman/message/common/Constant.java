package com.xman.message.common;

/**
 * @author yangxiang@
 * @Date 2015/11/2
 * @Time 10:16
 */
public class Constant {

    /* 默认线程数量 */
    public static final int defaultThreadNum = 10;
    /* kafka默认分区 */
    public static final int defaultPartitions = 1;

    /* Mq类型 */
    public static final String MQKafka = "kafka";
    public static final String MQBeanstalkd = "beanstalkd";
    public static final String MQRocketmq = "rocketmq";


}
