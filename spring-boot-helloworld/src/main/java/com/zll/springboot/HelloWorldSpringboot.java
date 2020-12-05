package com.zll.springboot;

import cn.hutool.core.lang.Dict;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * spring boot 的启动类
 *
 * {@link SpringBootApplication}
 * {@link SpringApplication}
 */
@RestController
@SpringBootApplication
public class HelloWorldSpringboot {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldSpringboot.class, args);
    }


    /**
     *
     * @param name
     * @return
     */
    @RequestMapping("/hello")
    public Dict hello(@RequestParam(required = false, defaultValue = "zll") String name) {
        return Dict.create().set("msg", "hello," + name);
    }
}
