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
