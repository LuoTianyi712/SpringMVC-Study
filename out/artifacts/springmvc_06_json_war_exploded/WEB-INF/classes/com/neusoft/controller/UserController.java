package com.neusoft.controller;

import com.alibaba.fastjson.JSON;
import com.neusoft.pojo.User;
import com.neusoft.utils.JsonUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/json")
public class UserController {

    @RequestMapping("/test1")
    public String test1() {

        // 创建一个对象
        User user = new User("阿斯顿",15,"MALE");

        return JsonUtils.getJson(user);
    }

    @RequestMapping("/test2")
    public String test2() {
        List<User> userList = new ArrayList<>();
        for (int i=1;i<=5;i++){
            User user = new User("阿斯顿"+i,15+i,"MALE");
            userList.add(user);
        }
        return JsonUtils.getJson(userList);
    }

    @RequestMapping("/test3")
    public String test3() {
        Date date = new Date();
        return JsonUtils.getJson(date, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping("/test4")
    public String test4() {
        List<User> userList = new ArrayList<>();
        for (int i=1;i<=5;i++){
            User user = new User("阿斯顿"+i,15+i,"MALE");
            userList.add(user);
        }
        return JSON.toJSONString(userList);
    }
}
