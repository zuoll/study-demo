package com.zll.thymeleaf.controller;

import com.zll.thymeleaf.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@Slf4j
public class IndexController {

    /**
     * 用户个人主页
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"", "/"})
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();

        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)) {
            mv.setViewName("redirect:/user/login-page");
        } else {
            mv.setViewName("page/index");
            mv.addObject(user);
        }
        return mv;
    }
}
