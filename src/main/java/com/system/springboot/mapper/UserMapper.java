package com.system.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.springboot.controller.dto.UserPasswordDTO;
import com.system.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
//实现set和getter
//@Mapper  已全局配置
public interface UserMapper extends BaseMapper<User> {
    //更新密码
    @Update("update stu_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);
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
