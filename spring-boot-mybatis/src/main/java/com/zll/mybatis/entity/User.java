package com.zll.mybatis.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private Long id;

    private String name;

    private Integer age;

    private String phoneNumber;

    private String createTime;

    private String updateTime;
}
