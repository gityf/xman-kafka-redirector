package com.xman.service.http.reflect;

import com.xman.service.http.common.Invocation;
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

        // paramNames即参数名
        LocalVariableTableParameterNameDiscoverer variableDiscover = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = variableDiscover.getParameterNames(method);

        invocation.setParameterNames(paramNames);
        invocation.setParameterTypes(parameterTypes);
        invocation.setGenericParameterTypes(types);

        return invocation;
    }

}
