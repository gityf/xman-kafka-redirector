package com.xman.message.filter;

import com.xman.message.listener.MessageEvent;
import com.xman.message.reflect.Invocation;

import java.util.List;

/**
 * @author yangxiang@
 * @Date 2015/11/29
 * @Time 13:32
 */
public interface Filter {

    void doFilter(List<Invocation> invocations, MessageEvent event);

}
