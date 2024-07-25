package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @date 2023/3/30 18:21
 */
@Data
@ApiModel(value = "江西客户通使用情况对象", description = "江西客户通使用情况对象")
public class McdFrontUsageInfo {

    @ApiModelProperty("地市/网格/区县/渠道编码")
    private String levelId;
    @ApiModelProperty("地市/网格/区县/渠道/区县名称")
    private String levelName;
    @ApiModelProperty("组织数")
    private String orgNum;
    @ApiModelProperty("工号数")
    private String staffNum;
    @ApiModelProperty("外呼工号数")
    private String canCallNum;
    @ApiModelProperty("外呼开通比例")
    private String canCallRate;
    @ApiModelProperty("组织登录数")
    private String orgLoginNum;
    @ApiModelProperty("组织参与比例")
    private String orgJoinRate;
    @ApiModelProperty("工号月登录总个数")
    private String staffLoginMCount;
    @ApiModelProperty("工号月登录总次数")
    private String staffLoginMNum;
    @ApiModelProperty("月员工平均登录次数")
    private String staffLoginAvg;
    @ApiModelProperty("渠道任务总数")
    private String taskAll;
    @ApiModelProperty("经理日处理任务量")
    private String campDealNum;
    @ApiModelProperty("经理当月处理量")
    private String taskMonthDeal;
    @ApiModelProperty("累计任务处理量")
    private String taskTotalDeal;
    @ApiModelProperty("当月有效办理量")
    private String efCampNumM;
    @ApiModelProperty("经理办理成功量")
    private String bprBusiNum;
    @ApiModelProperty("当月办理量")
    private String bprSuccM;
    @ApiModelProperty("累计办理量")
    private String bprSuccT;
    @ApiModelProperty("业务转换率")
    private String bprSuccRate;
    @ApiModelProperty("经理外呼量")
    private String callNum;
    @ApiModelProperty("经理外呼量月")
    private String callNumM;
    @ApiModelProperty("外呼成功量")
    private String callSuccNum;
    @ApiModelProperty("关怀短信发送量")
    private String smsSendNum;


}
