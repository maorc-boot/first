package com.asiainfo.biapp.pec.plan.jx.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("江西:系统使用情况-部门")
public class SystemUsageDeptDTO extends SystemUsage implements Serializable {

    @ApiModelProperty("部门ID")
    private String deptId;

   @ApiModelProperty("部门名称")
    private String deptName;


}
