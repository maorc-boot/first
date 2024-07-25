package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 策略操作记录
 * </p>
 *
 * @author imcd
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_camp_operate_log")
@ApiModel(value="McdCampOperateLog对象", description="策略操作记录")
public class McdCampOperateLog extends Model<McdCampOperateLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志唯一标识")
    @TableField("ID")
    private String id;

    @ApiModelProperty(value = "操作用户ID")
    @TableField("USER_ID")
    private String userId;

    @ApiModelProperty(value = "操作用户名")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty(value = "策略ID")
    @TableField("CAMPSEG_ID")
    private String campsegId;

    @ApiModelProperty(value = "日志类型 1：创建策略 2：修改策略 3：审批策略 4：策略状态变更 5：删除策略")
    @TableField("LOG_TYPE")
    private Integer logType;

    @ApiModelProperty(value = "日志描述")
    @TableField("LOG_DESC")
    private String logDesc;

    @ApiModelProperty(value = "操作时间")
    @TableField("LOG_TIME")
    private Date logTime;

    @ApiModelProperty(value = "操作结果")
    @TableField("OP_RESULT")
    private String opResult;

    @ApiModelProperty(value = "用户备注信息")
    @TableField("USER_COMMENT")
    private String userComment;

    @ApiModelProperty(value = "操作人ip地址")
    @TableField("USER_IP_ADDR")
    private String userIpAddr;

    @ApiModelProperty(value = "操作人浏览器类型")
    @TableField("USER_BROWER")
    private String userBrower;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
