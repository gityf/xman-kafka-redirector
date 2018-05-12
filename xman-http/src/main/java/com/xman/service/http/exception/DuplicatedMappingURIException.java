package com.xman.service.http.exception;

/**
 * Created by Administrator on 2015/9/18.
 */
public class DuplicatedMappingURIException extends ServiceHttpException {

    public DuplicatedMappingURIException(int exceptionCode, String exceptionMessage){
        super(exceptionCode, exceptionMessage);
    }

    public DuplicatedMappingURIException(String exceptionMessage) {
        super(ExceptionCode.DuplicatedMappingURI, exceptionMessage);
    }

    @Override
    public String toString() {
        return "DuplicatedMappingURIException {" +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
