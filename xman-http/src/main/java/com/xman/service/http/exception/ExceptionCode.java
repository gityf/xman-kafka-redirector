package com.xman.service.http.exception;

/**
 * Created by Administrator on 2015/9/18.
 */
public enum ExceptionCode {
    // 6开头为客户端设置错误
    UnknownScanPackages(601, "未指定扫描的包名"),
    SpringContextNull(602, "spring上下文为空"),
    UndefinedReturnCodeField(603, "返回类型缺少必要属性：returnCode"),

    // 7开头为服务端错误
    DuplicatedMappingURI(701, "配置的URI有重复的"),

    // http
    UnsupportedContentType(801, "ContentType不支持"),;


    private Integer code;
    private String comment;

    ExceptionCode(Integer code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public Integer getCode() {
        return code;
    }
}
