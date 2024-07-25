package com.asiainfo.biapp.cmn.jx.query;

import com.asiainfo.biapp.cmn.query.SysEnumTypeQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/1/3
 */
@Data
@ApiModel(value = "通过枚举类型查询枚举列表", description = "通过枚举类型查询枚举列表")
public class SysEnumTypeQueryJx extends SysEnumTypeQuery {
    @ApiModelProperty(value = "枚举父ID", required = true)
    private String parentId;
}
