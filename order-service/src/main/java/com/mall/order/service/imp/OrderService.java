package com.mall.order.service.imp;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mall.order.dto.User;
import com.mall.order.dto.mapper.UserMapper;
import com.mall.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hongfei
 */
@Service
@Slf4j
public class OrderService extends ServiceImpl<UserMapper,User> implements IOrderService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisScript<Boolean> rateLimiters;
    @Override
    public User getUserInfo(String id) {
        String Url="http://user-service/user/get/{id}";
        User user = restTemplate.getForObject(Url,User.class,id);
        return user;
    }

    /**
     *
     * @param key  key
     * @param limit
     *
     * redis限流
     *  redis-order-limit
     */
    @Override
    public void limitAccess(String key, Integer limit){

        boolean acquire = stringRedisTemplate.execute(
                rateLimiters,
                Lists.newArrayList(key),
                limit.toString()
        );
        if(!acquire){
            log.error("you access is block is {}",key);
            throw new RuntimeException(" YOU ACCESS IS BLOCK");
        }

    }
}
