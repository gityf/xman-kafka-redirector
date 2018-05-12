package com.xman.service.http.reflect;

import com.xman.service.http.common.Invocation;
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
 * Created by Administrator on 2015/9/17.
 */
public class ReflectionUtil {

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
        if (attr == null) {
            // exception
        }
        if (cm.getMethodInfo().getAccessFlags() != AccessFlag.PUBLIC) {
            return null;
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

        return invocation;
    }

}
