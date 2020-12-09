## 本 demo 主要演示了如何将 Spring Boot 项目打包成传统的 war 包程序。(传统的方式)

> 开发的时候和嵌入式tomcat启动一样，部署是外部的tomcat 
#### pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>study-demo</artifactId>
        <groupId>com.zll.study</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-boot-war</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--打包方式要写为war-->
    <packaging>war</packaging>

    <dependencies>

        <!-- 若需要打成 war 包，则需要将 tomcat 引入，scope 设置为 provided -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>

    </dependencies>
</project>
```

#### 启动类
```java
package com.zll.war;

import cn.hutool.core.lang.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 打包成war 包需要启动器 继承 {@literal SpringBootServletInitializer} 重写 configure 方法
 *
 * @see <a href="https://docs.spring.io/spring-boot/docs/2.1.0.RELEASE/reference/htmlsingle/#howto-create-a-deployable-war-file">官方打war包参考</a>
 * @see SpringBootServletInitializer#configure(SpringApplicationBuilder)
 */
@SpringBootApplication
@RestController
@Slf4j
public class WarDemoApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(WarDemoApplication.class, args);
        log.info("WarDemoApplication 启动成功");
    }

    /**
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WarDemoApplication.class);
    }


    /**
     * @return
     */
    @RequestMapping("/hello-war")
    public Dict helloWar(HttpServletRequest request) {
        log.info("===>path={}", request.getRequestURI());
        return Dict.create().set("msg", "hello war," + LocalDateTime.now());
    }
}

/**
 * 如果运行启动类出现servlet API 找不到就 mvn clean package ,
 * 然后 删除掉edit configurations 的启动了,重新运行启动类就可以了
 */

```

#### 配置文件
```yaml

#这个在外部的tomcat 下运行时不生效的, 本地spring boot 侵入式启动才有效
server:
  port: 9999
  servlet:
    context-path: /war
```


#### 温故知新，一些差异化
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20201209105311.png)
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20201209112214.png)
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20201209111939.png)
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20201209105428.png)


#### war 和 侵入式tomcat jar 的区别
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20201209101331.png)
![image-text](http://https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20201207104018.png)