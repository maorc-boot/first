package com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup;

import com.asiainfo.biapp.pec.plan.model.McdCustgroupDef;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 客户群详情实体
 *
 * @author lvcc
 * @date 2023/12/12
 */
@Data
@ApiModel(value = "客户群详情实体", description = "客户群详情实体")
public class CustgroupDetailVO extends McdCustgroupDef {

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "是否有属性，有，无")
    private String hasCustomAttrs;

    @ApiModelProperty(value = "属性列表")
    private List<String> customAttrs;

    @ApiModelProperty(value = "更新周期中文描述")
    private String updateCycleDesc;

    @ApiModelProperty(value = "属性列表")
    private String statusDesc;
}
