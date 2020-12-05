package com.zll.prop.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Getter
@Setter
@ToString
@Component
public class AppConfig {

    @Value("${demo.app-name}")
    private String name;

    @Value("${demo.app-desc}")
    private String desc;

    /**
     * 不存在时给一个默认值
     */
    @Value("${demo.app-version:1.0.0}")
    private String version;
}
