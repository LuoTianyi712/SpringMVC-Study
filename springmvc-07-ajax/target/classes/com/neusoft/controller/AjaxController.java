package com.neusoft.controller;

import com.neusoft.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

    @RequestMapping("/test1")
    public String testAjax(){
        return "ajax";
    }

    @RequestMapping("/a1")
    public void a1(String name, HttpServletResponse resp)
            throws IOException {
        System.out.println("a1:param: "+name);
        if (name.equals("yes")){
            resp.getWriter().print("true");
        }else {
            resp.getWriter().print("false");
        }
    }

    @RequestMapping("/a2")
    public List<User> a2(){
        List<User> userList = new ArrayList<>();

        userList.add(new User("ass",1,"male"));
        userList.add(new User("bss",2,"female"));
        userList.add(new User("css",3,"male"));

        return userList;
    }

    @RequestMapping("/a3")
    public String a3(String name,String pwd){
        String msg = "";

        if (name != null) {
            if (name.equals("admin")){
                msg = "ok";
            }else {
                msg = "username error";
            }
        }

        if (pwd != null) {
            if (pwd.equals("123456")){
                msg = "ok";
            }else {
                msg = "password error";
            }
        }
        return msg;
    }
}
