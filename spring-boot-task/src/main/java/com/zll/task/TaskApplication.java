package com.zll.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


//2020-12-05 17:27:32.248  INFO 24996 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService 'taskScheduler'
/**
 * @see org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
 * 定时任务demo 启动类
 */
@SpringBootApplication
@EnableScheduling//启动定时器
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}
