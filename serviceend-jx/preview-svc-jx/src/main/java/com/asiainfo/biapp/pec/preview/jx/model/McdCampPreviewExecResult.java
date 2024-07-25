package com.asiainfo.biapp.pec.preview.jx.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mamp
 * @date 2022/10/28
 */

@Data
@ApiModel(value = "江西:执行中预演结果", description = "江西:执行中预演结果")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class McdCampPreviewExecResult {

    @ApiModelProperty(value = "活动ID")
    private String campsegId;
    @ApiModelProperty(value = "渠道ID")
    private String channelId;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "产品名称")
    private String planName;
    @ApiModelProperty(value = "波次")
    private String waveNo;
    @ApiModelProperty(value = "过滤规则名称")
    private String ruleName;
    @ApiModelProperty(value = "过滤数量")
    private String filterNum;
    @ApiModelProperty(value = "预演用户数")
    private String previewUserNum;
    @ApiModelProperty(value = "备注")
    private String comment;
}
