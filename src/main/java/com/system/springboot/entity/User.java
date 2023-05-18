package com.system.springboot.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

//实体用户类
//该注解实现自动写getter，setter类，toString
@Data
@TableName("sys_user")
@ApiModel(value = "User对象")
//@ToString

public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户名")
    @Alias("用户名")           //HuTool提供的别名注解工具
    private String username;

//    @JsonIgnore  //忽略
    @ApiModelProperty("密码")
    @Alias("密码")
    private String password;

    @ApiModelProperty("昵称")
    @Alias("昵称")
    private String nickname;

    @ApiModelProperty("性别")
    @Alias("性别")
    private String sex;

    @ApiModelProperty("地址")
    @Alias("地址")
    private String address;

    @ApiModelProperty("手机号")
    @Alias("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    @Alias("邮箱")
    private String email;

    @ApiModelProperty("签到状态")
    @Alias("签到状态")
    private String sign;

    @ApiModelProperty("创建时间")
    @Alias("创建时间")
    private Date createTime;

    @ApiModelProperty("头像")
    @Alias("头像")
    private String avatarUrl;

    @ApiModelProperty("角色")
    @Alias("角色")
    private String role;

    @ApiModelProperty("打卡信息")
    @TableField(exist = false)
    private List<Health> health;

    @ApiModelProperty("签到时间")
    @Alias("签到时间")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date punchDate;

    @ApiModelProperty("是否发烧（0:未发烧，1:发烧）")
    @Alias("是否发烧（0:未发烧，1:发烧）")
    @TableField(exist = false)
    private Boolean isFever;

}
