package com.neusoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// 代表这个类会被Spring接管
// 被标注为 @Controller 的类下的所有的方法
// 如果返回值是String，并且有具体的页面可以跳转，那么就会被视图解析器所解析
public class ControllerTest2{

    @RequestMapping("/test2")
    public String test2(Model model){

        model.addAttribute("msg","Controller Test 2");

        return "test";
    }

    @RequestMapping("/test3")
    public String test3(Model model){

        model.addAttribute("msg","Controller Test 3");

        return "test";
    }

}
