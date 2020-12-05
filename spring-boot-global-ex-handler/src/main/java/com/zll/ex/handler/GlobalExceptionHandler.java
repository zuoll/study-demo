package com.zll.ex.handler;

import com.zll.ex.exception.JsonException;
import com.zll.ex.exception.PageException;
import com.zll.ex.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * 全局的统一异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_PAGE = "error";

    /**
     * 统一的异常的json 返回
     */
    @ResponseBody
    @ExceptionHandler({JsonException.class})
    public ApiResponse jsonErrorHandler(JsonException ex) {
        log.error("【JsonException】:{}", ex.getErrMsg());
        return ApiResponse.ofException(ex);
    }


    /**
     * 统一的异常的页面返回error.html
     */
    @ExceptionHandler(PageException.class)
    public ModelAndView pageErrorException(PageException ex) {
        log.error("【PageException】:{}", ex.getErrMsg());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ERROR_PAGE);
        modelAndView.addObject("message", ex.getErrMsg());
        return modelAndView;
    }
}
