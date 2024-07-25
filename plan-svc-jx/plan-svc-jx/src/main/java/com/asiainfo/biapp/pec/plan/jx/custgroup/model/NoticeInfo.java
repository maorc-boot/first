package com.asiainfo.biapp.pec.plan.jx.custgroup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ranpf
 * @since 2023-2-7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("notice_info")
@ApiModel(value="江西NoticeInfo对象", description="江西通知信息")
public class NoticeInfo extends Model<NoticeInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;

    @ApiModelProperty(value = "通知标题")
    private String noticeName;

    @ApiModelProperty(value = "通知内容")
    private String noticeContent;

    @ApiModelProperty(value = "通知类型：1-IOP, 2-帮助, 3-日历")
    private Integer noticeType;

    @ApiModelProperty(value = "接收用户ID")
    private String noticeUserId;

    @ApiModelProperty(value = "发布人ID")
    private String releaseUserId;

    @ApiModelProperty(value = "是否展示在页面：1-展示 0-不展示")
    private Integer isShowTip;

    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "修改人")
    private String modifyUser;

    @ApiModelProperty(value = "通知状态 0草稿 1 已发布")
    private Integer status;

    @ApiModelProperty(value = "附件路径")
    private String attachmentPath;

    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd",timezone ="GMT+8")
    @ApiModelProperty(value = "日历提醒时间")
    private Date calendarTime;

    @ApiModelProperty(value = "是否已读日程数据 0-未读 1-已读")
    private Integer isRead;


    @Override
    protected Serializable pkVal() {
        return this.noticeId;
    }

}
