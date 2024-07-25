package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Description : 素材预览接口入参
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-5
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "江西:素材预览入参",description = "素材预览入参")
public class DimMaterialPreviewRequest {
    
    @ApiModelProperty(value = "素材ID")
    private String materialId;
    
}
