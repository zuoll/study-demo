package com.zll.mybatis.mapper.user;

import com.zll.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Mapper和@Component 这个两个注解可以不写
//@Mapper
//org.apache.ibatis.binding.BindingException: Invalid bound statement (not found): com.zll.mybatis.mapper.user.UserMapper.saveUser
//出现这个文件就是包的扫描路径配错了
//@Component //可以不写，在运行时注入，但是写了，测试注入就不会有红色警告线
public interface UserMapper {

    /**
     * 查询用户列表
     */
    @Select("select * from ty_user")
    List<User> listUser();

    /**
     * 查询单个用户
     */
    @Select("select * from ty_user where phone_number=#{phone_number}")
    User getByPhoneNum(@Param("phone_number") String phone);


    /**
     * 根据主键查询
     */
    @Select("select * from ty_user where id = #{id}")
    User getById(@Param("id") Long id);

    //===============下面的方法的实现通过mapper.xml 中的sql===================

    /**
     * 删除用户根据id
     *
     * @param id
     * @return {@code 1} 成功。{@code 0} 失败
     */
    int deleteById(@Param("id") Long id);


    /**
     * 添加用户
     */
//    @Insert("")
//    @Options可以获取mybatis插入的主键 配合 @Insert
//    @Options(keyProperty = "id", keyColumn = "id", useGeneratedKeys = true)
    int saveUser(User user);
}
