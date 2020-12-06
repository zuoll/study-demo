package com.zll.freemarker.controller;

import com.zll.freemarker.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@Slf4j
public class IndexController {

    @RequestMapping({"", "/"})
    public ModelAndView index(HttpServletRequest request){
        System.out.println("======>IndexController.index");
        ModelAndView modelAndView = new ModelAndView();

        User user = (User) request.getSession().getAttribute("user");

        if(Objects.isNull(user)){
            modelAndView.setViewName("redirect:/user/login");
        }else{
            // resources/templates/page/index.ftl
            modelAndView.setViewName("page/index");
            modelAndView.addObject(user);
        }
        return modelAndView;
    }
}
