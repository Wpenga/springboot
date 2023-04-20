package com.system.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.springboot.entity.Health;
import com.system.springboot.entity.Leave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 学生请假表 Mapper 接口
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-04-18
 */
public interface LeaveMapper extends BaseMapper<Leave> {
    //关联查询获取姓名和联系方式
    @Select("select l.*, u.nickname, u.phone from stu_leave l left join sys_user u on l.username=u.username")
    List<Leave> selectAllLeaveAndUsername();

    @Select("select l.*,u.nickname from stu_leave l left join sys_user u on l.username=u.username where l.username=#{username}")
    List<Leave> selectLeaveAndUsername(@Param("username") String username);


    Page<Leave> findPage(Page<Leave> page,@Param("username") String username,@Param("nickname") String nickname);
}
