#### spring-boot-helloworld 
```sh

mvn clean package -DskipTests

```

> 本demo 演示如何写一个spring-boot的helloworld 

##### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>


    <groupId>com.zll.study</groupId>
    <artifactId>spring-boot-helloworld</artifactId>
    <packaging>jar</packaging>
    <version>1.0-SNAPSHOT</version>


    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.5.RELEASE</version>
    </parent>


    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.1.2</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

    </dependencies>


    <build>
        <finalName>${project.name}</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>


</project>


```

##### 编写启动类(HelloWorldApplication.java)使用嵌入的tomcat
```java
/**
 * spring boot 的启动类
 *
 * {@link SpringBootApplication}
 * {@link SpringApplication}
 */
@RestController
@SpringBootApplication
public class HelloWorldSpringboot {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldSpringboot.class, args);
    }


    /**
     *
     * @param name
     * @return
     */
    @RequestMapping("/hello")
    public Dict hello(@RequestParam(required = false, defaultValue = "zll") String name) {
        return Dict.create().set("msg", "hello," + name);
    }
}

``` 


#### 配置文件 application.yml
```yaml
server:
  servlet:
    context-path: /demo001
  port: 9999
```

##### 运行结果
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/20201205102656.png)


##### 一个小功能
* git 提交emoji表情的方法
```
https://github.com/Kimi-Gao/Program-Blog/issues/71

```
