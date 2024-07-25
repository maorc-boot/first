package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @date 2023/5/25 18:21
 */
@Data
@ApiModel(value = "江西客户通抢单执行情况对象", description = "江西客户通抢单执行情况对象")
public class McdFrontUsageOppoHandleInfo {


    @ApiModelProperty("日期")
    private String dataDate;
    @ApiModelProperty("策略名称")
    private String campsegName;
    @ApiModelProperty("策略ID")
    private String campsegId;
    @ApiModelProperty("策略地市")
    private String campCityId;
    @ApiModelProperty("策略地市名称")
    private String campCityName;
    @ApiModelProperty("地市编码")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("营销任务下发量")
    private int taskCount;
    @ApiModelProperty("当月抢单任务外呼量")
    private int campCallM;
    @ApiModelProperty("当月抢单任务处理量")
    private int campDealM;
    @ApiModelProperty("当月抢单任务成功量")
    private int campHandleM;
    @ApiModelProperty("累计抢单任务外呼量")
    private int campCallT;
    @ApiModelProperty("累计抢单任务处理量")
    private int campDealT;
    @ApiModelProperty("累计抢单外呼成功量")
    private int campHandleT;



}
