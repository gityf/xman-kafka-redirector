package com.xman.message.handler;

import com.alibaba.fastjson.JSON;
import com.xman.message.exception.*;
import com.xman.message.listener.MessageEvent;
import com.xman.message.reflect.Invocation;
import com.xman.message.spi.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 16:18
 */
public abstract class AbstractMessageHandler {
    private static final Logger ILOG = LoggerFactory.getLogger(AbstractMessageHandler.class.getName());

    protected MessageService messageService;

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void handle(Invocation invocation, MessageEvent event) {
        ILOG.debug("Handler handle event" + event);
        long starTime = System.currentTimeMillis();
        if (invocation == null) {
            throw new UndefinedTopicException("Handler接收的invocation为空");
        }
        if (event == null) {
            throw new MessageEmptyException("Handler接收的的event内容为空");
        }
        Object[] args = null;
        try {
            args = getArgs(invocation, event);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            ILOG.warn("参数适配失败，" + sw.toString());
            throw new ArgumentAdaptException("Abstract Handler参数适配失败");
        }
        // 回调方法并获取处理后的结果
        Object result = this.doHandle(invocation, args);

        if (result != null) {
            ILOG.debug("Handler invoke {} return {}.",
                    invocation.getMethod().getName(),
                    JSON.toJSON(result));

            // 将处理结果回写MQ
            String toTopic = invocation.getTopic();
            if (!StringUtils.isEmpty(invocation.getTo())) {
                toTopic = invocation.getTo();
            }
            long pubStartTime = System.currentTimeMillis();
            this.doPublish(toTopic, result);
            long pubEndTime = System.currentTimeMillis();
            ILOG.debug("Handler doPublish() costs {} ms!!",
                    pubEndTime - pubStartTime);
        } else {
            ILOG.debug("Handler invoke {} return null!!",
                    invocation.getMethod().getName());
        }
        long endTime = System.currentTimeMillis();
        ILOG.debug("Handler handle() costs {} ms!!", endTime - starTime);
    }

    /**
     * 发布消息
     *
     * @param toTopic 写入的topic
     * @param message 写入的消息
     */
    public void doPublish(String toTopic, Object message) {
        if (messageService == null) {
            throw new MessageDrivenExcpetiion(ExceptionCode.MessageServiceException, "Handler未指定MessageService");
        }
        // TODO 回写入消息队列【异步】
        messageService.publish(toTopic, JSON.toJSONString(message));
    }

    public Object[] getArgs(Invocation invocation, MessageEvent event) throws Exception {
        Object[] args = new Object[invocation.getParameterNames().length];

        boolean readBody = false;
        // 只有一个参数，则read msg body
        if (invocation.getParameterNames() != null && invocation.getParameterNames().length == 1) {
            readBody = true;
        }
        for (int i = 0; i < invocation.getParameterNames().length; i++) {
            String keyName = invocation.getParameterNames()[i];
            Object keyVal = event.getAttribute(keyName);
            if (keyVal == null && readBody) {
                keyVal = event.getAllAtrributes();
            }
            if (keyVal == null) {
                throw new IllegalArgumentException("参数缺失，请检查参数" + keyName + "是否存在。");
            }
            Class clz = invocation.getParameterTypes()[i];
            Object arg = null;
            if (clz == String.class) {
                arg = keyVal;
            } else if (clz == ByteBuffer.class) {
                BASE64Decoder decoder = new BASE64Decoder();
                //Base64解码
                byte[] b = decoder.decodeBuffer(keyVal + "");
                for (int j = 0; j < b.length; ++j) {
                    if (b[j] < 0) {//调整异常数据
                        b[j] += 256;
                    }
                }
                arg = ByteBuffer.wrap(b);
            } else if (List.class.isAssignableFrom(clz)) {
                Type genericType = invocation.getGenericParameterTypes()[i];
                if (genericType instanceof ParameterizedType) {
                    Class genericClass = (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                    arg = JSON.parseArray(JSON.toJSONString(keyVal), genericClass);
                } else {
                    throw new IllegalArgumentException("参数异常，获取list泛型失败。请检查参数" + keyName + "泛型类型");
                }
            } else {
                arg = JSON.parseObject(JSON.toJSONString(keyVal), clz);
            }
            args[i] = arg;
        }
        return args;
    }

    /**
     * 过滤器处理之后的方法处理
     *
     * @param invocation
     * @param args
     * @return
     */
    public abstract Object doHandle(Invocation invocation, Object[] args);

    /**
     * 处理参数直接为数组的 [{}{}]
     *
     * @param invocation
     * @param message
     * @return
     */
    public Object getArrayArgs(Invocation invocation, String message) {
        Object arg = null;
        if (invocation.getParameterTypes() != null && invocation.getParameterTypes().length > 0) {
            Class clz = invocation.getParameterTypes()[0];
            if (List.class.isAssignableFrom(clz)) {
                Type genericType = invocation.getGenericParameterTypes()[0];
                if (genericType instanceof ParameterizedType) {
                    Class genericClass = (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                    arg = JSON.parseArray(JSON.toJSONString(message), genericClass);
                } else {
                    throw new IllegalArgumentException("参数异常，获取list泛型失败。请检查参数" +
                            invocation.getParameterNames()[0] + "泛型类型");
                }
            }
        }
        return arg;
    }

}
