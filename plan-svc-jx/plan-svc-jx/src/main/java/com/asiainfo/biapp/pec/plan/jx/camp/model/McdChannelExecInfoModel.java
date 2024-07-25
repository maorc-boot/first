package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("渠道执行信息查询对象")
@TableName("mcd_channel_exec_info")
public class McdChannelExecInfoModel {

    @ApiModelProperty(value = "渠道ID")
    @TableId("CHANNEL_ID")
    private String channelId;
    @ApiModelProperty(value = "今日已营销")
    @TableField("EXEC_NUM")
    private int execNum;
    @ApiModelProperty(value = "今日待营销")
    @TableField("NO_EXEC_NUM")
    private int noExecNum;

}
