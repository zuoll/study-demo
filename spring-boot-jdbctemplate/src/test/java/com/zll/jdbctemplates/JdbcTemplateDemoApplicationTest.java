package com.zll.jdbctemplates;

import cn.hutool.json.JSONUtil;
import com.zll.jdbctemplate.JdbcTemplateDemoApplication;
import com.zll.jdbctemplate.dao.user.UserDao;
import com.zll.jdbctemplate.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = JdbcTemplateDemoApplication.class)
@RunWith(SpringRunner.class)
public class JdbcTemplateDemoApplicationTest {


    @Autowired
    private UserDao userDao;

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(4L);
        user.setAge(25);
        user.setPhoneNumber("1008611");
        user.setName("wangnima");
        Integer ret = userDao.updateById(user, 3L);
        System.out.println(ret);
    }


    @Test
    public void testDelete() {
        User user = new User();
        user.setAge(18);
        user.setPhoneNumber("10086111111");
        user.setName("wangnima2");

        Integer ret = userDao.insert(user);
        System.out.println(ret);
    }


    @Test
    public void testFindById() {
        User user = userDao.findById(4L);

        System.out.println(JSONUtil.toJsonStr(user));
    }

    @Test
    public void testFindList() {
        User user = new User();
        user.setAge(25);
        List<User> listByExample = userDao.findListByExample(user);
        System.out.println(listByExample);
    }

    @Test
    public void testUpdate2() {
        User user = new User();
        user.setId(3L);
        user.setAge(60);
        user.setPhoneNumber("1008611");
        user.setName("wangnima100");
        Integer ret = userDao.updateById(user);
        System.out.println(ret);
    }

}
