package com.johney.user.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johney.user.mapper.UserMapper;
import com.johney.user.model.User;

/**
 * Created by zl on 2015/8/27.
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getUserInfo(){
        return userMapper.findUserInfo();
    }
    
    public List<User> findUserInfoByUser(User user){
    	return userMapper.findUserInfoByUser(user);
    }

}
