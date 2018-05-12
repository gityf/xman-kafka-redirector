package com.xman.service.http.common;

/**
 * Created by Administrator on 2015/9/23.
 */
public class SignThreadLocal {
    public static final ThreadLocal<String> sign = new ThreadLocal<String>();

    public static String getSign() {
        return sign.get();
    }

    public static void setSign(String signStr) {
        sign.set(signStr);
    }
}
