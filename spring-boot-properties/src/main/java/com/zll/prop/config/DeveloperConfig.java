package com.zll.prop.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * tip 如果name,qq 这两个属性要添加导航，和代码提示。
 * 需配置一个文件META/INF/additional-spring-configuration-metadata.json
 * 和一个依赖spring-boot-configuration-processor
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "develop")
public class DeveloperConfig {

    /**
     *
     */
    private List<String> name;

    /**
     *
     */
    private List<String> qq;
}
