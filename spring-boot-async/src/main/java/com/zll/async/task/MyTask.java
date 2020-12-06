package com.zll.async.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
public class MyTask {

    private void doTask(String taskName, Integer time) {
        System.out.println(LocalDateTime.now() + "," + taskName + "开始执行"+time+"s, 当前的线程的名称是" + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LocalDateTime.now() + "," + taskName + "执行成功, 当前的线程的名称是" + Thread.currentThread().getName());
    }

    /**
     * 一个执行3s的异步任务
     *
     * @return
     */
    @Async //标注为一个线程池启动的异步任务
    public Future<Boolean> asyncTask1() {
        this.doTask("asyncTask1", 3);
        return new AsyncResult<>(Boolean.TRUE);
    }

    /**
     * 一个执行5s的异步任务
     *
     * @return
     */
    @Async//标注为一个线程池启动的异步任务
    public Future<Boolean> asyncTask2() {
        this.doTask("asyncTask2", 5);
        return new AsyncResult<>(Boolean.TRUE);
    }


    /**
     * 一个执行3s的同步任务
     *
     * @return
     */
    public void task1() {
        this.doTask("task1", 3);
    }

    /**
     * 一个执行5s的同步任务
     *
     * @return
     */
    public void task2() {
        this.doTask("task2", 5);
    }
}
