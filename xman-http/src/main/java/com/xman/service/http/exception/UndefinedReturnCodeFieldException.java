package com.xman.service.http.exception;

/**
 * Created by Administrator on 2015/9/18.
 */
public class UndefinedReturnCodeFieldException extends ServiceHttpException {

    public UndefinedReturnCodeFieldException(int exceptionCode, String exceptionMessage){
        super(exceptionCode, exceptionMessage);
    }

    public UndefinedReturnCodeFieldException(String exceptionMessage) {
        super(ExceptionCode.UndefinedReturnCodeField, exceptionMessage);
    }

    @Override
    public String toString() {
        return "UndefinedReturnCodeFieldException { " +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
