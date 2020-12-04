package com.zll.retelimit;

import cn.hutool.core.lang.Dict;
import com.zll.ratelimit.RatelimitApplication;
import com.zll.ratelimit.controller.TestController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatelimitApplication.class)
public class RatelimitApplicationTest {


    @Autowired
    private TestController testController;

    @Before
    public void init() {
        System.out.println(testController);
    }

    @Test
    public void test001() {

        for (int i = 0; i < 10; i++) {
            Dict dict = testController.test1();
            System.out.println(dict);
        }
    }


    @Test
    public void test002() {

        for (int i = 0; i < 10; i++) {
            Dict dict = testController.test2();
            System.out.println(dict);
        }
    }


    @Test
    public void test003() {

        for (int i = 0; i < 10; i++) {
            Dict dict = testController.test3();
            System.out.println(dict);
        }
    }


    /**
     * 这个只有走web 才能有用
     */
    @Test
    public void test004() {

        Dict dict = testController.test_global_ex();

        System.out.println(dict);
    }

}
