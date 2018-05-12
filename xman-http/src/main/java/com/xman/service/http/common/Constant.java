package com.xman.service.http.common;

/**
 * Created by yx on 2015/9/23.
 */
public class Constant {
    /* 返回类型错误码属性 */
    public static final String FIELD_RETURN_CODE = "errorCode";
    /* 返回类型消息格式类型[thrift对象/其他类型，如基本类型Long/String] */
    public static final String FIELD_MSG_TYPE = "msgType";
    public static final String FIELD_MSG_DATA = "msgData";
    /* 返回类型错误码信息属性 */
    public static final String FIELD_ERROR_MSG = "errorMsg";

    public static final String HEAD_SIGN_KEY = "SignKey";

    /* 成功错误码 */
    public static final Integer SUCCESS_CODE = 0;
    /* 框架错误码 */
    public static final Integer LOW_FRAME_ERROR_CODE = 600;
    public static final Integer HIGH_FRAME_ERROR_CODE = 1000;

    /* 业务错误码 */
    public static final Integer LOW_BIZ_ERROR_CODE = 1001;
    public static final Integer HIGHT_BIZ_ERROR_CODE = 10000;

    public static final Boolean RETURN_OBJ_NEED_CODE_PROP = Boolean.FALSE;

}
