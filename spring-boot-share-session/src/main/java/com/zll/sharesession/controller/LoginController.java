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
