package com.zll.sharesession.config;

import com.zll.sharesession.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //添加自定义的拦截器,sessionInterceptor
        InterceptorRegistration interceptor = registry.addInterceptor(sessionInterceptor);

        //放行不需要拦截的
        interceptor.excludePathPatterns("/page/login");
        interceptor.excludePathPatterns("/page/doLogin");
        interceptor.excludePathPatterns("/error");

        //其他的全部拦截
        interceptor.addPathPatterns("/**");
    }
}
