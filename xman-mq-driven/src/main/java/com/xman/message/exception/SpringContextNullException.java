package com.xman.message.exception;

/**
 * Created by yx on 2015/9/18.
 */
public class SpringContextNullException extends MessageDrivenExcpetiion {

    public SpringContextNullException(int exceptionCode, String exceptionMessage){
        super(exceptionCode, exceptionMessage);
    }

    public SpringContextNullException(String exceptionMessage) {
        super(ExceptionCode.SpringContextNull, exceptionMessage);
    }

    @Override
    public String toString() {
        return "SpringContextNullException{" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
