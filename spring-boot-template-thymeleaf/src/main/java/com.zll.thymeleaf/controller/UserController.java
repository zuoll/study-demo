package com.zll.thymeleaf.controller;

import com.zll.thymeleaf.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {


    /**
     * 未登录跳转到登录页面
     */
    @GetMapping("/login-page")
    public ModelAndView login() {
        return new ModelAndView("page/login");
    }

    /**
     * 登录成功跳转到个人主页
     */
    @PostMapping("/doLogin")
    public ModelAndView doLogin(HttpServletRequest request, User user) {
        log.info("user=>{}", user);

        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/");
        view.addObject(user);
        request.getSession().setAttribute("user", user);
        return view;
    }
}
