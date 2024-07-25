package com.asiainfo.biapp.cmn.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author ranpf
 * @since 2023-5-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("notice_read_user_list")
@ApiModel(value="McdNoticeReadUserListModel对象", description="上线通知已读信息记录表")
public class McdNoticeReadUserListModel extends Model<McdNoticeReadUserListModel> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "通知ID")
    @TableField(value = "notice_id" )
    private Long noticeId;

    @ApiModelProperty(value = "已读人ID")
    @TableField(value = "read_user_id" )
    private String readUserId;

    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date readTime;

}



