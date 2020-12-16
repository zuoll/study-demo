package com.zll.jpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "orm_department")
public class Department extends BaseEntity {


    @Column(name = "name", columnDefinition = "varchar(255) not null")
    private String name;


    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "super_dept", referencedColumnName = "id")
    private Department superDept;


    @Column(name = "levels", columnDefinition = "int not null default 0")
    private Integer levels;


    @Column(name = "sort_no", columnDefinition = "int not null default 0")
    private Integer sortNo;


    /**
     * 子部门集合
     */
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER, mappedBy = "superDept")
    private Collection<Department> children;

    /**
     * 部门下用户集合
     */
    @ManyToMany(mappedBy = "departmentList")
    private Collection<User> userList;


}
