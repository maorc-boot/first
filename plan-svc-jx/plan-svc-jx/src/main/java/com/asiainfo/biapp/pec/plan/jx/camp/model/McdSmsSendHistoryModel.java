package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("江西短信发送历史数据表")
@TableName("MCD_SMS_SEND_HISTORY_INFO")
public class McdSmsSendHistoryModel {

    @ApiModelProperty("策略ID")
    @TableId("CAMPSEG_ID")
    private String campsegId;
    @ApiModelProperty("策略根ID")
    @TableField("CAMPSEG_ROOT_ID")
    private String campsegRootId;
    @ApiModelProperty("策略名称")
    @TableField("CAMPSEG_NAME")
    private String campsegName;
    @ApiModelProperty("策略创建人")
    @TableField("CREATE_USER_ID")
    private String createUserId;
    @ApiModelProperty("创建人名称")
    @TableField("USER_NAME")
    private String userName;
    @ApiModelProperty("策略开始时间")
    @TableField("START_DATE")
    private String startDate;
    @ApiModelProperty("策略结束时间")
    @TableField("END_DATE")
    private String endDate;
    @ApiModelProperty("推荐用语")
    @TableField("EXEC_CONTENT")
    private String execContent;
    @ApiModelProperty("任务ID")
    @TableField("TASK_ID")
    private String taskId;
    @ApiModelProperty("地市ID")
    @TableField("CITY_ID")
    private String cityId;
    @ApiModelProperty("地市名称")
    @TableField("CITY_NAME")
    private String cityName;
    @ApiModelProperty("客户群数量")
    @TableField("CUST_LIST_COUNT")
    private int custListCount;
    @ApiModelProperty("发送量")
    @TableField("SEND_NUM")
    private int sendNum;
    @ApiModelProperty("渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;



}
