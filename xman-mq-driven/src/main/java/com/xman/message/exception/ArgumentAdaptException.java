package com.xman.message.exception;

/**
 * Created by yx on 2015/9/18.
 */
public class ArgumentAdaptException extends MessageDrivenExcpetiion {

    public ArgumentAdaptException(int exceptionCode, String exceptionMessage) {
        super(exceptionCode, exceptionMessage);
    }

    public ArgumentAdaptException(String exceptionMessage) {
        super(ExceptionCode.ArgumentsAdaptError, exceptionMessage);
    }

    @Override
    public String toString() {
        return "ArgumentAdaptException{" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
