### spring boot 整合 thymeleaf

### `SessionInterceptor.java` 登录拦截
```java
package com.zll.thymeleaf.interceptor;

import com.zll.thymeleaf.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@Slf4j
public class SessionInterceptor extends HandlerInterceptorAdapter {

    /**
     * 访问任何路径只要没登录就跳转到登录页面
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("request = [" + request + "], response = [" + response + "], handler = [" + handler + "]");
        HttpSession session = request.getSession();
        if (session.getAttribute(Constant.SESSION_USER) != null) {
            return true;
        }

        String url = "/user/login-page";
        log.info("SessionInterceptor【preHandle】path = {}", request.getContextPath() + url);
        response.sendRedirect(request.getContextPath() + url);
        return false;
    }
}

```


#### 静态资源放行
```java
package com.zll.thymeleaf.config;

import com.zll.thymeleaf.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;
//    private AccessInterceptor accessInterceptor;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        AliasParamProcessor aliasResolver = new AliasParamProcessor(true);
//        argumentResolvers.add(aliasResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("WebMvcConfig.addInterceptors");
        // 自定义拦截器，添加拦截路径和排除拦截路径
//        registry.addInterceptor(accessInterceptor).addPathPatterns("/**");

        //添加自定义的拦截器,sessionInterceptor
        InterceptorRegistration interceptor = registry.addInterceptor(sessionInterceptor);
        //本项目的其他路径全部拦截
        interceptor.addPathPatterns("/**");

        //放行不需要拦截的接口
        //登录页面是要放行的，不然别人怎么登录
        interceptor.excludePathPatterns("/user/login-page");
        interceptor.excludePathPatterns("/user/doLogin");
        //默认的错误页面也不需要拦截
        interceptor.excludePathPatterns("/error");
    }


    /**
     * 静态资源的拦截
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        需要配置1：----------- 需要告知系统，这是要被当成静态文件的！
//        第一个方法设置访问路径前缀，第二个方法设置资源路径
        registry.addResourceHandler("/**").addResourceLocations("classpath:");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/images/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }
}

```


#### thymeleaf 语法学习
> https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html


