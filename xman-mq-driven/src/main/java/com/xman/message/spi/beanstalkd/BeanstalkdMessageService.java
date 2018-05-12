package com.xman.message.spi.beanstalkd;

import com.xman.message.common.Constant;
import com.xman.message.listener.EventListener;
import com.xman.message.spi.MessageService;

import java.util.Set;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 16:34
 */
public class BeanstalkdMessageService implements MessageService {

    @Override
    public void publish(String topic, String message) {

    }

    @Override
    public void subscribe(Set<String> topic, EventListener listener) {

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public String getMq() {
        return Constant.MQBeanstalkd;
    }
}
