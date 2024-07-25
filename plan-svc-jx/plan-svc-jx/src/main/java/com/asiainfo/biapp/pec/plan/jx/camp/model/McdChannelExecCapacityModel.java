package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("渠道执行能力查询对象")
@TableName("mcd_channel_exec_capacity")
public class McdChannelExecCapacityModel {

    @ApiModelProperty(value = "渠道ID")
    @TableId("CHANNEL_ID")
    private String channelId;
    @ApiModelProperty(value = "今日已营销")
    @TableField("EXEC_CAPACITY")
    private int execCapacity;


}
