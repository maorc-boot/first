package com.asiainfo.biapp.pec.approve.jx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/4/26
 */
@Data
@ApiModel(value = "预警管理详情查询")
public class WarningDetailReq extends WarningReq {

    @ApiModelProperty(value = "地市ID")
    private String cityId;
    @ApiModelProperty(value = "部门ID")
    private String deptId;
    @ApiModelProperty(value = "创建人")
    private String createUser;
    @ApiModelProperty(value = "开始时间")
    private String startDate;
    @ApiModelProperty(value = "结束时间")
    private String endDate;
}
