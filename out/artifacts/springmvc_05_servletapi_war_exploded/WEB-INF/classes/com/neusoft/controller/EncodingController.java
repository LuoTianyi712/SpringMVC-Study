package com.neusoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/encoding")
public class EncodingController {
    @RequestMapping("/test1")
    public String test(String name, Model model){
        model.addAttribute("msg",name);
        System.out.println(name);
        return "hello";
    }
}
