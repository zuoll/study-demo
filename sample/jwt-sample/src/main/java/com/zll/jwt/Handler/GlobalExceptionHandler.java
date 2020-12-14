package com.zll.jwt.Handler;

import com.zll.jwt.exception.BizException;
import com.zll.jwt.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局的统一异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 统一的异常的json 返回
     */
    @ResponseBody
    @ExceptionHandler({BizException.class})
    public ApiResponse jsonErrorHandler(BizException ex) {
        log.error("【JsonException】:{}", ex.getErrmsg());
        return ApiResponse.ofException(ex);
    }

}
