package com.xman.message.exception;

/**
 * Created by Administrator on 2015/9/18.
 */
public enum ExceptionCode {
    // 6开头为客户端设置错误
    UnknownScanPackages(601, "未指定扫描的包名"),
    SpringContextNull(602, "spring上下文为空"),
    UndefinedTopic(603, "未定义订阅消息的topic"),
    MessageEmpty(604, "空消息"),
    SubscribeFail(605, "订阅失败"),
    ProduceFail(606, "生成消息失败"),
    KafkaConfigError(607, "kafka配置错误"),
    SPILoadFail(608, "SPI加载Service失败"),
    KeyValueNotMatch(609, "注解配置keys与values不匹配"),
    ArgumentsAdaptError(610, "参数适配失败"),
    MessageServiceException(611, "消息模块错误"),
    MessageDispatchException(612, "消息事件分发错误"),
    MessageMappingException(613, "消息映射异常"),
    ;


    private Integer code;
    private String comment;

    ExceptionCode(Integer code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public Integer getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }
}
