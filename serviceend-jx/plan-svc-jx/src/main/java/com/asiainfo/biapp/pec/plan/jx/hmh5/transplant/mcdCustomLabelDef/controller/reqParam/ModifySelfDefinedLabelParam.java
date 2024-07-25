package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam
 * @className: SelfDefinedLabelParam
 * @author: chenlin
 * @description: 修改客户通自定义标签参数
 * @date: 2023/6/26 14:25
 * @version: 1.0
 */
@Data
@ApiModel("修改客户通自定义标签参数")
public class ModifySelfDefinedLabelParam {

    @ApiModelProperty(value = "要修改标签的customLabelId")
    @NotNull(message = "customLabelId值不可为空！")
    private String customLabelId;

    @ApiModelProperty(value = "修改后的客群规则",example = "阿巴，阿巴阿巴")
    @NotBlank(message = "客群规则不可为空")
    @Length(min = 1,max = 20,message = "客群规则需在1-20个字符之间！")
    private String customLabelDes;
}
