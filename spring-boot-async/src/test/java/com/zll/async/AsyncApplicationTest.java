package com.zll.async;

import com.zll.async.task.MyTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AsyncApplication.class)
public class AsyncApplicationTest {

    @Autowired
    private MyTask myTask;

    /**
     * 测试异步任务耗时
     */
    @Test
    public void testAsync() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        //并发执行
        Future<Boolean> future1 = myTask.asyncTask1();
        Future<Boolean> future2 = myTask.asyncTask2();

        //阻塞直到每个任务完成
        Boolean b1 = future1.get();
        Boolean b2 = future2.get();
        System.out.println("result1="+b1+",result2="+b2);
        long end = System.currentTimeMillis();

        //并发执行总的耗时是时间执行最长的那个
        System.out.println("cost time = "+(end -start)/1000+"s");
    }


    /**
     * 测试同步任务耗时
     */
    @Test
    public void testSync() {
        long start = System.currentTimeMillis();
        myTask.task1();
        myTask.task2();
        long end = System.currentTimeMillis();
        //同步执行，总的耗时是每个任务执行时间的和
        System.out.println("cost time = "+(end -start)/1000+"s");
    }
}
