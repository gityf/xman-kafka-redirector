package com.xman.message.notify;

import com.xman.message.annotation.MQHanlder;
import com.xman.message.annotation.RegisterMQ;
import com.xman.message.exception.UndefinedTopicException;
import com.xman.message.reflect.Invocation;
import com.xman.message.reflect.ReflectionWithSpringUtil;
import com.xman.message.reflect.scan.LoadPackageClasses;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 17:05
 */
public class NotifyMapping {
    // 支持多包扫描
    private String[] scanPackages;
    private ApplicationContext context;

    public NotifyMapping(String[] scanPackages, ApplicationContext context) {
        this.scanPackages = scanPackages;
        this.context = context;
    }

    /**
     * 获取订阅配置表
     *
     * @return
     * @throws Exception
     */
    public ConcurrentHashMap<String, List<Invocation>> getMappings() throws Exception {
        ConcurrentHashMap<String, List<Invocation>> mapping = new ConcurrentHashMap<>();

        LoadPackageClasses loadPackageClasses = new LoadPackageClasses(scanPackages, MQHanlder.class);

        Set<Method> methods = loadPackageClasses.getMethodSet();
        for (Method method : methods) {
            Object obj = context.getBean(method.getDeclaringClass());
            Invocation invocation = ReflectionWithSpringUtil.getInvocation(obj, method);
            RegisterMQ annotation = method.getAnnotation(RegisterMQ.class);
            if (annotation != null) {
                String topic = annotation.topic();
                if (StringUtils.isEmpty(topic)) {
                    throw new UndefinedTopicException("方法:" + method.getName() + "未定义订阅的topic");
                }
                if (mapping.containsKey(topic) && mapping.get(topic) != null) {
                    mapping.get(topic).add(invocation);
                } else {
                    List<Invocation> invocations = new ArrayList<>();
                    invocations.add(invocation);
                    mapping.put(topic, invocations);
                }
            }
        }

        return mapping;
    }

    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
