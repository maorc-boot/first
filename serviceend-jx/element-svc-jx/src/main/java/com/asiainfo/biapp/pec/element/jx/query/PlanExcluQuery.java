package com.asiainfo.biapp.pec.element.jx.query;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author : zhouyang
 * @date : 2021-11-25 11:24:46
 */
@Data
@ApiModel(value = "主产品实体Id", description = "主产品实体Id")
public class PlanExcluQuery extends McdPageQuery {

    @NotEmpty
    @ApiModelProperty(value = "主产品id", required = true)
    private String planId;

    @ApiModelProperty("产品来源类型, 0 系统,1 CRM系统")
    private int sourceType = 0;

}
