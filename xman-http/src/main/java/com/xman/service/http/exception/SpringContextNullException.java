package com.xman.service.http.exception;

/**
 * Created by Administrator on 2015/9/18.
 */
public class SpringContextNullException extends ServiceHttpException {

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
