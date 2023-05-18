package com.system.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.springboot.common.Constants;
import com.system.springboot.controller.dto.UserDTO;
import com.system.springboot.controller.dto.UserDTO2;
import com.system.springboot.entity.User;
import com.system.springboot.exception.ServiceException;
import com.system.springboot.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new ServiceException(Constants.CODE_700, "账号不存在，请注册");
        }
        List<String> list = Collections.singletonList(user.getRole());
        return new UserDTO2(user,list);
    }
}