package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 智推棱镜：我的模板列表信息
 *
 * @author: lvchaochao
 * @date: 2024/4/22
 */
@Data
@ApiModel("我的模板列表信息")
public class TemplateListInfoVO extends TemplateBaseInfo{

    @ApiModelProperty(value = "引用次数")
    private Integer refCount;
}
