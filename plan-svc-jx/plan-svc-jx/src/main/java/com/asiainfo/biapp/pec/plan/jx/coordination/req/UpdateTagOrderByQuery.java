package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 产品配置修改优先级入参对象
 *
 * @author: lvchaochao
 * @date: 2023/7/24
 */
@Data
@ApiModel("标签配置修改入参对象")
public class UpdateTagOrderByQuery {

    @ApiModelProperty("标签编码")
    private String tagId;

    @ApiModelProperty("优先级")
    private Integer orderBy;
}
