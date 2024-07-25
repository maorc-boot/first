package com.asiainfo.biapp.pec.element.jx.query;

import com.asiainfo.biapp.pec.element.query.PlanManageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/10/18
 */
@Data
@ApiModel(value = "产品列表查询入参对象", description = "根据条件查询产品列表")
public class PlanManageJxQuery extends PlanManageQuery {
    /**
     * 产品业务分类
     */
    @ApiModelProperty(value = " 产品业务分类")
    private String prodClassBusi;

    @ApiModelProperty(value = " 商品类型")
    private String offerType;

    @ApiModelProperty(value = " 产商品类型")
    private String itemType;

    @ApiModelProperty(value = "是否支持AI 0-不支持 1-支持")
    private Integer isSupportAi;

}
