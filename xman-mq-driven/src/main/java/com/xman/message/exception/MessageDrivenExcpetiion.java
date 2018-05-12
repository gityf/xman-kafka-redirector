package com.xman.message.exception;

public class MessageDrivenExcpetiion extends RuntimeException {

    protected int exceptionCode;
    protected String exceptionMessage;

    public MessageDrivenExcpetiion(int exceptionCode, String exceptionMessage) {
        this.exceptionCode = exceptionCode;
        this.exceptionMessage = exceptionMessage;
    }

    public MessageDrivenExcpetiion(ExceptionCode exceptionCode, String exceptionMessage) {
        this.exceptionCode = exceptionCode.getCode();
        this.exceptionMessage = exceptionMessage;
    }

    public MessageDrivenExcpetiion() {}

    public int getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(int exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String toString() {
        return "MessageDrivenExcpetiion{" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
