package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo
 * @className: SelectAlarmTypeResult
 * @author: chenlin
 * @description: 查询江西客户通自定义预警类别
 * @date: 2023/6/28 17:03
 * @version: 1.0
 */
@Data
@ApiModel("查询江西客户通自定义预警类别")
public class SelectAlarmTypeResult {

    // @ApiModelProperty(value = "字典KEY值")
    // private String dicKey;
    //
    // @ApiModelProperty(value = "字典值")
    // private String dicValue;
    //
    // @ApiModelProperty(value = "字典显示名称")
    // private String dicName;

    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @ApiModelProperty(value = "类型id")
    private String typeId;

}
