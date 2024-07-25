package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ranpf
 * @description: 客户使用情况报表插入入参对象
 * @date 2023/2/24 10:21
 */
@Data
@ApiModel("客户通使用情况地市查询入参")
public class McdFrontUsageCityQuery {



    @ApiModelProperty("开始时间yyyy-MM-dd")
    private String startTime;
    @ApiModelProperty("结束时间yyyy-MM-dd")
    private String endTime;
    @ApiModelProperty("地市ID或名称")
    private String cityIdOrName;
    @ApiModelProperty("每页大小")
    private int pageSize = 20;
    @ApiModelProperty("当前页")
    private int currentPage =1 ;


}
