package com.system.springboot.service;

import com.system.springboot.common.Result;
import com.system.springboot.entity.User;

public interface LoginService {
    Result login(User user);

    Result logout();
}
