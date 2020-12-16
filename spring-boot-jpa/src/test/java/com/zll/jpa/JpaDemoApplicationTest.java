package com.zll.jpa;

import cn.hutool.core.util.IdUtil;
import com.zll.jpa.dao.UserDao;
import com.zll.jpa.entity.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaDemoApplication.class)
public class JpaDemoApplicationTest {


    @Autowired
    private UserDao userDao;


    @Value("${test.userId}")
    private String userId;


    private static String userId2;


    /**
     * 静态注入
     *
     * @param userId2
     */
    @Value("${test.userId}")
    public void setUserId2(String userId2) {
        JpaDemoApplicationTest.userId2 = userId2;
    }


    @Test
    public void test002() {
        System.out.println("userId=" + userId);
        System.out.println("name=" + JpaDemoApplicationTest.userId2);
    }

    @Test
    @Ignore
    public void test001() {
        String salt = IdUtil.simpleUUID();
        User user = User.builder().name("test001").password("123456").salt(salt).email("123@qq.com")
                .phoneNumber("13145878049").status(1)
                .lastLoginTime(new Date()).build();

        userDao.save(user);

    }

}
