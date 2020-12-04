package com.zll.ratelimit.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 限流注解用了spring 的AliaisFor 必须通过AnnotationUtils获取，才会生效
 * {@link AliasFor}
 * {@link org.springframework.core.annotation.AnnotationUtils}
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    int DEFAULT_REQ_TIMES = 10;


    /**
     * 最大请求数
     *
     * @return
     */
    @AliasFor("max")
    long value() default DEFAULT_REQ_TIMES;

    /**
     * 最大的请求次数
     *
     * @return
     */
    @AliasFor("value")
    long max() default DEFAULT_REQ_TIMES;


    /**
     * 限流key
     *
     * @return
     */
    String key() default "";

    /**
     * 超时时长默认是1 分钟
     *
     * @return
     */
    long timeout() default 1;

    /**
     * 超时时间单位，默认是分钟
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;
}
