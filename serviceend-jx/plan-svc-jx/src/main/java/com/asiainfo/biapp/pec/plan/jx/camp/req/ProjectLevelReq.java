package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/1/3
 */
@Data
@ApiModel("项目层级查询")
public class ProjectLevelReq {


    @ApiModelProperty(value = "项目层级类型:层级2-iop_projectLevel2_id，层级3=iop_projectLevel3_id", required = true)
    private String levelType;
    @ApiModelProperty(value = "项目层级父ID", required = true)
    private String parentId;
}
