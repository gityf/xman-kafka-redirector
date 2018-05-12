package com.xman.service.http.common;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2015/9/17.
 */
public class Invocation {

    private Object obj;

    private Method method;

    private Class<?>[] parameterTypes;

    private Type[] genericParameterTypes;

    private String[] parameterNames;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(String[] parameterNames) {
        this.parameterNames = parameterNames;
    }


    public Type[] getGenericParameterTypes() {
        return genericParameterTypes;
    }

    public void setGenericParameterTypes(Type[] genericParameterTypes) {
        this.genericParameterTypes = genericParameterTypes;
    }
}
