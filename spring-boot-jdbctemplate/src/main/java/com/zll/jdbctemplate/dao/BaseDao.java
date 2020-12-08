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


    /**
     * @param t
     * @return
     */
    public Integer updateById(T t) {
        Object id = ReflectUtil.getFieldValue(t, "id");
        if (Objects.isNull(id))
            throw new RuntimeException("id is null");

        Class<?> clz = id.getClass();
        if (clz == clz2) {
            P pk = (P) id;
            return this.updateById(t, pk, true);
        }

        throw new RuntimeException("cast type error");
    }


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

        System.out.println(maps);

        List<T> ret = new ArrayList<>();
        maps.stream().forEach(map -> ret.add(BeanUtil.fillBeanWithMap(map, ReflectUtil.newInstance(clz), true, false)));
        return ret;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        BaseDao<User, Long> baseDao = new UserDao(null);
//        String tableName = baseDao.getTableName();
//        System.out.println(tableName);


//        BaseDao<SuperUser, Long> baseDao2 = new SuperUserDao(null);
//        String tableName2 = baseDao2.getTableName();
//        SuperUser superUser = new SuperUser();
//        superUser.setId(10L);
//        superUser.setEmail("1498742107@qq.com");
//        superUser.setPassword("123456");
//        superUser.setAge(22);
//        superUser.setPhoneNumber("13798452743");
////        String tableName3 = baseDao2.getTableName(superUser);
//        System.out.println(tableName2);
//        List fields = baseDao2.getFields(superUser, true);
//        fields.stream().forEach(f -> System.out.println(f));
//
//        System.out.println("=====================");
//        List fields2 = baseDao2.getFields(superUser, false);
//        fields2.stream().forEach(f -> System.out.println(f));


        SuperUser superUser = new SuperUser();
        // 获取所有字段，包含父类中的字段
        Field[] fields = ReflectUtil.getFields(superUser.getClass());

        for (Field field : fields) {
            Pk pk = field.getAnnotation(Pk.class);

            System.out.println(pk);
            System.out.println(ObjectUtil.isNull(field.getAnnotation(Pk.class)));
            Ignore ignore = field.getAnnotation(Ignore.class);
            System.out.println(ignore);
            System.out.println(field);

        }

        System.out.println("===================");
        List<Field> collect = CollUtil.toList(fields).stream()
                .filter(field -> Objects.isNull(field.getAnnotation(Pk.class))
                        && Objects.isNull(field.getAnnotation(Ignore.class))).collect(Collectors.toList());

        collect.stream().forEach(f -> System.out.println(f));

        List<String> columns = baseDao.getColumns(collect);
        columns.stream().forEach(col -> System.out.println(col));


        List<String> collect1 = columns.stream().map(s -> StrUtil.appendIfMissing(s, " = ?")).collect(Collectors.toList());
        System.out.println(collect1);
        String params = StrUtil.join(",", columns);
        System.out.println(params);
    }

}
