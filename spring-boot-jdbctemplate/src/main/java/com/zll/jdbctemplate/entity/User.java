package com.zll.jdbctemplate.entity;

import com.zll.jdbctemplate.annotation.Ignore;
import com.zll.jdbctemplate.annotation.Pk;
import com.zll.jdbctemplate.annotation.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Table(name = "ty_user")
public class User implements Serializable {

    @Pk
    private Long id;

    private String name;

    private Integer age;

    private String phoneNumber;

    @Ignore
    private String createTime;

    @Ignore
    private String updateTime;
}
