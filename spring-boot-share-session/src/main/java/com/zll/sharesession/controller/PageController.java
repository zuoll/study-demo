package com.zll.sharesession.controller;

import com.zll.sharesession.constants.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/page")
public class PageController {


    /**
     * 跳转到首页
     */
    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        String token = (String) request.getSession().getAttribute(Constant.USER_TOKEN);
        view.addObject("token", token);
        view.setViewName("index");
        return view;
    }

    /**
     * 跳转到登陆页
     */
    @GetMapping("/login")
    public ModelAndView login(Boolean isRedirect) {
        ModelAndView view = new ModelAndView();
        if (Objects.isNull(isRedirect) || !isRedirect) {
            view.addObject("message", "请先登陆");
        }
        view.setViewName("login");
        return view;
    }


    /**
     * 登陆
     */
    @PostMapping("/doLogin")
    public String doLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        System.out.println("username = "+username);
        HttpSession session = request.getSession();
        session.setAttribute(Constant.USER_TOKEN, UUID.randomUUID());
        //假设登陆成功,然后跳转到首页
        return "redirect:/page/index";
    }

}
