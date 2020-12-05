package com.zll.ex;

import com.zll.ex.constants.Status;
import com.zll.ex.exception.JsonException;
import com.zll.ex.exception.PageException;
import com.zll.ex.model.ApiResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

/**
 * spring boot 的统一异常处理启动类
 */
@SpringBootApplication
@Controller
public class GlobalExApplication {
    public static void main(String[] args) {
        SpringApplication.run(GlobalExApplication.class, args);
    }


    /**
     * 返回一个统一json 错误
     *
     * @return
     */
    @RequestMapping("/json")
    @ResponseBody
    public ApiResponse testJson() {
        throw new JsonException(Status.UNKNOWN_ERROR);
    }


    /**
     * 返回一个正常的json数据
     *
     * @return
     */
    @RequestMapping("/json-ok")
    @ResponseBody
    public ApiResponse testJsonOK() {
        return ApiResponse.ofSuccess("ok");
    }


    /**
     * 返回一个统一错误页面 error.html
     *
     * @return
     */
    @RequestMapping("/page")
    public ModelAndView testPage() {
        throw new PageException(510, "参数错误");
    }


    /**
     * 返回一个正常的hello.html页面
     *
     * @return
     */
    @RequestMapping("/hello")
    public ModelAndView testHello() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("data", LocalDateTime.now());
        //相当于访问 /templates/hello.html
        modelAndView.setViewName("hello");
        return modelAndView;
    }
}
