package com.asiainfo.biapp.pec.element.jx.material.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Description : 查询,删除运营位接口入参
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-3
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "江西:素材管理新建运营位查询接口入参", description = "素材管理新建运营位查询接口入参")
public class DimAdivInfoJxQuery {


    @ApiModelProperty(value = "渠道ID")
    private String channelId;
    
    @ApiModelProperty(value = "运营位ID")
    private String adivId;

    

}
