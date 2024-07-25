package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * description: 查询AI客群瘦身分析结果响应对象实体
 *
 * @author: lvchaochao
 * @date: 2024/6/17
 */
@ApiModel("查询AI客群瘦身分析结果响应对象实体")
@Data
public class AICustLoseWeightResVO {

    @ApiModelProperty("标签的唯一标识")
    private String id;

    // @ApiModelProperty("标签id")
    // private String tagId;

    @ApiModelProperty("dna标签id")
    private String columnNum;

    @ApiModelProperty("标签名称")
    private String columnName;

    // @ApiModelProperty("标签规则")
    // private String condition;

    @ApiModelProperty("外层下拉规则 等于 不等于")
    private String outCalType;

    /**
     *  {
     *    value: '7',
     *    valueCn: '果洛1',
     *    condition: '!=',
     * }
     */
    @ApiModelProperty("标签值")
    private List<Map<String, String>> tagValue;

    // @ApiModelProperty("标签值-中文")
    // private List<String> tagValueCn;

    @ApiModelProperty("是否正向 0-否 1-是")
    private Integer isForwardDire;

    @ApiModelProperty("关联度")
    private BigDecimal correlation;

    @ApiModelProperty("是否扩展标签 0-否 1-是")
    private Integer isExtend;
}
