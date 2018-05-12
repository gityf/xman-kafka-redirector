package com.xman.message.exception;

/**
 * Created by yx on 2015/9/18.
 */
public class MessageEmptyException extends MessageDrivenExcpetiion {

    public MessageEmptyException(int exceptionCode, String exceptionMessage){
        super(exceptionCode, exceptionMessage);
    }

    public MessageEmptyException(String exceptionMessage) {
        super(ExceptionCode.MessageEmpty, exceptionMessage);
    }

    @Override
    public String toString() {
        return "UndefinedTopicException{" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
