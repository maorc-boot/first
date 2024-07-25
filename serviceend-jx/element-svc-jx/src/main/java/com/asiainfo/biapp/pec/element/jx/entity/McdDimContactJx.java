package com.asiainfo.biapp.pec.element.jx.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 触点信息表
 * </p>
 *
 * @author wuhq6
 * @since 2021-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_dim_contact")
@ApiModel(value="McdDimContact对象", description="触点信息表")
public class McdDimContactJx extends Model<McdDimContactJx> {

    private static final long serialVersionUID = -363315701730181811L;

    @ApiModelProperty(value = "触点ID")
    @TableId("CONTACT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private String contactId;

    @ApiModelProperty(value = "渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty(value = "来源: 0：省级触点信息 1：全网触点信息")
    @TableField("CONTACT_SOURCE")
    private Integer contactSource;

    @ApiModelProperty(value = "触点类型: 0：电子渠道 1：实体渠道")
    @TableField("CONTACT_TYPE")
    private Integer contactType;

    @ApiModelProperty(value = "触点名称")
    @TableField("CONTACT_NAME")
    private String contactName;

    @ApiModelProperty(value = "触点编码")
    @TableField("CONTACT_CODE")
    private String contactCode;

    @ApiModelProperty(value = "触点状态")
    @TableField("CONTACT_STATUS")
    private Integer contactStatus;

    @ApiModelProperty(value = "是否可排期: 0：不可排期 1：可排期")
    @TableField("IS_SCHEDULED")
    private Integer isScheduled;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "触点描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "触点客户类型 0-集团 1-成员")
    @TableField("CONTACT_CUST_TYPE")
    private Integer contactCustType;

    @ApiModelProperty(value = "执行交互流程 0-任务推送 1-推荐查询 2-更多类型")
    @TableField("CONTACT_EXEC_FLOW")
    private Integer contactExecFlow;

    @ApiModelProperty(value = "免打扰时段 如：0:00-6:00,22:00-24:00")
    @TableField("CONTACT_AVOID_TIME")
    private String contactAvoidTime;


    @Override
    protected Serializable pkVal() {
        return this.contactId;
    }

}
