package com.zll.freemarker.model;

import lombok.Data;

@Data
public class User {
    /**
     *
     */
    private String username;
    /**
     *
     */
    private String password;


    //idea 要安装lombok 插件
    //测试lombok 自动生成 的方法
    public static void main(String[] args) {
        User user = new User();
        System.out.println(user.getUsername());
    }
}
