package com.system.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.springboot.controller.dto.UserPasswordDTO;
import com.system.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
//实现set和getter
//@Mapper  已全局配置
public interface UserMapper extends BaseMapper<User> {
    //更新密码
    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);
    //分页 联表 多条件 模糊 查询
    Page<User> findPage(Page<User> page,@Param("role") String role, @Param("username") String username,@Param("address") String address,@Param("nickname") String nickname,@Param("email") String email,@Param("phone")String phone);


    //获取未签到学生表
    List<User> getStuExportList();

    @Select("select nickname from sys_user where username=#{username}")
    String findByUsername(@Param("username") String username);
    /*
    //查找所有数据
    @Select("select * from stu_user")
    List<User> findAll();

    //插入
    @Insert("INSERT INTO stu_user(username,password,nickname,phone,address)" +
            "VALUES(#{username},#{password},#{nickname},#{phone},#{address})")
    int insert(User user);

    //更新
//    @Update("update stu_user set username=#{username}, password=#{password}, " +
//            "nickname=#{nickname}, phone=#{phone}, address=#{address} where id=#{id}" )
    int update(User user);

    //删除
    @Delete("DELETE FROM stu_user WHERE id = #{id}")
    Integer deleteById(@Param("id") Integer id);
    //分页查询  模糊查询
    @Select("select * from stu_user where username like concat('%',#{username},'%') limit #{pageNum}, #{pageSize}")
    List<User> selectPage(Integer pageNum, Integer pageSize,String username);


    //计算全部数据
    @Select("select count(*) from stu_user where username like concat('%',#{username},'%')")
    Integer selectTotal(String username);*/
}