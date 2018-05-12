package com.xman.message.annotation;

import com.xman.message.common.ConditionRelation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangxiang@
 * @Date 2015/11/1
 * @Time 16:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RegisterMQ {

    /**
     * 订阅的消息队列topic
     *
     * @return
     */
    String topic();

    /**
     * 返回结果输出到的队列topic，如果为空，则直接输出到同一个topic
     *
     * @return
     */
    String to() default "";

    /**
     * 默认使用的编码方式,支持 JSON, RAW
     */
    String codec() default "JSON";

    String[] keys() default {};

    String[] values() default {};

    /**
     * 各key筛选条件之间的组合关系 默认 and
     *
     * @return
     */
    ConditionRelation cr() default ConditionRelation.AND;
}
