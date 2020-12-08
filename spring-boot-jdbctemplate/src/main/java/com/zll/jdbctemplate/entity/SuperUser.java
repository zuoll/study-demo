package com.zll.jdbctemplate.entity;

import com.zll.jdbctemplate.annotation.Ignore;
import com.zll.jdbctemplate.annotation.Pk;
import com.zll.jdbctemplate.annotation.Table;
import lombok.Data;

@Data
@Table
public class SuperUser extends User {

    @Pk
    private Long id;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private String email;

    /**
     *
     */
    @Ignore
    private String addr;
}
