package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * description: 产品配置列表查询入参
 *
 * @author: lvchaochao
 * @date: 2023/7/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("产品配置列表查询入参对象")
public class PlanConfListQuery  extends McdPageQuery {

    @ApiModelProperty("产品系列类型")
    @NotBlank(message = "产品系列类型值不能为空")
    private String planType;

    @ApiModelProperty("产品业务分类")
    private String prodClassBusi;
}
