### spring boot 整合jdbc Template 

##### pom.xml
```
  <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
    </dependencies>
```

#### `BaseDao.java`
```java
package com.zll.jdbctemplate.dao;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.base.CaseFormat;
import com.zll.jdbctemplate.annotation.Column;
import com.zll.jdbctemplate.annotation.Ignore;
import com.zll.jdbctemplate.annotation.Pk;
import com.zll.jdbctemplate.annotation.Table;
import com.zll.jdbctemplate.dao.user.UserDao;
import com.zll.jdbctemplate.entity.SuperUser;
import com.zll.jdbctemplate.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @param <T> 实体类型
 * @param <P> 对应表主键类型
 */
@Slf4j
public abstract class BaseDao<T, P> {

    /**
     * datasource JdbcTemplate
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * T class
     */
    private Class<T> clz;

    /**
     * P class
     */
    private Class<P> clz2;


    //构造器， 这个让子类注入
    public BaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        clz = (Class<T>) parameterizedType.getActualTypeArguments()[0];//T 的类型
        clz2 = (Class<P>) parameterizedType.getActualTypeArguments()[1];//P 的类型
        log.debug("parameterizedType = {}", parameterizedType);
        log.debug("T clz = {}, P clz = {}", clz, clz2);
    }


    /**
     * 获取表名
     */
    private String getTableName(T t) {
        log.info("t={}", t);
        //spring @AliasFor
        Table table = AnnotationUtils.findAnnotation(t.getClass(), Table.class);
        if (ObjectUtil.isNotNull(table) && StrUtil.isNotBlank(table.name())) {
            return StrUtil.format("`{}`", table.name());
        } else {
            //类名的驼峰转下划线
            String to = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, t.getClass().getSimpleName());
            return StrUtil.format("`{}`", to.toLowerCase());
        }
    }

    /**
     * 获取表名
     *
     * @return
     */
    private String getTableName() {
        log.info("class={}", clz);
        //spring @AliasFor
        Table table = AnnotationUtils.findAnnotation(clz, Table.class);
        if (ObjectUtil.isNotNull(table) && StrUtil.isNotBlank(table.name())) {
            return StrUtil.format("`{}`", table.name());
        } else {
            //没有指定名称表名称就是类名
            //类名的驼峰转下划线
            String to = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clz.getSimpleName());
            return StrUtil.format("`{}`", to.toLowerCase());
        }
    }

    /**
     * 获取列名
     */

    private List<String> getColumns(List<Field> fieldList) {

        List<String> colList = new ArrayList<>();

        for (Field field : fieldList) {
            Column column = field.getAnnotation(Column.class);
            String columnName;
            if (ObjectUtil.isNotNull(column)) {
                columnName = column.name();
            } else {
                columnName = field.getName();
            }
            //駝峰转为下划线
            String to = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, columnName);
            colList.add(StrUtil.format("`{}`", to));
        }
        return colList;
    }


    /**
     * @param t 实体的类型
     *          获取字段名
     */
    private List<Field> getFields(T t, boolean skipNull) {
        // 获取所有字段，包含父类中的字段
        Field[] fields = ReflectUtil.getFields(t.getClass());

        // 过滤数据库中不存在的字段，以及自增列, 和 ignore 字段
        Stream<Field> fieldStream = CollUtil.toList(fields).stream()
                .filter(field -> ObjectUtil.isNull(field.getAnnotation(Pk.class))
                        && ObjectUtil.isNull(field.getAnnotation(Ignore.class)));

        if (skipNull) {
            return fieldStream.filter(field -> ObjectUtil.isNotNull(ReflectUtil.getFieldValue(t, field)))
                    .collect(Collectors.toList());
        } else {
            return fieldStream.collect(Collectors.toList());
        }
    }

    /**
     * @param sql
     * @param param
     */
    private void print(String sql, Object... param) {
        log.debug("====>sql = {}", sql);
        log.debug("====>param={}", JSONUtil.toJsonStr(param));
    }


    /**
     * @param t
     * @param pk
     * @return
     */
    protected Integer updateById(T t, P pk) {
        return this.updateById(t, pk, true);
    }


//    protected Integer updateById(T t) {
//        Object id = ReflectUtil.getFieldValue(t, "id");
//        if (Objects.isNull(id))
//            throw new RuntimeException("id is null");
//        return this.updateById(t, id, true);
//    }


    /**
     * @param t
     * @param skipNull
     * @return
     */
    protected Integer insert(T t, boolean skipNull) {
        String table = this.getTableName();
        List<Field> fields = this.getFields(t, skipNull);
        List<String> columns = this.getColumns(fields);

        String cols = StrUtil.join(",", columns);

        //构造占位符
        String params = StrUtil.repeatAndJoin("?", columns.size(), ",");

        //获取参数的值
        List<Object> vals = fields.stream().map(f -> ReflectUtil.getFieldValue(t, f)).collect(Collectors.toList());

        Object[] objects = ArrayUtil.toArray(vals, Object.class);

        String sql = StrUtil.format("insert into {table}({columns}) values({params})",
                Dict.create().set("table", table).set("columns", cols).set("params", params));

        this.print(sql, objects);

        return jdbcTemplate.update(sql, objects);
    }

    /**
     * @param pk
     * @return
     */
    protected Integer deleteById(P pk) {
        String tableName = this.getTableName();
        String sql = StrUtil.format("delete from {table} where id = ?", Dict.create().set("table", tableName));
        this.print(sql, pk);
        return jdbcTemplate.update(sql, pk);
    }

    /**
     * @param t        对象
     * @param pk       主键
     * @param skipNull 是否忽略null 值
     *                 通用的根据主键删除
     */
    protected Integer updateById(T t, P pk, boolean skipNull) {

        String tableName = this.getTableName(t);

        List<Field> fields = this.getFields(t, skipNull);

        List<String> columns = this.getColumns(fields);
        String param = StrUtil.join(",", columns.stream().map(col -> StrUtil.appendIfMissing(col, "=?")).collect(Collectors.toList()));
        log.debug("param={}", param);

        //获取对象的值
        List<Object> fieldValues = fields.stream().map(f -> ReflectUtil.getFieldValue(t, f)).collect(Collectors.toList());
        fieldValues.add(pk);
        Object[] vals = ArrayUtil.toArray(fieldValues, Object.class);

        String sql = StrUtil.format("UPDATE {table} SET {param} WHERE id = ?",
                Dict.create().set("table", tableName).set("param", param));
        this.print(sql, vals);
        return jdbcTemplate.update(sql, vals);
    }

    /**
     * @param pk
     * @return
     */
    protected T findById(P pk) {
        String tableName = this.getTableName();
        String sql = StrUtil.format("select * from {table} where id = ?", Dict.create().set("table", tableName));

        print(sql, pk);

        RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clz);

        return jdbcTemplate.queryForObject(sql, new Object[]{pk}, rowMapper);
    }


    /**
     *
     */
    public List<T> findListByExample(T t) {
        String tableName = this.getTableName();
        List<Field> fields = this.getFields(t, true);
        List<String> columns = this.getColumns(fields);
        List<String> cols = columns.stream().map(col -> col + "=?").collect(Collectors.toList());
        String where = StrUtil.join(" and  ", cols);

        Object[] vals = fields.stream().map(f -> ReflectUtil.getFieldValue(t, f)).toArray();

        String sql = StrUtil.format("select * from {table} where 1=1 and {where}", Dict.create().set("table", tableName).set("where", where));

        print(sql, vals);

        List<Map<String, Object>> maps = this.jdbcTemplate.queryForList(sql, vals);

        List<T> ret = new ArrayList<>();
        maps.stream().forEach(map -> ret.add(BeanUtil.fillBeanWithMap(map, ReflectUtil.newInstance(clz), true, false)));
        return ret;
    }

}

```


### 数据源配置 application.yml
```yaml
server:
  port: 9999
  servlet:
    context-path: /jdbctemplate




# hikari 连接池配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/springboot-mybatis?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource
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
    com.zll: debug


```