package com.asiainfo.biapp.pec.plan.jx.coordination.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description: 产品列表返回实体
 *
 * @author: lvchaochao
 * @date: 2023/7/24
 */
@ApiModel("产品列表返回实体")
@Data
public class McdCoordinatePlanModel {

    @ApiModelProperty("产品编码")
    private String planId;

    @ApiModelProperty("产品名称")
    private String planName;

    @ApiModelProperty("分类编码")
    private String typeId;

    @ApiModelProperty("分类名称")
    private String typeName;

    @ApiModelProperty("产品生效时间")
    private String startDate;

    @ApiModelProperty("产品失效时间")
    private String endDate;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("优先级")
    private String priority;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否新增 true--为该系列新增产品")
    private boolean isAdd;
}
