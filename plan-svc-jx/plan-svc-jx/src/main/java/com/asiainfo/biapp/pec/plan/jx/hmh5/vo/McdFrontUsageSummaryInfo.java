package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @date 2023/5/30 18:21
 */
@Data
@ApiModel(value = "江西客户通使用情况明细对象", description = "江西客户通使用情况明细对象")
public class McdFrontUsageSummaryInfo {

    @ApiModelProperty("地市编码")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("渠道编码")
    private String channelId;
    @ApiModelProperty("渠道名称")
    private String channelName;
    @ApiModelProperty("区县编码")
    private String countyId;
    @ApiModelProperty("区县名称")
    private String countyName;
    @ApiModelProperty("工号")
    private String staffId;
    @ApiModelProperty("名称")
    private String staffName;
    @ApiModelProperty("外呼量")
    private String callingNum;
    @ApiModelProperty("是否开通外呼功能 1 有,否则 无")
    private String callFlag;
    @ApiModelProperty("日期")
    private String dataDate;
    @ApiModelProperty("看护类型 1 工号看护 否则 渠道共享看护 ")
    private String isSelfManager;
    @ApiModelProperty("组织登录数")
    private int joinStaffM;
    @ApiModelProperty("组织参与比例")
    private String loginChannel;
    @ApiModelProperty("登录次数")
    private int loginCount;
    @ApiModelProperty("月登录总次数")
    private int loginCountM;
    @ApiModelProperty("月员工平均登录次数")
    private int loginStaff;
    @ApiModelProperty("营销任务总量isSelfManager=1,取staffTask否则channelShareTask")
    private int staffTask;
    @ApiModelProperty("渠道任务量")
    private int channelShareTask;
    @ApiModelProperty("经理当月处理量")
    private int taskMonthDeal;
    @ApiModelProperty("累计任务处理量")
    private int taskTotalDeal;
    @ApiModelProperty("当月有效办理量")
    private int efCampNumM;
    @ApiModelProperty("经理办理成功量")
    private int bprBusiNum;
    @ApiModelProperty("当月办理量")
    private int bprSuccM;
    @ApiModelProperty("累计办理量")
    private int bprSuccT;
    @ApiModelProperty("外呼量月")
    private int callNumM;
    @ApiModelProperty("外呼成功量-月")
    private int callSuccM;
    @ApiModelProperty("关怀短信发送量 月")
    private int smsMonthM;


}
