package com.mall.user.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.user.dto.User;
import com.mall.user.dto.mapper.UserMapper;
import com.mall.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hongfei
 */
@Service
@Slf4j
public class UserService extends ServiceImpl<UserMapper,User>  implements IUserService {


}
