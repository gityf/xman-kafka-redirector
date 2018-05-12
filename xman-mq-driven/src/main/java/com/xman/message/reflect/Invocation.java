package com.xman.message.reflect;

import com.xman.message.common.ConditionRelation;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by yx on 2015/9/17.
 */
public class Invocation {

    private Object obj;

    private Method method;

    private Class<?>[] parameterTypes;

    private Type[] genericParameterTypes;

    private String[] parameterNames;

    private String topic;
    private String to;
    private String codec;
    private String[] keys;
    private String[] values;
    private ConditionRelation cr;

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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public ConditionRelation getCr() {
        return cr;
    }

    public void setCr(ConditionRelation cr) {
        this.cr = cr;
    }
}
