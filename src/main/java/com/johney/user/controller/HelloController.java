package com.johney.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.johney.user.model.User;
import com.johney.user.service.UserService;

@Controller
@RequestMapping("/hello")
public class HelloController {
	@Autowired
	private UserService userService;

    @RequestMapping()
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
//        model.addAttribute("name", name);
        User users=null;
        userService.getUserInfo();
//        int i=1/0;
//        System.out.println(users.getAge());
      //返回的是在src/main/resources/templates下的文件名称（后缀默认.html）
        return "/hello/hello";
    }
    
}
