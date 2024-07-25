package com.asiainfo.biapp.pec.approve.jx.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批量审批状态原因
 */
@Data
@ApiModel(value = "江西批量审批-批量审批状态原因")
public class McdBatchApproveTaskPO {

    @ApiModelProperty("审核状态  1 审核通过 0 驳回")
    private Integer batchApproveStatus;

    @ApiModelProperty("审批意见")
    private String batchApproveReason;
}
