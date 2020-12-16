package com.zll.jpa.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder

@Entity
@Table(name = "orm_user")
public class User extends BaseEntity {

    private String name;

    private String password;

    private String salt;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 状态，-1：逻辑删除，0：禁用，1：启用
     */
    private Integer status;

    @Column(name = "last_login_time")
    private Date lastLoginTime;


    /**
     * 关联表
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "orm_user_dept",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "dept_id", referencedColumnName = "id"))
    private Collection<Department> departmentList;

}
