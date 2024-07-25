package com.asiainfo.biapp.pec.plan.jx.specialuser.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 特殊客户群表
 * </p>
 *
 * @author imcd
 * @since 2022-02-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mcd_special_use")
@ApiModel(value = "McdSpecialUse对象", description = "特殊客户群表")
public class JxMcdSpecialUse extends Model<JxMcdSpecialUse> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("渠道id")
    @TableField("channel_id")
    private String channelId;

    @ApiModelProperty("特殊客户群类型")
    @TableField("cust_type")
    private Integer custType;

    @ApiModelProperty("手机号码")
    @TableField("product_no")
    private String productNo;

    @ApiModelProperty("描述")
    @TableField("desc")
    private String desc;

    @ApiModelProperty("入库时间")
    @TableField("enter_time")
    private Date enterTime = new Date();

    @ApiModelProperty("创建用户id")
    @TableField("create_user_id")
    private String createUserId;

    @ApiModelProperty("数据源 1:手工导入 2:系统导入")
    @TableField("data_source")
    private Integer dataSource;

    @ApiModelProperty("生效时间")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty("失效时间")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty("创建用户名称")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty("渠道名称")
    @TableField("CHANNEL_NAME")
    private String channelName;


    @Override
    public Serializable pkVal() {
        return null;
    }

}
