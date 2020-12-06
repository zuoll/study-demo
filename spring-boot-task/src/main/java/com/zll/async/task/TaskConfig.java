package com.zll.async.task;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 定时任务使用线程池，运行，可以提升效率
 */

@EnableScheduling
@Configuration
@ComponentScan(basePackages = "com.zll.async.task.job")
public class TaskConfig implements SchedulingConfigurer {


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }


    /**
     * {@link TaskSchedulingProperties}
     * {@link BasicThreadFactory}
     * 这个也可以在配置文件中配置
     * 等同于
     * <pre>
     *     #TaskSchedulingProperties.java
     * spring:
     *   task:
     *     scheduling:
     *       pool:
     *         size: 10
     *       thread-name-prefix: demo-task-
     * </pre>
     * 代码配置就相当于配置参数写死了
     *
     * @return
     */
    @Bean
    public Executor taskExecutor() {
        return new ScheduledThreadPoolExecutor(5,
                new BasicThreadFactory.Builder().namingPattern("demo-task-%d").build());
    }
}
