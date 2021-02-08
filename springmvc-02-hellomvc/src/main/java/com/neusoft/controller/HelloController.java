package com.neusoft.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 导入Controller接口
public class HelloController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ModelAndView 模型和视图
        ModelAndView mv = new ModelAndView();
        // 封装对象，放在 ModelAndView 的 model 中
        mv.addObject("msg","HelloSpringMVC");
        // 封装视图，放在ModelAndView 的 view 中
        mv.setViewName("hello");
        return mv;
    }
}
