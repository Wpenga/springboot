package com.system.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 
 * </p>
 *
 * @author 吴泽鹏
 * @since 2023-04-12
 */
@Getter
@Setter
@TableName("sys_notice")
@ApiModel(value = "Notice对象", description = "")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("公告ID")
      @TableId(value = "notice_id", type = IdType.AUTO)
      private Integer noticeId;

      @ApiModelProperty("公告标题")
      private String noticeTitle;

      @ApiModelProperty("公告类型")
      private String noticeType;

      @ApiModelProperty("公告内容")
      private String noticeContent;

      @ApiModelProperty("公告状态")
      private String status;

      @ApiModelProperty("创建者")
      private String createBy;

      @ApiModelProperty("创建时间")
      private LocalDateTime createTime;

      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
      @ApiModelProperty("更新时间")
      private LocalDateTime updateTime;

      @ApiModelProperty("接收对象")
      private String receiver;


}
