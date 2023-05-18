package com.system.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.system.springboot.common.Constants;
import com.system.springboot.common.Result;
import com.system.springboot.entity.User;
import com.system.springboot.service.LoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginController {
    @Resource
    private LoginService loginService;
    @PostMapping("/user/login")
    public Result login(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){//判断添加 1.不为null 2.长度不为0 3.不存在空格
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return loginService.login(user);
    }

    @GetMapping("/user/logout")
    public Result logout(){
        return loginService.logout();
    }
}