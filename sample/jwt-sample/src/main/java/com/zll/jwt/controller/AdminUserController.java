package com.zll.jwt.controller;

import cn.hutool.core.lang.Dict;
import com.zll.jwt.conf.AudienceConf;
import com.zll.jwt.conf.JwtIgnore;
import com.zll.jwt.util.ApiResponse;
import com.zll.jwt.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
public class AdminUserController {

    @Autowired
    private AudienceConf audienceConf;


    /**
     * @param response
     * @param username
     * @param password
     * @return
     */
    @JwtIgnore //登录是要放行的
    @PostMapping("/login")
    public ApiResponse adminLogin(HttpServletResponse response, String username, String password) {
        // 这里模拟测试, 默认登录成功，返回用户ID和角色信息
        String userId = "10086";
        String role = "admin";

        // 创建token
        String token = JwtTokenUtil.createJWT(userId, username, role, audienceConf);
        log.info("### 登录成功, token={} ###", token);

        // 将token放在响应头
        response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY, JwtTokenUtil.TOKEN_PREFIX + token);
        // 将token响应给客户端
        return ApiResponse.ofSuccess(Dict.create().set("token", token));
    }


    @GetMapping("/users")
    public ApiResponse userList(HttpServletRequest request) {
        log.info("### 查询所有用户列表 ###");
        Dict dict = Dict.create();
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains(JwtTokenUtil.TOKEN_PREFIX)) {
            String token = authorization.split(" ")[1];
            String userId = JwtTokenUtil.getUserId(token, audienceConf.getBase64Secret());
            String userName = JwtTokenUtil.getUserName(token, audienceConf.getBase64Secret());
            dict.set("userId", userId).set("userName", userName);
        }
        return ApiResponse.ofSuccess(dict);
    }
}
