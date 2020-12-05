######  spring 统一的异常处理，有两种，第1， 抛异常返回统一的格式，第2， 如果是页面 ，跳到统一的异常页面

###### pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>spring-boot-demo-ratelimit-redis</groupId>
    <artifactId>spring-boot-global-ex-handler</artifactId>
    <packaging>jar</packaging>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.5.RELEASE</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

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
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>


        <!--
        javax.servlet.ServletException: Circular view path [hello]: would dispatch back to the current handler
        URL [/global-ex/hello] again. Check your ViewResolver setup! (Hint:-->

        <!--导入这个包是错误的-->
        <!--   <dependency>
               <groupId>org.thymeleaf</groupId>
               <artifactId>thymeleaf</artifactId>
           </dependency>-->

        <!--spring boot 集成thymeleaf-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
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

##### 全局的异常处理器类 **GlobalExceptionHandler.java**

```java
package com.zll.ex;

import com.zll.ex.constants.Status;
import com.zll.ex.exception.JsonException;
import com.zll.ex.exception.PageException;
import com.zll.ex.model.ApiResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

/**
 * spring boot 的统一异常处理启动类
 */
@SpringBootApplication
@Controller
public class GlobalExApplication {
    public static void main(String[] args) {
        SpringApplication.run(GlobalExApplication.class, args);
    }


    /**
     * 返回一个同一个json 错误
     *
     * @return
     */
    @RequestMapping("/json")
    @ResponseBody
    public ApiResponse testJson() {
        throw new JsonException(Status.UNKNOWN_ERROR);
    }


    /**
     * @return
     */
    @RequestMapping("/json-ok")
    @ResponseBody
    public ApiResponse testJsonOK() {
        return ApiResponse.ofSuccess("ok");
    }


    /**
     * 返回一个同一个错误页面 error.html
     *
     * @return
     */
    @RequestMapping("/page")
    public ModelAndView testPage() {
        throw new PageException(510, "参数错误");
    }


    /**
     * 返回一个正常的hello.html
     *
     * @return
     */
    @RequestMapping("/hello")
    public ModelAndView testHello() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("data", LocalDateTime.now());
        //相当于访问 /templates/hello.html
        modelAndView.setViewName("hello");
        return modelAndView;
    }
}

```

##### 异常类**BaseException.java** **PageException.java** **JsonException.java**
```java
package com.zll.ex.exception;

import com.zll.ex.constants.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private Integer errCode;

    private String errMsg;


    public BaseException(Status status) {
        //这个必须要调用一下，否则堆栈抛出的异常是null
        super(status.getMsg());
        this.errCode = status.getCode();
        this.errMsg = status.getMsg();
    }


    public BaseException(Integer errCode, String errMsg) {
        //这个必须要调用一下，否则堆栈抛出的异常是null
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}

```


##### 启动类**GlobalExApplication.java**
```java
package com.zll.ex;

import com.zll.ex.constants.Status;
import com.zll.ex.exception.JsonException;
import com.zll.ex.exception.PageException;
import com.zll.ex.model.ApiResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

/**
 * spring boot 的统一异常处理启动类
 */
@SpringBootApplication
@Controller
public class GlobalExApplication {
    public static void main(String[] args) {
        SpringApplication.run(GlobalExApplication.class, args);
    }


    /**
     * 返回一个同一个json 错误
     *
     * @return
     */
    @RequestMapping("/json")
    @ResponseBody
    public ApiResponse testJson() {
        throw new JsonException(Status.UNKNOWN_ERROR);
    }


    /**
     * @return
     */
    @RequestMapping("/json-ok")
    @ResponseBody
    public ApiResponse testJsonOK() {
        return ApiResponse.ofSuccess("ok");
    }


    /**
     * 返回一个同一个错误页面 error.html
     *
     * @return
     */
    @RequestMapping("/page")
    public ModelAndView testPage() {
        throw new PageException(510, "参数错误");
    }


    /**
     * 返回一个正常的hello.html
     *
     * @return
     */
    @RequestMapping("/hello")
    public ModelAndView testHello() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("data", LocalDateTime.now());
        //相当于访问 /templates/hello.html
        modelAndView.setViewName("hello");
        return modelAndView;
    }
}

```
