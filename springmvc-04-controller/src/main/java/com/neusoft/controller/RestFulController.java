package com.neusoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RestFulController {

    // 常规方式
    @RequestMapping("/add")
    public String test1(int a, int b, Model model){
        int res = a+b;
        model.addAttribute("msg","结果为："+res);
        return "test";
    }

    // RestFul
    @RequestMapping(value = "/add2/{a}/{b}",method = RequestMethod.GET)
    public String test2(@PathVariable int a, @PathVariable int b, Model model){
        int res = a+b;
        model.addAttribute("msg","结果为："+res);
        return "test";
    }

    @GetMapping("/add3/{a}/{b}")
    public String test3(@PathVariable int a, @PathVariable int b, Model model){
        int res = a+b;
        model.addAttribute("msg","结果为："+res);
        return "test";
    }
    @PostMapping("/add3/{a}/{b}")
    public String test4(@PathVariable int a, @PathVariable int b, Model model){
        int res = a+b;
        model.addAttribute("msg","结果为："+res);
        return "test";
    }
}
