package com.xman.message.filter;

import com.xman.message.common.ConditionRelation;
import com.xman.message.exception.ExceptionCode;
import com.xman.message.exception.MessageDrivenExcpetiion;
import com.xman.message.handler.AbstractMessageHandler;
import com.xman.message.listener.MessageEvent;
import com.xman.message.reflect.Invocation;

import java.util.List;
import java.util.Objects;

/**
 * @author yangxiang@
 * @Date 2015/11/29
 * @Time 13:28
 */
public class DispatchFilter implements Filter {

    AbstractMessageHandler messageHandler;

    @Override
    public void doFilter(List<Invocation> invocations, MessageEvent event) {
        if (messageHandler == null) {
            throw new MessageDrivenExcpetiion(ExceptionCode.MessageDispatchException,
                    "分发过滤器Filter未指定消息处理器Handler");
        }
        for (Invocation invocation : invocations) {
            Object[] args = null;
            // 条件匹配
            if (fitCondition(invocation, event)) {
                messageHandler.handle(invocation, event);
            }
        }
    }

    /**
     * 做条件筛选
     *
     * @param invocation
     * @param event
     * @return
     */
    public boolean fitCondition(Invocation invocation, MessageEvent event) {
        boolean match = true;
        String[] keys = invocation.getKeys();
        String[] values = invocation.getValues();
        ConditionRelation cr = invocation.getCr();
        if (keys == null || values == null) {
            return match;
        }

        if (ConditionRelation.OR.equals(cr)) {
            return doOrCr(keys, values, event);
        } else if (ConditionRelation.NOT.equals(cr)) {
            return doNotCr(keys, values, event);
        } else {
            return doAndCr(keys, values, event);
        }
    }

    /**
     * 与关系处理
     *
     * @param keys
     * @param values
     * @param event
     * @return
     */
    public boolean doAndCr(String[] keys, String[] values, MessageEvent event) {
        for (int i = 0; i < keys.length; i++) {
            if (!Objects.equals(values[i], "" + event.getAttribute(keys[i]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 或关系处理
     *
     * @param keys
     * @param values
     * @param event
     * @return
     */
    public boolean doOrCr(String[] keys, String[] values, MessageEvent event) {
        for (int i = 0; i < keys.length; i++) {
            if (Objects.equals(values[i], "" + event.getAttribute(keys[i]))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 非关系处理
     *
     * @param keys
     * @param values
     * @param event
     * @return
     */
    public boolean doNotCr(String[] keys, String[] values, MessageEvent event) {
        for (int i = 0; i < keys.length; i++) {
            if (Objects.equals(values[i], "" + event.getAttribute(keys[i]))) {
                return false;
            }
        }
        return true;
    }

    public void setMessageHandler(AbstractMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
}
