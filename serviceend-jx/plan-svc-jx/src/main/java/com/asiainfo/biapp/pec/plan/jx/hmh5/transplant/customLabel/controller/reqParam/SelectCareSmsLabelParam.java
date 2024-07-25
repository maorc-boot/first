package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Data
@ApiModel("查询客群标签的请求参数模型")
public class SelectCareSmsLabelParam implements Serializable {

    @ApiModelProperty(value = "查询当前页，默认1", dataType = "Integer", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "查询页大小，默认10", dataType = "Integer", example = "10")
    private Integer pageSize;

    /**
     * 标签名(最长64字符)
     */
    @ApiModelProperty(value = "标签名称")
    private String labelName;

    /**
     * 标签编码(最长128字符)
     */
    @ApiModelProperty(value = "标签编号")
    private String labelCode;

    /**
     * 地市编码(最长32字符)
     */
    @ApiModelProperty(value = "城市代号")
    private String cityCode;

    /**
     * 标签目标表名(最长64字符)
     */
    @ApiModelProperty(value = "标签目标表名")
    private String dataTableName;



    public Integer getPageNum() {
        return pageNum != null && pageNum > 0 ? pageNum : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}
