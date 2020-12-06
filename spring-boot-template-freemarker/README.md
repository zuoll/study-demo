##### spring boot 继承freemarker 演示


##### pom.xml
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

    <artifactId>spring-boot-template-freemarker</artifactId>

    <dependencies>
        <!--spring boot 整合freemarker-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>
    </dependencies>

</project>
```


#### application.yml
```yaml
server:
  port: 9999
  servlet:
    context-path: /freemarker


#    FreeMarkerProperties.java
#    这些都是默认的配置，
spring:
  freemarker:
    cache: false
    charset: utf-8
    suffix: .ftl
    enabled: true

```

##### 控制器 `IndexController.java` `UserController.java`
```java
package com.zll.freemarker.controller;

import com.zll.freemarker.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    /**
     * get 请求是到登录的页面
     *
     * @return
     */
    @GetMapping("/login")
    public ModelAndView login() {
        System.out.println("======>get 请求 UserController.login");
        ModelAndView view = new ModelAndView();
        view.setViewName("page/login");
        return view;
    }


    /**
     * post 请求是用户提交了账号和密码 验证是正确
     *
     * @return
     */
    @PostMapping("/login")
    public ModelAndView login(User user, HttpServletRequest request) {
        System.out.println("======>post 请求 UserController.login");
        //假设用户提交的是正确的账号和密码
        System.out.println("===>user="+user);
        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/"); //跳转到主页
        view.addObject(user);

        //用户存储到session
        request.getSession().setAttribute("user", user);
        return view;
    }
}

```

```java
package com.zll.freemarker.controller;

import com.zll.freemarker.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@Slf4j
public class IndexController {

    @RequestMapping({"", "/"})
    public ModelAndView index(HttpServletRequest request){
        System.out.println("======>IndexController.index");
        ModelAndView modelAndView = new ModelAndView();

        User user = (User) request.getSession().getAttribute("user");

        if(Objects.isNull(user)){
            modelAndView.setViewName("redirect:/user/login");
        }else{
            // resources/templates/page/index.ftl
            modelAndView.setViewName("page/index");
            modelAndView.addObject(user);
        }
        return modelAndView;
    }
}

```

#### page
`index.ftl`
```jsp
<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<body>
<div id="app" style="margin: 20px 20%">
    欢迎登录，${user.username}！
</div>
</body>
</html>
```

`login.ftl`
```jsp
<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<body>
<div id="app" style="margin: 20px 20%">
    欢迎登录，${user.username}！
</div>
</body>
</html>
```

##### freemarker 学习网址
>  https://freemarker.apache.org/docs/dgui.html
