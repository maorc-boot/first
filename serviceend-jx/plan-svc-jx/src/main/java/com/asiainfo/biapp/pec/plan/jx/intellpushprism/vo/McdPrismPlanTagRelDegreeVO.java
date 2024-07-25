package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 智推棱镜AI推理场景产品标签关联度对象实体
 *
 * @author: lvchaochao
 * @date: 2024/6/17
 */
@ApiModel("智推棱镜AI推理场景产品标签关联度对象实体")
@Data
public class McdPrismPlanTagRelDegreeVO {

    @ApiModelProperty("数据日期 yyyyMMdd")
    private String dataDate;

    @ApiModelProperty("产品id")
    private String planId;

    @ApiModelProperty("标签ID")
    private String tagId;

    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("标签关联度 保留四位小数")
    private Double correlation;
}
