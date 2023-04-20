package com.system.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.springboot.entity.Health;
import com.system.springboot.entity.Leave;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 学生请假表 服务类
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-04-18
 */
public interface ILeaveService extends IService<Leave> {
    //关联查询获取姓名和联系方式
    List<Leave> selectAllLeaveAndUsername();

    List<Leave> getByUsername(String username);

    Page findPage(Page<Leave> page, String username, String nickname);
}
