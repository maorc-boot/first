package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 保存或修改短信模板与标签关系入参
 *
 * @author: lvchaochao
 * @date: 2024/2/26
 */
@Data
@ApiModel("保存或修改短信模板与标签关系入参对象")
public class SaveOrUpdateRelaParam {

    @ApiModelProperty("模板编号")
    private String smsTemplateCode;

    @ApiModelProperty("标签编号 多个英文逗号分割")
    private String labelCodes;
}
