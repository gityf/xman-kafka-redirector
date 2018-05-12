package com.xman.service.http.common;

/**
 * Created by wangyaofu on 2017/6/9.
 */
public class ParamBasic {
    private String              name;
    private Class<?>            returnType;
    private Class<?>[]          parameterTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
