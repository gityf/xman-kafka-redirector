package com.xman.message.handler.json;

import com.xman.message.handler.AbstractMessageHandler;
import com.xman.message.reflect.Invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 16:31
 */
public class DefaultMessageHandler extends AbstractMessageHandler {
    private static final Logger ILOG = Logger.getLogger(DefaultMessageHandler.class.getName());

    @Override
    public Object doHandle(Invocation invocation, Object[] args) {
        // 调用目标方法
        Method method = invocation.getMethod();
        Object result = null;
        try {
            result = method.invoke(invocation.getObj(), args);
        } catch (InvocationTargetException e) {
            ILOG.warning("调用目标方法:" + method.getName() +
                    "失败。错误原因:" + e.getTargetException().getMessage());
        } catch (IllegalArgumentException e) {
            ILOG.warning("调用方法:" + method.getName() + "参数异常。异常原因:" + e.getMessage());
        } catch (IllegalAccessException e) {
            ILOG.warning("调用方法访问异常。异常原因" + e.getMessage());
        }

        return result;
    }

}
