package com.system.springboot.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.springboot.common.RoleEnum;
import com.system.springboot.controller.dto.UserPasswordDTO;
import com.system.springboot.service.IUserService;
import com.system.springboot.common.Constants;
import com.system.springboot.controller.dto.UserDTO;
import com.system.springboot.entity.Menu;
import com.system.springboot.entity.User;
import com.system.springboot.exception.ServiceException;
import com.system.springboot.mapper.RoleMapper;
import com.system.springboot.mapper.RoleMenuMapper;
import com.system.springboot.mapper.UserMapper;
import com.system.springboot.service.IMenuService;
import com.system.springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

//服务实现类
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    private  static final Log LOG = Log.get();

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO login(UserDTO userDTO) {     //登录逻辑判断
        // 对用户密码进行md5加密
//        userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User isExit = getOne(wrapper);
        if(isExit == null){
            throw new ServiceException(Constants.CODE_700, "账号不存在，请注册");
        }
        User one = getUserInfo(userDTO);  //账号密码验证
        //非空继续判断
        if (one != null) {
            //根据登录信息copy数据库对应的全部信息 one复制给userDTO
            BeanUtils.copyProperties(one, userDTO);
            //设置Token
            String token = TokenUtils.getToken(one.getId().toString(),one.getPassword());
            userDTO.setToken(token);
            String role = one.getRole();
            //存储用户的菜单
            List<Menu> rolesMenus = getRoleMenus(role);
            userDTO.setMenus(rolesMenus);

            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDTO) {       //注册逻辑操作
//        userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User one = getOne(wrapper);
        if(one == null){
            one = new User();
            BeanUtils.copyProperties(userDTO,one);
            //默认注册的用户角色 = 学生
            one.setRole(RoleEnum.ROLE_STUDENT.toString());
            one.setPassword(passwordEncoder.encode(one.getPassword()));
            if (one.getNickname() == null) {
                one.setNickname(one.getUsername());  //姓名默认学号
            }
            save(one);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在，请直接登录");
        }
        return one;
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {   //更新密码
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userPasswordDTO.getUsername());// 使用 LambdaQueryWrapper 构造查询条件
        User user = getOne(wrapper);
        int rows = 0;
        if(user != null && passwordEncoder.matches(userPasswordDTO.getPassword(),user.getPassword())){
            user.setPassword(passwordEncoder.encode(userPasswordDTO.getNewPassword()));
//            userPasswordDTO.setNewPassword(passwordEncoder.encode(userPasswordDTO.getNewPassword()));
//            System.out.println(userPasswordDTO.getNewPassword());
            //更新1  Parameter 'username' not found. Available parameters are [ew, param1, et, param2]
//            boolean row = new LambdaUpdateChainWrapper<User>(userMapper)
//                    .eq(User::getUsername,userPasswordDTO.getUsername())
//                    .set(User::getPassword,userPasswordDTO.getNewPassword())
//                    .update();
            //更新2
//            update(
//                Wrappers.<User>lambdaUpdate()
//                    .eq(User::getUsername,userPasswordDTO.getUsername())
//                    .set(User::getPassword,userPasswordDTO.getNewPassword())
//            );
//            LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//            lambdaUpdateWrapper.eq(User::getUsername,userPasswordDTO.getUsername())
//                                .set(User::getPassword,userPasswordDTO.getNewPassword());

//            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("username", "admin")
//                         .set("password", userPasswordDTO.getNewPassword());
            rows = userMapper.updateById(user);
//            int rows = userMapper.update(null, updateWrapper);
//            update = userMapper.updatePassword(userPasswordDTO);
        }

        if (rows < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    @Override
    public Page findPage(Page<User> page,String role ,String username, String address, String nickname,String email, String phone) {
        return userMapper.findPage(page,role,username,address,nickname,email,phone);
    }

    /**
     * 获取用户信息
     * @param userDTO
     * @return
     */
    private User getUserInfo(UserDTO userDTO){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
//        wrapper.eq(User::getPassword, userDTO.getPassword());
        User one;
        try {
            one = getOne(wrapper); //查询数据库信息
            if(one != null && !passwordEncoder.matches(userDTO.getPassword(),one.getPassword())){
                return null;
            }
        } catch (Exception e) {
            LOG.error(e);
            throw  new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
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
   /* @Autowired
    private UserMapper userMapper;
      MyBatis
    public  int save(User user){
        if(user.getId() == null){   //判断新增还是更新
            return userMapper.insert(user);
        }
        else {  //更新
            return userMapper.update(user);
        }
    }*/
    //MyBatis Plus
    /*public  boolean saveUser(User user){
        if(user.getId() == null){   //判断新增还是更新
            return save(user);
        }
        else {  //更新
            return updateById(user);
        }
        return saveOrUpdate(user);
    }*/
