package com.asiainfo.biapp.pec.approve.jx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 批量审批结果
 *
 * @author mamp
 * @date 2023/9/21
 */
@Data
@ApiModel(value = "批量审批结果")
public class BatchApproveResult {

    @ApiModelProperty("成功实例")
    private List<String> success;
    @ApiModelProperty("失败实例")
    private List<String> fail;
}
