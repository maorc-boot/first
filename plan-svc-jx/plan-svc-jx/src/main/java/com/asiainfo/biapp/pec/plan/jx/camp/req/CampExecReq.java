package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/10/28
 */
@Data
@ApiModel("活动执行信息查询")
public class CampExecReq {

    @ApiModelProperty(value = "根活动ID", required = true)
    private String campsegRootId;

    @ApiModelProperty(value = "子活动ID", required = false)
    private String campsegId;

    @ApiModelProperty(value = "渠道ID", required = false)
    private String channelId;

    @ApiModelProperty(value = "开始时间日期格式:yyyyMMdd", required = false, notes = "日期格式:yyyyMMdd")
    private String startDate;

    @ApiModelProperty(value = "结束时间,日期格式:yyyyMMdd", required = false, notes = "日期格式:yyyyMMdd")
    private String endDate;
}
