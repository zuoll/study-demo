### spring boot 整合mybatis


#### pom.xml

```$xslt
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <mybatis.version>1.3.2</mybatis.version>
    </properties>

    <dependencies>
        
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis.version}</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

    </dependencies>
```


#### 连接池，数据源 ，mybatis 相关配置
```yaml
server:
  servlet:
    context-path: /mybatis-demo
  port: 9999


# https://www.jianshu.com/p/c414d36450f4
# hikari 连接池配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/springboot-mybatis?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource

    #   开发用 start
    #    initialization-mode: always
    #    continue-on-error: true
    #    schema:
    #      - "classpath*:db/schema.sql"
    #    data:
    #      - "classpath*:db/data.sql"
    #
    #    #   开发用 end

    hikari:
      minimum-idle: 5
      connection-test-query: SELECT 1 FROM DUAL
      maximum-pool-size: 20
      #      是否自动提交
      auto-commit: true
      #      超时30s
      idle-timeout: 30000
      pool-name: SpringBootDemoHikariCP
      #      60s
      max-lifetime: 60000
      #      连接超时30s
      connection-timeout: 30000


# 日志级别 按照包名
logging:
  level:
    com.zll.mybatis.mapper: trace
    #    打印执行的sql
    com.zll: debug



#mybatis 相关配置


mybatis:
  configuration:
    #    表的字段到java 实体的映射 下划线转为驼峰
    map-underscore-to-camel-case: true
  # 指定sql映射文件位置(mybatis 扫描xml文件路径)
  mapper-locations:
    - classpath*:mappers/*/**.xml
    - classpath*:mappers/**.xml
  #    有这个查询字段和实体就不需要别名 as 做转化了（下划线转为驼峰或者驼峰转为下划线）
  type-aliases-package: com.zll.mybatis.entity

```


#### `userMapper.java`
```java
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

```

#### 运行结果
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20201207172202.png)


参考
> Mybatis官方文档：http://www.mybatis.org/mybatis-3/zh/index.html
  
> Mybatis官方脚手架文档：http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/
  
> Mybatis整合Spring Boot官方demo：https://github.com/mybatis/spring-boot-starter/tree/master/mybatis-spring-boot-samples