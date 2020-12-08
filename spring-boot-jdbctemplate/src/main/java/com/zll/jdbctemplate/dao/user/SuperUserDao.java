package com.zll.jdbctemplate.dao.user;

import com.zll.jdbctemplate.dao.BaseDao;
import com.zll.jdbctemplate.entity.SuperUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SuperUserDao extends BaseDao<SuperUser, Long> {

    /**
     * 构造器注入
     *
     * @param jdbcTemplate
     */
    @Autowired
    public SuperUserDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }
}
