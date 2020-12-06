package com.zll.sharesession.interceptor;

import com.zll.sharesession.constants.Constant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return super.preHandle(request, response, handler);

        //session 为空跳转到登陆的页面,不为空,跳转到主页
        HttpSession session = request.getSession();
        if (session.getAttribute(Constant.USER_TOKEN) != null) {
            return true;
        }

        String url = "page/login?redirect=true";
        response.sendRedirect(request.getContextPath() + url);
        return false;


    }
}
