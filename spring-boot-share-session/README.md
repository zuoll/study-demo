##### 使用redis 来存储session 可以达到重启服务,session 不会失效, 从而客户端不必重新登陆

#### 验证
* 输入主页地址http://localhost:9999/share-session 没有登录会跳转到登录页面
* http://localhost:9999/share-session/login/login-page?redirect=true 登录成功跳转到首页
只要不换浏览器，这个session 1个小时就是有效的， 就不需要重新登录
* 如果删除redis 的这个session 就需要重新登录



### pom.xml
```$xslt
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!--spring 的共享session-->
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency>
        <!-- 对象池，使用redis时必须引入 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
```


#### 拦截器
```java
package com.zll.sharesession.interceptor;

import com.zll.sharesession.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@Slf4j
public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return super.preHandle(request, response, handler);

              //session 为空跳转到登陆的页面,不为空,跳转到主页
        HttpSession session = request.getSession();
        if (session.getAttribute(Constant.USER_TOKEN) != null) {
            return true;
        }

        String url = "/login/login-page?redirect=true";
        log.info("SessionInterceptor【preHandle】path = {}",request.getContextPath() + url);
        response.sendRedirect(request.getContextPath() + url);
        return false;
    }
}
```


#### 控制器
```java
package com.zll.sharesession.controller;

import cn.hutool.core.util.IdUtil;
import com.zll.sharesession.constants.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequestMapping("/login")
public class LoginController {


    /**
     * 跳转到首页， 必须是登录的才能到首页
     */
    @GetMapping({"/", "/index"})
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Object obj = request.getSession().getAttribute(Constant.USER_TOKEN);

        if (obj instanceof String && StringUtils.isEmpty(obj.toString())) {
            //跳转到登录页面
            return this.login(true);
        }
        view.addObject("token", obj);
        view.setViewName("page/index");
        return view;
    }

    /**
     * 跳转到登陆页面
     */
    @GetMapping("/login-page")
    public ModelAndView login(Boolean isRedirect) {
        ModelAndView view = new ModelAndView();
        if (Objects.isNull(isRedirect) || !isRedirect) {
            view.addObject("message", "请先登陆");
        }
        view.setViewName("page/login");
        return view;
    }


    /**
     * 提交登陆, 真实场景要POST提交账号密码
     */
    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        System.out.println("====>username = " + username);
        HttpSession session = request.getSession();
        String simpleUUID = IdUtil.simpleUUID();
        System.out.println("token  = " + simpleUUID);
        session.setAttribute(Constant.USER_TOKEN, simpleUUID);
        //假设登陆成功,然后跳转到首页 ，调用index(HttpServletRequest request)
        return "redirect:/login/index";
    }
}

```


#### spring-session的配置文件
```yaml
server:
  servlet:
    context-path: /share-session
  port: 9999



# 开启spring session 的redis 管理
spring:
  session:
    store-type: redis
    redis:
      namespace: spring:session
      flush-mode: immediate
#    public enum RedisFlushMode {
#    ON_SAVE,
#    IMMEDIATE;
#
#    private RedisFlushMode() {
#  }
#}


  redis:
    host: localhost
    port: 6379
#    连接的超时时间 记得要有单位Duration
    timeout: 10000ms

#    redis 连接池lettuce的配置
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0


```

#### 
![image-text](https://zll-images-1254006866.cos.ap-guangzhou.myqcloud.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20201207104018.png)