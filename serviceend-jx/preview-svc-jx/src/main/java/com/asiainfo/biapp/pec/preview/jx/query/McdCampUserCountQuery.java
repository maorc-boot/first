package com.asiainfo.biapp.pec.preview.jx.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IOP营销策略用户统计任务开启请求
 *
 * @author suny3
 * @date 2024/7/22
 */
@Data
@ApiModel("IOP营销策略用户统计任务开启请求")
public class McdCampUserCountQuery {

    @ApiModelProperty("是否生成任务: 是否默认：1")
    private Integer isCreateTask = 1;
}
