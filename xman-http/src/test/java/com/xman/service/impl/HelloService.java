package com.xman.service.impl;

import com.xman.service.IHelloSerice;
import com.xman.service.http.annotation.HttpService;
import com.xman.service.http.annotation.HttpServiceMethod;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/9/22.
 */
@HttpService
@Service
public class HelloService implements IHelloSerice {

    @HttpServiceMethod(URI = "/testRet")
    @Override
    public ResponseMsg testRetMsg() {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setErrorCode(1);
        responseMsg.setErrorMsg("OK");
        responseMsg.setMsg("Hello World");
        return responseMsg;
    }

    @HttpServiceMethod(URI = "/testHelloMsg")
    @Override
    public HelloMessage testHelloMsg() {
        HelloMessage helloMessage = new HelloMessage();
        helloMessage.setErrorCode(1);
        helloMessage.setErrorMsg("XX");
        helloMessage.setMsgBody("BODY");
        helloMessage.setMsgName("sss");
        helloMessage.setValidFlag(true);
        return helloMessage;
    }

    @HttpServiceMethod(URI = "/test01")
    @Override
    public ReturnMsg test01(String appId, String token, Request request) {
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setErrorCode(0);
        returnMsg.setErrorMsg("OK");
        returnMsg.setData(request.getUsername() + ":" + request.getPassword());
        return returnMsg;
    }

    @HttpServiceMethod(URI = "/test001")
    public OReturnMsg test001(String appId, String token) throws TException {
        OReturnMsg returnMsg = new OReturnMsg();
        try {
            returnMsg.setErrorCode(0);
            returnMsg.setErrorMsg("OK");
            returnMsg.setMsgType("other");
            returnMsg.setMsgData("String return data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMsg;
    }

    @HttpServiceMethod(URI = "/test")
    public ReturnMsg test(String order_id, String partner_id,
                          String product_line, String total_amount, String sign, String sinType,
                          String noise) throws TException{
        ReturnMsg returnMsg = new ReturnMsg();
        try {
            System.out.println("HelloWorld");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMsg;
    }

    @HttpServiceMethod(URI = "hi")
    public String sayHello(int sss) {
        return "hi "+ sss;
    }
}
