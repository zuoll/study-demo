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
