package com.asiainfo.biapp.pec.approve.jx.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 广点通渠道素材审核回调入参对象
 *
 * @author: lvchaochao
 * @date: 2023/8/29
 */
@Data
@ApiModel("广点通渠道素材审核回调入参对象")
public class MaterialAuditFallBackVo {

    @ApiModelProperty("素材编码")
    @JsonProperty("Material_ID")
    private String materialId;

    @ApiModelProperty("审核状态  0审核通过【活动正常执行】 1审核不通过【活动退回，只能重新发起活动，素材ID必须每次提交必须不同】")
    @JsonProperty("MainAuditStatus")
    private String mainAuditStatus;

    @ApiModelProperty("失败原因 在不通过时会有审核描述信息（审核不通过、拉取文件失败、文件类型错误、素材上限、文件大小超标等")
    @JsonProperty("Failed_Reason")
    private String failedReason;
}
