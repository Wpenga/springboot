package com.system.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.springboot.entity.Health;
import com.system.springboot.entity.Leave;
import com.system.springboot.mapper.LeaveMapper;
import com.system.springboot.service.ILeaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 学生请假表 服务实现类
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-04-18
 */
@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements ILeaveService {
    @Resource
    LeaveMapper leaveMapper;
    @Override
    public List<Leave> selectAllLeaveAndUsername() {
        return leaveMapper.selectAllLeaveAndUsername();
    }

    @Override
    public List<Leave> getByUsername(String username) {
        return leaveMapper.selectLeaveAndUsername(username);
    }

    @Override
    public Page findPage(Page<Leave> page, String username, String nickname) {
        return leaveMapper.findPage(page,username,nickname);
    }
}
