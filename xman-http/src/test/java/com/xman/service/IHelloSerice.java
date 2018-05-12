package com.xman.service;

import com.xman.service.impl.HelloMessage;
import com.xman.service.impl.Request;
import com.xman.service.impl.ResponseMsg;
import com.xman.service.impl.ReturnMsg;

/**
 * Created by Administrator on 2015/9/22.
 */
public interface IHelloSerice {

    String sayHello(int sss);

    ResponseMsg testRetMsg();

    HelloMessage testHelloMsg();

    ReturnMsg test01(String appId, String token, Request request);
}
