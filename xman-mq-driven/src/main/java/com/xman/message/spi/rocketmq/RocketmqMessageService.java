package com.xman.message.spi.rocketmq;

import com.xman.message.common.Constant;
import com.xman.message.listener.EventListener;
import com.xman.message.spi.MessageService;

import java.util.Set;

/**
 * Created by wangyaofu on 2018/2/7.
 */
public class RocketmqMessageService implements MessageService {
    @Override
    public void publish(String topic, String message) {

    }

    @Override
    public void subscribe(Set<String> topics, EventListener listener) {

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public String getMq() {
        return Constant.MQRocketmq;
    }
}
