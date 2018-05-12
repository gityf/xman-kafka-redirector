package com.xman.message.reflect;

import com.xman.message.annotation.RegisterMQ;
import com.xman.message.common.ConditionRelation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author yangxiang@
 * @Date 2015/12/4
 * @Time 14:29
 */
public class ReflectionWithSpringUtil {

    public static Invocation getInvocation(Object obj, Method method) throws Exception {
        Invocation invocation = new Invocation();
        invocation.setObj(obj);
        invocation.setMethod(method);

        Type[] types = method.getGenericParameterTypes();
        Class[] parameterTypes = method.getParameterTypes();
        RegisterMQ registerMQ = method.getAnnotation(RegisterMQ.class);
        String topic = registerMQ.topic();
        String to = registerMQ.to();
        String codec = registerMQ.codec();
        String[] keys = registerMQ.keys();
        String[] values = registerMQ.values();
        ConditionRelation cr = registerMQ.cr();

        // paramNames即参数名
        LocalVariableTableParameterNameDiscoverer variableDiscover = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = variableDiscover.getParameterNames(method);

        invocation.setParameterNames(paramNames);
        invocation.setParameterTypes(parameterTypes);
        invocation.setGenericParameterTypes(types);
        invocation.setTo(to);
        invocation.setCodec(codec);
        invocation.setTopic(topic);
        invocation.setKeys(keys);
        invocation.setValues(values);
        invocation.setCr(cr);

        return invocation;
    }

}
