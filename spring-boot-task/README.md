#### spring boot 下的定时任务的配置方式

##### pom.xml
```xml
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
    </dependency>

```


#### `yml`和`java` 两种配置方式
```yml

#TaskSchedulingProperties.java
spring:
  task:
    scheduling:
      pool:
        size: 10
      thread-name-prefix: task-demo-

```

#### `java 的配置方式`
```java
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

```

#### 几种常见的定器
```java
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

```

#### 结果测试
```java
package com.zll.async.task;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskApplication.class)
public class TaskApplicationTest {


    @Before
    public void init() {
        log.info("启动时间={}", LocalDateTime.now());
    }


    @Test
    public void test001() throws Exception {
        //阻塞
        System.in.read();
    }
}

```

#### 运行结果展示
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/20201205172427.png)