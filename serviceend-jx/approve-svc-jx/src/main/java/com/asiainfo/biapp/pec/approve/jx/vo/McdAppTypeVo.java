package com.asiainfo.biapp.pec.approve.jx.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 *
 * @author ranpf
 * @since 2023-6-12
 */
@Data
@Accessors(chain = true)
@ApiModel(value="McdAppTypeVo对象", description="McdAppTypeVo对象")
public class McdAppTypeVo   {

    @ApiModelProperty(value = "枚举键")
    private String enumKey;

    @ApiModelProperty(value = "枚举值")
    private String enumValue;

    @ApiModelProperty(value = "顺序")
    private Integer enumOrder;


}
