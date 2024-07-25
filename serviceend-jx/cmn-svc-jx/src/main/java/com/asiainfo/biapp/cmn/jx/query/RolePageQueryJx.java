package com.asiainfo.biapp.cmn.jx.query;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/12/13
 */
@Data
@ApiModel(value = "角色分页查询入参", description = "角色分页查询入参")
public class RolePageQueryJx  extends McdPageQuery {

    @ApiModelProperty(value = "部门ID")
    private String deptId;
}
