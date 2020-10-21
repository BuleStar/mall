package com.mall.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.order.dto.User;

/**
 * @author hongfei
 */
public interface IOrderService extends IService<User> {



    public User getUserInfo(String id);
    public void limitAccess(String key, Integer limit);
}
