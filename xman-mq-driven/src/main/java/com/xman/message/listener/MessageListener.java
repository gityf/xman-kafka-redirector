package com.xman.message.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xman.message.codec.Codec;
import com.xman.message.codec.CodecCode;
import com.xman.message.codec.support.FastJsonCodec;
import com.xman.message.common.util.BeanHelper;
import com.xman.message.exception.ExceptionCode;
import com.xman.message.exception.MessageDrivenExcpetiion;
import com.xman.message.exception.MessageEmptyException;
import com.xman.message.exception.UndefinedTopicException;
import com.xman.message.filter.DispatchFilter;
import com.xman.message.handler.AbstractMessageHandler;
import com.xman.message.reflect.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author yangxiang@
 * @Date 2015/11/29
 * @Time 22:29
 */
public class MessageListener implements EventListener {
    private static final Logger ILOG = LoggerFactory.getLogger(MessageListener.class);

    private AbstractMessageHandler messageHandler;
    protected ConcurrentHashMap<String, List<Invocation>> mappings;
    private Codec codec;
    private DispatchFilter filter;
    ExecutorService executor;
    /* 核心数 默认为处理核数 */
    private int corePoolSize = Runtime.getRuntime().availableProcessors();
    /* 最大线程数 默认为coreSize的2倍 */
    private int maxPoolSize;
    /* 线程空闲超时[毫秒] 默认3000毫秒 */
    private long idlTimeout = 3000;
    /* 等待队列大小 默认100*/
    private int workQueueSize = 100;

    // 线程池工厂类
    final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("[MGD] Worker-%d")
            .setDaemon(false)
            .build();

    public MessageListener(Integer corePoolSize, Integer maxPoolSize, Integer workQueueSize, Long idlTimeout) {
        if (corePoolSize != null && corePoolSize > 0) {
            this.corePoolSize = corePoolSize;
        }
        if (maxPoolSize != null && maxPoolSize > 0) {
            this.maxPoolSize = maxPoolSize;
        }
        if (workQueueSize != null && workQueueSize > 0) {
            this.workQueueSize = workQueueSize;
        }
        if (idlTimeout != null && idlTimeout > 0) {
            this.idlTimeout = idlTimeout;
        }
        if (maxPoolSize <= 0) {
            this.maxPoolSize = corePoolSize * 2;
        }
        // 需要创建单线程线程池
        if (corePoolSize == 1 && maxPoolSize == 1) {
            executor = Executors.newSingleThreadExecutor(threadFactory);
        } else {
            executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, idlTimeout, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<Runnable>(workQueueSize),
                    threadFactory,
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }

    @Override
    public void receiveMessage(final String topic, final byte[] data) {
        if (StringUtils.isEmpty(topic)) {
            throw new UndefinedTopicException("Handler接收的topic为空");
        }
        if (data.length < 1) {
            throw new MessageEmptyException("Handler接收的的消息内容为空");
        }

        try {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Map<String, Object> requestParam = null;
                        List<Invocation> invocations = mappings.get(topic);
                        if (invocations == null) {
                            return;
                        }
                        // 反序列化
                        if (codec == null) {
                            codec = new FastJsonCodec();
                        }
                        boolean convertFail = false;
                        Object obj = null;
                        for (Invocation invocation: invocations) {
                            if (!StringUtils.isEmpty(invocation.getCodec()) &&
                                    invocation.getCodec().equals(CodecCode.CODEC_JSON.getComment())) {
                                ByteBuffer buffer = ByteBuffer.wrap(data);
                                obj = codec.decode(buffer);
                            } else {
                                obj = new JSONObject();
                                JSONObject jsonObject = (JSONObject) obj;
                                if (invocation.getParameterNames() != null && invocation.getParameterNames().length > 0) {
                                    jsonObject.put(invocation.getParameterNames()[0], new String(data));
                                } else {
                                    // default params.
                                    jsonObject.put("data", new String(data));
                                }
                            }
                            break;
                        }

                        try {
                            if (obj instanceof JSONObject) {
                                requestParam = (JSONObject) obj;
                            } else {
                                requestParam = BeanHelper.convertBeanToMap(obj);
                            }
                        } catch (Exception e) {
                            convertFail = true;
                            ILOG.warn("反序列化对象转Map失败" + e.getCause());
                        }

                        // 触发消息事件
                        if (!convertFail) {
                            MessageEvent event = new MessageEvent(requestParam);
                            event.setTopic(topic);
                            fireEvent(event);
                        }
                    } catch (Exception e) {
                        ILOG.warn("receiveMessage.submit.run.err=" + e.getCause());
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            // 拒绝的消息 丢回消息队列
            JSONObject jsonObject = JSON.parseObject(new String(data));
            messageHandler.doPublish(topic, jsonObject);
        } catch (Exception e ) {
            // 异常时,消息放回消息队列
            JSONObject jsonObject = JSON.parseObject(new String(data));
            messageHandler.doPublish(topic, jsonObject);
        }
    }

    @Override
    public void fireEvent(MessageEvent event) {
        String topic = event.getTopic();
        if (this.mappings == null) {
            throw new MessageDrivenExcpetiion(ExceptionCode.MessageMappingException,
                    "监听器listener未指定映射mapping");
        }
        List<Invocation> invocations = this.mappings.get(topic);
        if (invocations == null) {
            ILOG.info(topic + "没有订阅者，但却正在被监听");
            return;
        }

        if (filter == null) {
            filter = new DispatchFilter();
        }
        filter.setMessageHandler(this.messageHandler);
        // 触发过滤器,执行分发
        filter.doFilter(invocations, event);
    }

    @Override
    public void setMessageHandler(AbstractMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setMappings(ConcurrentHashMap<String, List<Invocation>> mappings) {
        this.mappings = mappings;
    }

    public void setFilter(DispatchFilter filter) {
        this.filter = filter;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setIdlTimeout(long idlTimeout) {
        this.idlTimeout = idlTimeout;
    }

    public void setWorkQueueSize(int workQueueSize) {
        this.workQueueSize = workQueueSize;
    }
}
