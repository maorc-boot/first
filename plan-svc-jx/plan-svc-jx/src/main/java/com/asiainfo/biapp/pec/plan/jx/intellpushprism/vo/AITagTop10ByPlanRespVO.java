package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * description: 获取该产品AI画像标签TOP10响应实体
 *
 * @author: lvchaochao
 * @date: 2024/6/13
 */
@ApiModel("获取该产品AI画像标签TOP10响应实体")
@Data
public class AITagTop10ByPlanRespVO {

    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("标签关联度 保留四位小数")
    private double correlation;

    @ApiModelProperty("标签值集合对象")
    private List<TagVO> labelVOList;

    @Data
    @ApiModel("标签值集合对象实体")
    public static class TagVO {

        @ApiModelProperty("标签值")
        private String tagValue;

        @ApiModelProperty("标签值中文名")
        private String tagValueCn;

        @ApiModelProperty("标签值用户数占比 保留四位小数")
        private BigDecimal proportion;
    }
}
