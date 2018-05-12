package com.xman.message.reflect;

import com.xman.message.annotation.RegisterMQ;
import com.xman.message.common.ConditionRelation;
import com.xman.message.exception.ExceptionCode;
import com.xman.message.exception.MessageDrivenExcpetiion;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by yx on 2015/9/17.
 */
public class Reflection {

    public static Invocation getInvocation(Object obj, Class clazz, Method method) throws Exception {
        Invocation invocation = new Invocation();
        invocation.setObj(obj);
        invocation.setMethod(method);

        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get(clazz.getCanonicalName());

        String name = method.getName();
        CtMethod cm = ctClass.getDeclaredMethod(name);
        MethodInfo methodInfo = cm.getMethodInfo();
        Type[] types = method.getGenericParameterTypes();
        Class[] parameterTypes = method.getParameterTypes();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                .getAttribute(LocalVariableAttribute.tag);
        RegisterMQ registerMQ = method.getAnnotation(RegisterMQ.class);
        String topic = registerMQ.topic();
        String to = registerMQ.to();
        String[] keys = registerMQ.keys();
        String[] values = registerMQ.values();
        ConditionRelation cr = registerMQ.cr();

        if (attr == null) {
            // exception
        }
        if (cm.getMethodInfo().getAccessFlags() != AccessFlag.PUBLIC) {
            return null;
        }
        if (keys.length != values.length) {
            throw new MessageDrivenExcpetiion(ExceptionCode.KeyValueNotMatch, ExceptionCode.KeyValueNotMatch.getComment());
        }

        // paramNames即参数名
        String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int j = 0; j < paramNames.length; j++) {
            paramNames[j] = attr.variableName(j + pos);
        }

        invocation.setParameterNames(paramNames);
        invocation.setParameterTypes(parameterTypes);
        invocation.setGenericParameterTypes(types);
        invocation.setTo(to);
        invocation.setTopic(topic);
        invocation.setKeys(keys);
        invocation.setValues(values);
        invocation.setCr(cr);
        return invocation;
    }

}
