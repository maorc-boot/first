package com.asiainfo.biapp.pec.plan.jx.coordination.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 策略统筹-产品分类表实体
 *
 * @author: lvchaochao
 * @date: 2023/7/24
 */
@Data
@ApiModel("策略统筹-产品分类表实体对象")
public class McdCoordinatePlanTypeVo {

    @ApiModelProperty("产品分类ID")
    private String typeId;

    @ApiModelProperty("分类名称")
    private String typeName;

    @ApiModelProperty("分类排序")
    private String orderBy;

    @ApiModelProperty("状态 1-可用,0-不可用")
    private String status;
}
