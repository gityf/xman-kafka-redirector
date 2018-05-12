package com.xman.message.exception;

/**
 * Created by yx on 2015/9/18.
 */
public class ProduceException extends MessageDrivenExcpetiion {

    public ProduceException(int exceptionCode, String exceptionMessage){
        super(exceptionCode, exceptionMessage);
    }

    public ProduceException(String exceptionMessage) {
        super(ExceptionCode.ProduceFail, exceptionMessage);
    }

    @Override
    public String toString() {
        return "ProduceException{" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
