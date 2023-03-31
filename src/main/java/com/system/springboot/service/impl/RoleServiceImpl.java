package com.system.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.springboot.entity.Role;
import com.system.springboot.entity.RoleMenu;
import com.system.springboot.mapper.RoleMapper;
import com.system.springboot.mapper.RoleMenuMapper;
import com.system.springboot.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-03-30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Transactional  //要么执行成功，要么不执行
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        //1.删除角色绑定关系
        roleMenuMapper.delete(queryWrapper);
//        roleMenuMapper.deleteByRoleId(roleId);
        //2.将前端传来的菜单id数组绑定到角色id
        for(Integer menuId : menuIds){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }


}
