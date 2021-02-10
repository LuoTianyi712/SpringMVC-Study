package com.neusoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/model")
public class ModelTest1 {
    // 在spring配置文件中，移除视图解析器

    @RequestMapping("/test1")
    public String test(Model model){
        model.addAttribute("msg", "Model Test 1");
        // 转发
        return "/WEB-INF/jsp/hello.jsp";
    }

    @RequestMapping("/test2")
    public String test2(Model model){
        model.addAttribute("msg", "Model Test 2");
        // 转发
        return "forward:/WEB-INF/jsp/hello.jsp";
    }

    @RequestMapping("/test3")
    public String test3(Model model){
        model.addAttribute("msg", "Model Test 3");
        // 重定向
        return "redirect:/index.jsp";
        // http://localhost:8080/servletapi/index.jsp?msg=Model+Test+3
        // http://localhost:8080/servletapi/test3
    }
}
