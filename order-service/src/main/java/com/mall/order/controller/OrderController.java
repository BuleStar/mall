package com.mall.order.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.util.concurrent.RateLimiter;
import com.mall.order.dto.User;
import com.mall.order.service.IOrderService;
import com.mall.order.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author hongfei
 */
@RestController
@RefreshScope
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private IOrderService orderService;

    RateLimiter rateLimiter=RateLimiter.create(2.0);

    @GetMapping("/getuser/{id}")
    @SentinelResource(value = "getUser",blockHandlerClass = {ExceptionUtils.class})
    public User getUser(@PathVariable String id ){
        User user = orderService.getUserInfo(id);
//        user.setUserName(username);
        return user;
    }

    /**
     * Google guava 限流
     * @return
     */
    @GetMapping("/tryAcquire")
    public String tryAcquire(){

        if(rateLimiter.tryAcquire(1)){
            return "success";
        }else {

            return "fail";
        }
    }

    @GetMapping("/tryAcquireTime")
    public String tryAcquireTIme(){
        if(rateLimiter.tryAcquire(1,10, TimeUnit.SECONDS)){
            return "success";
        }else {

            return "fail";
        }
    }


    @GetMapping("/ratelimiter")
    public String redisRateLimiter(){

        orderService.limitAccess("order-service-ratelimiter",1);
        return "redis flow success";
    }

}
