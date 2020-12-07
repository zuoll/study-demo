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
