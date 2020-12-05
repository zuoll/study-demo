###  本demo 演示如何读取自定义配置，



#### 自定义的属性可以有导航和hit的两个要点
*  \META-INF\additional-spring-configuration-metadata.json
```json
{
  "properties": [
    {
      "name": "develop.qq",
      "type": "java.lang.String",
      "description": "developer qq.",
      "defaultValue": ""
    },
    {
      "name": "develop.name",
      "type": "java.lang.String",
      "description": "developer name.",
      "defaultValue": ""
    },
    {
      "name": "demo.app-name",
      "type": "java.lang.String",
      "description": "application name.",
      "defaultValue": ""
    },
    {
      "name": "demo.app-desc",
      "type": "java.lang.String",
      "description": "application description.",
      "defaultValue": ""
    },
    {
      "name": "demo.app-version",
      "type": "java.lang.String",
      "description": "application version.",
      "defaultValue": ""
    }
  ]
}

```

* 导入配置处理器依赖
```xml
        <!--
		在 META-INF/additional-spring-configuration-metadata.json 中配置
		可以去除 application.yml 中自定义配置的红线警告，并且为自定义配置添加 hint 提醒
		 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
```


#### 编写一个AppConfig.java 的配置类
```java
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

```

#### 编写一个DeveloperConfig的配置类
```java
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

```


#### 编写启动类
```java
package com.zll.prop;

import cn.hutool.core.lang.Dict;
import com.zll.prop.config.AppConfig;
import com.zll.prop.config.DeveloperConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * {@code @AllArgsConstructor(onConstructor_ = @Autowired)}
 * 可以给所有的属性注入依赖,而不必每个属性都去@Autowired
 */
@AllArgsConstructor(onConstructor_ = @Autowired)
@SpringBootApplication
@RestController
public class PropApplication {

    private AppConfig appConfig;

    private DeveloperConfig developerConfig;

    public static void main(String[] args) {
        SpringApplication.run(PropApplication.class, args);
    }


    @GetMapping("/get-config")
    public Dict testGetConfig() {
        return Dict.create().set("app_config", appConfig)
                .set("develop_config", developerConfig);
    }

}


```

#### 运行结果展示
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/20201205143025.png)


#### 自定义注释提示
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/20201205115148.png)

