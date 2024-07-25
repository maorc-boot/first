package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@ApiModel(value="外呼明细查询", description="外呼明细数据")
public class McdOutDetailsVo {

    @ApiModelProperty("appKey")
    private String appKey;

    @ApiModelProperty("客户振铃时长")
    private String alertDuration;

    @ApiModelProperty("业务场景ID")
    private String scenarioId;

    @ApiModelProperty("业务场景名称")
    private String scenarioName;

    @ApiModelProperty("地市")
    private String city;

    @ApiModelProperty("区县")
    private String county;

    @ApiModelProperty("网格编码")
    private String gridId;

    @ApiModelProperty("网格名称")
    private String gridName;

    @ApiModelProperty("渠道编码")
    private String channelId;

    @ApiModelProperty("渠道名称")
    private String channelName;

    @ApiModelProperty("经理工号")
    private String managerid;

    @ApiModelProperty("经理姓名")
    private String managerName;

    @ApiModelProperty("唯一标识")
    private String onlyId;

    @ApiModelProperty("被叫号码")
    private String caller;

    @ApiModelProperty("主叫号码")
    private String callee;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("外显号码")
    private String display;

    @ApiModelProperty("通话开始时间")
    private String startTime;

    @ApiModelProperty("通话结束时间")
    private String endTime;

    @ApiModelProperty("通话时长")
    private String duration;

    @ApiModelProperty("外呼结果")
    private String result;

    @ApiModelProperty("")
    private String verification;

    @ApiModelProperty("挂断类型")
    private String endType;

    @ApiModelProperty("录音文件")
    private String url;

    public void setCallee(String callee) {
        if(StringUtils.isNotBlank(callee)){
            callee = callee.substring(0,3)+"****"+callee.substring(7);
        }
        this.callee = callee;
    }
}
