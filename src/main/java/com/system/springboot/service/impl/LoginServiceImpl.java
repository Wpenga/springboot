package com.system.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.springboot.common.Constants;
import com.system.springboot.common.Result;
import com.system.springboot.controller.dto.UserDTO;
import com.system.springboot.controller.dto.UserDTO2;
import com.system.springboot.entity.Menu;
import com.system.springboot.entity.User;
import com.system.springboot.exception.ServiceException;
import com.system.springboot.mapper.RoleMapper;
import com.system.springboot.mapper.RoleMenuMapper;
import com.system.springboot.mapper.UserMapper;
import com.system.springboot.service.IMenuService;
import com.system.springboot.service.LoginService;
import com.system.springboot.utils.RedisCache;
import com.system.springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LoginServiceImpl  implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;
//    @Override
//    public Result login(User user) {
//        //AuthenticationManager authenticate进行用户认证
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        if(Objects.isNull(authenticate)){
//            throw new RuntimeException("登录失败");
//        }
//        // 获取一个UserDetails
//        UserDTO2 userDTO = (UserDTO2) authenticate.getPrincipal();
//        String userId = userDTO.getUser().getId().toString();
//        String jwt = TokenUtils.getToken(userId,userDTO.getUser().getPassword());
//        userDTO.setToken(jwt);
//        //把完整的用户信息存入redis  userid作为key
//        redisCache.setCacheObject("login:"+userId,userDTO);
//        return Result.success(userDTO);
//    }
    @Override
    public Result login(User user) {     //登录逻辑判断
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
//            throw new RuntimeException("登录失败");
        }

        // 获取一个UserDetails
        UserDTO2 userDTO = (UserDTO2) authenticate.getPrincipal();
        String userId = userDTO.getUser().getId().toString();
        String role = userDTO.getUser().getRole();
        //存储用户的菜单
        List<Menu> rolesMenus = getRoleMenus(role);
        userDTO.setMenus(rolesMenus);
        // 生成token
        String jwt = TokenUtils.getToken(userId,userDTO.getUser().getPassword());
        userDTO.setToken(jwt);
        //把完整的用户信息存入redis  userid作为key
        redisCache.setCacheObject("login:"+userId,userDTO);
        return Result.success(userDTO);
    }
    @Override
    public Result logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDTO2 loginUser = (UserDTO2) authentication.getPrincipal();
        Integer userid = loginUser.getUser().getId();
        //删除redis中的值
        redisCache.deleteObject("login:"+userid);
        return Result.success("注销成功");
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        //根据role(flag)获取表sye_role对应的id
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        // 根据id获取表sys_role_menu对应的menuId数组
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // 存储筛选完成之后的列表
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
