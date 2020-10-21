package com.mall.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.mall.user.dto.User;
import com.mall.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hongfei
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @GetMapping("/get/{id}")
    @SentinelResource("get")
    public User get(@PathVariable String id){
        User user = userService.getById(id);
        return user;
    }
}
