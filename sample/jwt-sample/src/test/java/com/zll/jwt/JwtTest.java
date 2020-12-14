package com.zll.jwt;

import com.zll.jwt.conf.AudienceConf;
import com.zll.jwt.util.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtTest {

    @Autowired
    private AudienceConf audienceConf;


    @Test
    public void test001() {

        String token = JwtTokenUtil.createJWT("10086", "zhangsan", "admin", audienceConf);
        System.out.println(token);

        String userId = JwtTokenUtil.getUserId(token, audienceConf.getBase64Secret());
        String userName = JwtTokenUtil.getUserName(token, audienceConf.getBase64Secret());

        System.out.println(userId);
        System.out.println(userName);
    }
}
