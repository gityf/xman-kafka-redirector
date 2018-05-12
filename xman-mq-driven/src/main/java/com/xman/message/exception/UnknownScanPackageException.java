package com.xman.message.exception;

/**
 * Created by yx on 2015/9/18.
 */
public class UnknownScanPackageException extends MessageDrivenExcpetiion {

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
