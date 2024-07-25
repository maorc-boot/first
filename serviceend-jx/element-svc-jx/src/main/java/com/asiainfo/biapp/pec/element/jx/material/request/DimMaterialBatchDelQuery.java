package com.asiainfo.biapp.pec.element.jx.material.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * Description : 素材分页(或根据条件)查询
 * </p>
 *
 * @author : wuhq6
 * @date : Created in 2021/12/2  16:34
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "江西素材批量删除入参",description = "素材批量删除入参")
public class DimMaterialBatchDelQuery {

    /**
     * 素材ID集合
     */
    @ApiModelProperty(value = "素材ID集合")
    private List<String> materialIds;

}
