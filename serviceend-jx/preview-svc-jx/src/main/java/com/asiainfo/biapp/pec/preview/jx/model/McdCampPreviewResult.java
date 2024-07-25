package com.asiainfo.biapp.pec.preview.jx.model;

import com.asiainfo.biapp.pec.preview.jx.entity.McdCampPreviewLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/10/28
 */

@Data
@ApiModel(value = "执行前预演结果", description = "执行前预演结果")
public class McdCampPreviewResult extends McdCampPreviewLog {

    @ApiModelProperty(value = "客户群名称")
    private String custgroupName;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "产品名称")
    private String planName;
    @ApiModelProperty(value = "产品ID")
    private String planId;
    @ApiModelProperty(value = "预演总剔除数")
    private Integer filterTotal;

    @ApiModelProperty(value = "客户群准确度")
    private String custAccuracyRate ;
    @ApiModelProperty(value = "产品准确度")
    private String planAccuracyRate ;
    @ApiModelProperty(value = "触点覆盖度")
    private String contactCoverage ;
    @ApiModelProperty(value = "转换率预测")
    private String sucessRate ;
}
