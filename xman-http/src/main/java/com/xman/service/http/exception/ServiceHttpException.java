package com.xman.service.http.exception;

public class ServiceHttpException extends RuntimeException {

    protected int exceptionCode;
    protected String exceptionMessage;

    public ServiceHttpException(int exceptionCode, String exceptionMessage) {
        this.exceptionCode = exceptionCode;
        this.exceptionMessage = exceptionMessage;
    }

    public ServiceHttpException(ExceptionCode exceptionCode, String exceptionMessage) {
        this.exceptionCode = exceptionCode.getCode();
        this.exceptionMessage = exceptionMessage;
    }

    public ServiceHttpException() {}

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
        return "ServiceHttpException{" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
