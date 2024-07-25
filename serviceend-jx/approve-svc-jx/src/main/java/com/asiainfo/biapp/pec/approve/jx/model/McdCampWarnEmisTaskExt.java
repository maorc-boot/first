package com.asiainfo.biapp.pec.approve.jx.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/4/26
 */
@Data
@ApiModel(value = "预警详情")
public class McdCampWarnEmisTaskExt extends McdCampWarnEmisTask {

    @ApiModelProperty(value = "创建人名称")
    private String userName;
    @ApiModelProperty(value = "地市名称")
    private String cityName;
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "汇总:预警总数")
    private int total;
    @ApiModelProperty(value = "汇总:未处理数量")
    private int unfinished;
    @ApiModelProperty(value = "汇总:已处理数量")
    private int finished;
}
