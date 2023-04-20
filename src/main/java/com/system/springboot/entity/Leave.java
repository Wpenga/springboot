package com.system.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 学生请假表
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-04-18
 */
@Getter
@Setter
  @TableName("stu_leave")
@ApiModel(value = "Leave对象", description = "学生请假表")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("请假ID，主键")
      @TableId(value = "Leave_ID", type = IdType.AUTO)
      private Integer leaveId;

      @ApiModelProperty("学号")
      private String username;

      @ApiModelProperty("请假开始时间")
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
      private LocalDateTime startTime;

      @ApiModelProperty("请假结束时间")
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
      private LocalDateTime endTime;

      @ApiModelProperty("请假原因")
      private String reason;

      @ApiModelProperty("审批状态（0:未批准，1:已批准，2:已驳回）")
      private Integer status;

      @ApiModelProperty("申请时间（记录学生申请时间）")
      private LocalDateTime applyTime;


      //昵称 手机号 用户表获取
      @TableField(exist = false)
      private String nickname;
      @TableField(exist = false)
      private String phone;

}
