package com.asiainfo.biapp.pec.plan.jx.custgroup.vo;

import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefJx;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author : zhouyang
 * @date : 2021-11-25 09:11:54
 *
 */
@Data
@ApiModel(value = "客户群详情实体", description = "客户群详情实体")
public class CustgroupDetailJxVO extends McdCustgroupDefJx {

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "是否有属性，有，无")
    private String hasCustomAttrs;

    @ApiModelProperty(value = "属性列表")
    private List<String> customAttrs;
}
