package com.xman.service.http.exception;

/**
 * Created by Administrator on 2015/9/18.
 */
public class UnknownScanPackageException extends ServiceHttpException {

    public UnknownScanPackageException(int exceptionCode, String exceptionMessage){
        super(exceptionCode, exceptionMessage);
    }

    public UnknownScanPackageException(String exceptionMessage) {
        super(ExceptionCode.UnknownScanPackages, exceptionMessage);
    }

    @Override
    public String toString() {
        return "UnknownScanPackageException { " +
                "exceptionCode=" + exceptionCode +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
