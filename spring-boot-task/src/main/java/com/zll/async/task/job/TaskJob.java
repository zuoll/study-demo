package com.zll.async.task.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class TaskJob {

    /**
     * cron 表达式的定时
     * 按照标准时间来算，每隔 10s 执行一次
     */
//    @Scheduled(cron = "0/10 * * * * ?")
    public void job1() {
        log.info("从启动时间开始，每隔10s执行一次={}", LocalDateTime.now());
    }

    /**
     * 固定频率的定时
     * 从启动时间开始，间隔 2s 执行
     */

//    @Scheduled(fixedRate = 2000)
    public void job2() {
        log.info("按照标准时间来算，每隔2s执行一次={}", LocalDateTime.now());

    }

    /**
     * 可以延时的定时
     * 从启动时间开始，延迟 3s 后间隔 5s 执行
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 3000)
    public void job3() {
        log.info("从启动时间开始，延迟3s后间隔5s执行={}", LocalDateTime.now());
    }
}
