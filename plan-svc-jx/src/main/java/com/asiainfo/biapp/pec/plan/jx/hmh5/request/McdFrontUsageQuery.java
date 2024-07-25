package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @description: 客户使用情况报表插入入参对象
 * @date 2023/2/24 10:21
 */
@Data
public class McdFrontUsageQuery   {

    @ApiModelProperty("数据日期yyyy-MM-dd")
    private String dataDate;
    @ApiModelProperty("开始时间yyyy-MM-dd")
    private String startTime;
    @ApiModelProperty("结束时间yyyy-MM-dd")
    private String endTime;
    @ApiModelProperty("每页大小")
    private int pageSize = 20;
    @ApiModelProperty("当前页")
    private int currentPage =1 ;
    @ApiModelProperty("渠道ID或名称")
    private String channelIdOrName;
    @ApiModelProperty("地市ID或名称")
    private String cityIdOrName;
    @ApiModelProperty("区县ID或名称")
    private String countyIdOrName;
    @ApiModelProperty("网格编码或名称")
    private String gridCodeOrName;
    @ApiModelProperty("策略ID或名称")
    private String campsegIdOrName;
    @ApiModelProperty("工号ID或名称")
    private String staffIdOrName;

}
