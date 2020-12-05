package com.zll.task;

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
