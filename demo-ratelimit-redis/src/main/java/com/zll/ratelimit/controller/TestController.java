package com.zll.ratelimit.controller;

import cn.hutool.core.lang.Dict;
import com.zll.ratelimit.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class TestController {


    @RateLimiter(value = 5)
    @GetMapping("/test1")
    public Dict test1() {
        log.info("test1 被执行了。。。。");
        return Dict.create()
                .set("msg", "hello world test1")
                .set("description", "别老想看到我， 不行你加快手速访问试试~~~")
                .set("date", LocalDateTime.now());
    }


    @RateLimiter(value = 2, key = "test2")
    @GetMapping("/test2")
    public Dict test2() {
        log.info("test2 被执行了。。。。");
        return Dict.create()
                .set("msg", "hello world test2")
                .set("description", "别老想看到我， 不行你加快手速访问试试~~~")
                .set("date", LocalDateTime.now());
    }


    @GetMapping("/test3")
    public Dict test3() {
        log.info("test3 被执行了。。。。");
        return Dict.create()
                .set("msg", "hello world test3")
                .set("date", LocalDateTime.now());
    }


    @GetMapping("/test_global_ex")
    public Dict test_global_ex() {
        log.info("test_global_ex 被执行了。。。。");
        int a = 1 / 0;
        return Dict.create()
                .set("msg", "hello world test_global_ex")
                .set("date", LocalDateTime.now());
    }
}
