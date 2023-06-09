package com.system.springboot.service;

import com.system.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-03-30
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
