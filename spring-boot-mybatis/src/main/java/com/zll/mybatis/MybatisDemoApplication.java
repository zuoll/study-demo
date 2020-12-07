package com.zll.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//com.zll.mybatis.mapper.** 包括子包
@MapperScan(basePackages = "com.zll.mybatis.mapper.**")
@SpringBootApplication
public class MybatisDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisDemoApplication.class, args);
    }
}
