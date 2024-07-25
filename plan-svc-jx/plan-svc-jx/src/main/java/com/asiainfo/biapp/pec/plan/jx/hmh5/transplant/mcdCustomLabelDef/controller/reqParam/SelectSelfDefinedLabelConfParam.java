package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam
 * @className: SelfDefinedLabelParam
 * @author: chenlin
 * @description: 配置页面查询客户通自定义标签参数
 * @date: 2023/6/26 14:25
 * @version: 1.0
 */
@Data
@ApiModel("配置页面查询客户通自定义标签参数")
public class SelectSelfDefinedLabelConfParam {

    @ApiModelProperty(value = "查询当前页，默认1", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "查询页大小，默认10", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "标签名称，默认null查询全部")
    @Pattern(regexp = "\\S+", message = "标签名称不能含有空字符！")
    private String customLabelName;


    public Integer getPageNum() {
        return pageNum != null && pageNum > 0 ? pageNum : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }

}
