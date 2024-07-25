package com.asiainfo.biapp.cmn.jx.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ranpf
 * @date 2023-1-16
 */
@Data
@ApiModel(value = "江西根据地市查询部门", description = "根据地市查询部门")
public class DepartmentJxQuery  {
    @ApiModelProperty(value = "地市ID")
    private String cityId;

}
