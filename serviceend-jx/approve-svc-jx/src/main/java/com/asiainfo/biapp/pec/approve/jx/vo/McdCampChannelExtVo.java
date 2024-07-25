package com.asiainfo.biapp.pec.approve.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;




/**
 * <p>
 * 策略渠道相关信息
 * </p>
 *
 * @author ranpf
 * @since 2023-11-16
 */
@Data
@ApiModel(value="McdCampChannelExtVo对象", description="策略渠道扩展属性信息")
public class McdCampChannelExtVo {

    @ApiModelProperty(value = "扩展值")
    private String attrDisplayValue;

    @ApiModelProperty(value = "扩展字段名称")
    private String attrDisplayName;



}
