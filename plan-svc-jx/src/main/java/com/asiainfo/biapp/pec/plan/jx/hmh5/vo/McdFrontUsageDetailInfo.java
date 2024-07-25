package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @date 2023/5/2518:21
 */
@Data
@ApiModel(value = "江西客户通活动处理对象", description = "江西客户通活动处理对象")
public class McdFrontUsageDetailInfo {


    @ApiModelProperty("日期")
    private String dataDate;
    @ApiModelProperty("策略名称")
    private String campsegName;
    @ApiModelProperty("开始时间")
    private String startDate;
    @ApiModelProperty("结束时间")
    private String endDate;
    @ApiModelProperty("策略ID")
    private String campsegId;
    @ApiModelProperty("创建人")
    private String createUserId;
    @ApiModelProperty("创建人名称")
    private String createUserName;
    @ApiModelProperty("策略描述")
    private String campsegDesc;
    @ApiModelProperty("策略地市")
    private String campCityId;
    @ApiModelProperty("策略地市名称")
    private String campCityName;
    @ApiModelProperty("部门")
    private String deptId;
    @ApiModelProperty("部门名称")
    private String deptName;
    @ApiModelProperty("地市编码")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("营销任务下发量")
    private int taskCount;
    @ApiModelProperty("当月任务外呼量")
    private int campCallNum;
    @ApiModelProperty("当月任务处理量")
    private int campDealNum;
    @ApiModelProperty("外呼成功量")
    private int campCallSucc;
    @ApiModelProperty("累计任务外呼量")
    private int campCallTotal;
    @ApiModelProperty("累计任务处理量")
    private int campDealTotal;
    @ApiModelProperty("累计外呼成功量")
    private int campCallSuccTotal;



}
