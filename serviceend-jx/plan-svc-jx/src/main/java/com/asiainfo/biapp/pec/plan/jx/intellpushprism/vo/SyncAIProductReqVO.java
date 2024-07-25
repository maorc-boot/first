package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description: 智慧大脑产品同步IOP入参
 *
 * @author: lvchaochao
 * @date: 2024/5/25
 */
@Data
@ApiModel("智慧大脑产品同步IOP入参")
public class SyncAIProductReqVO {

    @ApiModelProperty("新增产品列表")
    private List<String> addProducts;

    @ApiModelProperty("清除产品列表")
    private List<String> delProducts;
}
