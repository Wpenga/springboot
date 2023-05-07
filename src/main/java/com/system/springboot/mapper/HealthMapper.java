package com.system.springboot.mapper;

import com.system.springboot.entity.Health;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-04-14
 */
public interface HealthMapper extends BaseMapper<Health> {
    // 查询学生及其打卡记录
//    @Select("select s.*, r.id as record_id, r.punch_date as punchDate, r.is_fever, r.go_risk, r.vaccine_count " +
//            "from stu_user s left join stu_health r on s.username=r.username "+"WHERE s.username = #{username}")
    List<User> selectAllStudentAndRecord(@Param("username") String username);

    //获取用户打卡信息，联表查询获取用户昵称
    @Select("select stu_health.*,sys_user.nickname from stu_health left join sys_user on stu_health.username = sys_user.username "
            +"where stu_health.username=#{username}  ORDER BY id DESC LIMIT 1")
    Health getHealthByUser(String username);


    //计算最新发烧人数
    @Select("SELECT COUNT(*) FROM stu_health WHERE id IN (\n" +
            "  SELECT MAX(id)\n" +
            "  FROM stu_health\n" +
            "  GROUP BY username\n" +
            ") AND is_fever = 1;")
    int isFeverCount();

    //或许最新的打卡信息
    @Select("SELECT * FROM stu_health WHERE id IN\n" +
            "    (SELECT MAX(id) FROM stu_health  GROUP BY username);")
    List<Health> getisSign();


    //一对一查询，根据用户名username查询用户表的昵称nickname
    @Select("select * from stu_health where username=#{username} ORDER BY id DESC LIMIT 1")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "username",property = "username"),
            @Result(column = "is_fever",property = "isFever"),
            @Result(column = "vaccine_count",property = "vaccineCount"),
            @Result(column = "go_risk",property = "goRisk"),
            @Result(column = "punch_date",property = "punchDate"),
            @Result(column = "username",property = "nickname",javaType = User.class,
                    one = @One(select = "com.system.springboot.mapper.UserMapper.findByUsername")
            ),
    })
    List<Health> findHealthAndNickname(String username);
}
