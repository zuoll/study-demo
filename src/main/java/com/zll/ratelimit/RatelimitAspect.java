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
