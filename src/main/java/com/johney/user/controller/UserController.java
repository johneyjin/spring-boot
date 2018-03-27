package com.johney.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.johney.user.model.User;
import com.johney.user.service.UserService;

/**
 * Created by zl on 2015/8/27.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @RequestMapping()
    /**
     * @responseBody 注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，
     * 写入到response对象的body区，通常用来返回JSON数据或者是XML数据，需要注意的呢，在使用此注解之后
     * 不会再走试图处理器，而是直接将数据写入到输入流中，他的效果等同于通过response对象输出指定格式的数据。
     * @param model
     * @return
     */
    public String getUserInfo(Model model) {
//        List<User> user = userService.getUserInfo();
//        model.addAttribute("user",user);
//    	System.out.println("访问user");
        return "/user/user";
    }
    
    /**
     * 如果是后台到前台的json数据，则使用@ResponseBody
     */
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public List<User> getUserInfo(String page,String rows){
    	User users=new User();
//    	users.setName("johney");
//    	List<User> user = userService.findUserInfoByUser(users);
    	List<User> user2 = userService.getUserInfo();
//    	System.out.println("取到数据了");
        return user2;
    }
}
