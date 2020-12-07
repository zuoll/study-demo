package com.zll.mybatis;

import com.zll.mybatis.entity.User;
import com.zll.mybatis.mapper.user.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@SpringBootTest(classes = MybatisDemoApplication.class)
@RunWith(SpringRunner.class)
public class MybatisDemoApplicationTest {


    @Autowired
    private UserMapper userMapper;


    /**
     * 添加用户
     */
    @Test
    public void test001() {
        User user = new User();
        user.setAge(25);
        user.setPhoneNumber("13145878045");
        user.setName("ZLL5");
        System.out.println("insert before id = " + user.getId());
        int ret = userMapper.saveUser(user);
        Assert.isTrue(ret == 1, "insert fail");
        //获取插入的主键
        System.out.println("insert after id = " + user.getId());
    }


    @Test
    public void test002() {
        User user = userMapper.getById(5L);
        Assert.notNull(user, "user is null");
        System.out.println(user);
    }

    @Test
    public void test003() {
        int ret = userMapper.deleteById(2L);
        Assert.isTrue(ret == 1, "delete fail");
    }
}
