package com.xman.message.exception;

/**
 * Created by yx on 2015/9/18.
 */
public class UndefinedTopicException extends MessageDrivenExcpetiion {

    public UndefinedTopicException(int exceptionCode, String exceptionMessage){
        super(exceptionCode, exceptionMessage);
    }

    public UndefinedTopicException(String exceptionMessage) {
        super(ExceptionCode.UndefinedTopic, exceptionMessage);
    }

    @Override
    public String toString() {
        return "UndefinedTopicException{" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
