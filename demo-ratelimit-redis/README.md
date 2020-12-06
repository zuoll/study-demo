### 使用redis+lua 脚本实现限流,旨在保护API 被恶意的频繁访问
#### 打包，进入到项目根目录下，和pom文件同一级别
```sh
mvn clean package -DskipTests

```

#### pom
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zll.study</groupId>
    <artifactId>ratelimit-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <description>redis + lua 限流 demo</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.5.RELEASE</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- 对象池，使用redis时必须引入 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>

        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.1.2</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <finalName>${project.name}</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

#### 注解
```java

package com.zll.ratelimit.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 限流注解用了spring 的AliaisFor 必须通过AnnotationUtils获取，才会生效
 * {@link AliasFor}
 * {@link org.springframework.core.annotation.AnnotationUtils}
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    int DEFAULT_REQ_TIMES = 10;


    /**
     * 最大请求数
     *
     * @return
     */
    @AliasFor("max")
    long value() default DEFAULT_REQ_TIMES;

    /**
     * 最大的请求次数
     *
     * @return
     */
    @AliasFor("value")
    long max() default DEFAULT_REQ_TIMES;


    /**
     * 限流key
     *
     * @return
     */
    String key() default "";

    /**
     * 超时时长默认是1 分钟
     *
     * @return
     */
    long timeout() default 1;

    /**
     * 超时时间单位，默认是分钟
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;
}


```


####
```java
package com.zll.ratelimit;

import cn.hutool.core.util.StrUtil;
import com.zll.ratelimit.annotation.RateLimiter;
import com.zll.ratelimit.utils.IpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatelimitAspect {


    private static final String REDIS_LIMIT_KEY_PREFIX = "limit:";

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisScript<Long> redisScript;


    @Pointcut("@annotation(com.zll.ratelimit.annotation.RateLimiter)")
    public void ratelimit() {
    }


    @Around("ratelimit()")
    public Object pointCut(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        if (rateLimiter != null) {

            String key = rateLimiter.key();
            if (StrUtil.isBlank(key)) {
                //默认用类+方法名的前缀作为key
                key = method.getDeclaringClass().getName() + StrUtil.DOT + method.getName();
                log.debug("key={}", key);
            }

            //最终限流的key 为 前缀+ip 地址
            //TODO 局域网要考虑多用户访问的情况 ，加上参数更加合理

            key += ":" + IpUtils.getIpAddr();

            log.debug("key={}", key);

            long max = rateLimiter.max();
            long timeout = rateLimiter.timeout();
            TimeUnit timeUnit = rateLimiter.timeUnit();

            boolean checkLimit = this.checkLimit(key, max, timeout, timeUnit);

            if (checkLimit) {
                throw new RuntimeException("<<<手速太快了，请慢点访问，谢谢>>>");
            }
        }

        // do biz
        return joinPoint.proceed();
    }


    private boolean checkLimit(String key, long max, long timeout, TimeUnit timeUnit) {

        key = REDIS_LIMIT_KEY_PREFIX + key;

        //时间单位统一使用毫秒
        long ttl = timeUnit.toMillis(timeout);

        //当前的时间毫秒数
        long now = Instant.now().toEpochMilli();

        long expired = now - ttl;

        Long execute = stringRedisTemplate.execute(redisScript, Collections.singletonList(key), now + "", ttl + "", expired + "", max + "");

        if (execute != null) {
            if (execute == 0) {
                log.error("【{}】 在单位时间{} 毫秒内达到访问上限，当前的接口的上限是{}", key, ttl, max);
                return true;
            } else {
                log.info("【{}】在单位时间 {} 毫秒内访问{}次", key, ttl, execute);
                return false;
            }
        }
        return false;
    }

}

```

#### 运行结果

![Image text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/20201205100717.png)


