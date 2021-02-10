package com.neusoft.controller;

import com.neusoft.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/test1")
    public String test1(@RequestParam("username") String name, Model model){
        // 1.接收前端参数
        System.out.println("接收到前端的参数："+name);
        // 2.将返回的结果传递给前端
        model.addAttribute("msg",name);
        // 3.视图跳转
        return "hello";
    }

    @GetMapping("test2")
    public String test2(User user, Model model){
        System.out.println(user);
        model.addAttribute("msg",user);
        return "hello";
    }

    @GetMapping("test3")
    public String test3(User user, ModelMap map){
        System.out.println(user);
        map.addAttribute("msg",user);
        return "hello";
    }
}
