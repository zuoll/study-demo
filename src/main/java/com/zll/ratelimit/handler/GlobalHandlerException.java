package com.zll.ratelimit.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截，统一的返回格式
 */
@Slf4j
@RestControllerAdvice
@Order(-1)
public class GlobalHandlerException {

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Dict handler(RuntimeException ex) {
        System.out.println("GlobalHandlerException.handler");
        log.error("ex={}", ExceptionUtil.getRootCauseMessage(ex));
        return Dict.create().set("msg", ex.getMessage());
    }


    /**
     * @param ex
     * @return
     */
    @ExceptionHandler({ArithmeticException.class})
    public Dict handler2(ArithmeticException ex) {
        System.out.println("GlobalHandlerException.handler2");
        log.error("ex={}", ExceptionUtil.getRootCauseMessage(ex));
        return Dict.create().set("msg", ex.getMessage());
    }


    /**
     * @param ex
     * @return
     */
    @ExceptionHandler({Exception.class})
    public Dict handler3(Exception ex) {
        System.out.println("GlobalHandlerException.handler3");
        log.error("ex={}", ExceptionUtil.getRootCauseMessage(ex));
        return Dict.create().set("msg", ex.getMessage());
    }
}
