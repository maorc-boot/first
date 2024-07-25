package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo
 * @className: SelfDefinedLabelResultInfo
 * @author: chenlin
 * @description: 客户通自定义标签配置页面查询标签结果
 * @date: 2023/6/26 15:58
 * @version: 1.0
 */
@Data
@ApiModel("客户通自定义标签配置页面查询标签结果")
public class SelfDefinedLabelResultInfo {

    @ApiModelProperty(value = "主键自增id，配置完成后点击确认时传入")
    private Integer labelId;

    @ApiModelProperty(value = "标签ID")
    private String customLabelId;

    @ApiModelProperty(value = "标签名")
    private String customLabelName;

    @ApiModelProperty(value = "所属模块，无效字段，仅便于查询结果")
    private String moduleName;

}
