package com.xman.message.exception;

/**
 * Created by yx on 2015/9/18.
 */
public class SubscribeException extends MessageDrivenExcpetiion {

    public SubscribeException(int exceptionCode, String exceptionMessage){
        super(exceptionCode, exceptionMessage);
    }

    public SubscribeException(String exceptionMessage) {
        super(ExceptionCode.SubscribeFail, exceptionMessage);
    }

    @Override
    public String toString() {
        return "SubscribeException{" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
