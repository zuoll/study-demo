package com.zll.jdbctemplate.dao.user;

import com.zll.jdbctemplate.dao.BaseDao;
import com.zll.jdbctemplate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends BaseDao<User, Long> {

    /**
     * 构造器注入
     *
     * @param jdbcTemplate
     */
    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


    //========== 这样重写感觉有点啰嗦??? （父类的方法的访问是protected)=============

    //BaseDao 所有的方法访问范围定义为公有，然后类定义为abstract, 就可以不必重写
    public Integer updateById(User user, Long pk) {
        return super.updateById(user, pk);
    }

    public Integer insert(User user) {
        return super.insert(user, true);
    }

    public Integer deleteById(Long pk) {
        return super.deleteById(pk);
    }

    public Integer updateById(User user, Long pk, boolean skipNull) {
        return super.updateById(user, pk, skipNull);
    }

    public User findById(Long pk) {
        return super.findById(pk);
    }
}
