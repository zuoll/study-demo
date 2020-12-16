package com.zll.jpa.entity;

import lombok.*;

/**
 * POJO
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TestBean {
    private String userId;
    private String userName;
    private int age;
}