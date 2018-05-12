package com.xman.message.exception;

/**
 * Created by yx on 2015/9/18.
 */
public class SPILoadServiceException extends MessageDrivenExcpetiion {

    public SPILoadServiceException(int exceptionCode, String exceptionMessage){
        super(exceptionCode, exceptionMessage);
    }

    public SPILoadServiceException(String exceptionMessage) {
        super(ExceptionCode.SPILoadFail, exceptionMessage);
    }

    @Override
    public String toString() {
        return "SPILoadServiceException{" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
