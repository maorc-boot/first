package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 客群打分区间数据查询入参对象
 *
 * @author: lvchaochao
 * @date: 2024/5/25
 */
@Data
@ApiModel("客群打分区间数据查询入参对象")
public class QueryScoreRangeReqVO {

    @ApiModelProperty("产品编码")
    private String productId;

    @ApiModelProperty("营销目标")
    private String marketGoal;

    @ApiModelProperty("前置客群文件名")
    private String preCustFileName;

    @ApiModelProperty("dna规则字符串")
    private String dnaRule;

    @ApiModelProperty("渠道id")
    private String channelId;
}
