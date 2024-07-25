package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@ApiModel("修改客群标签的请求参数模型")
public class ModifyCareSmsLabelParam implements Serializable {
    /**
     * 流水序列号（改为int，且自增）
     */
    @ApiModelProperty(value = "客群标签的序列号")
    @NotNull(message = "序列号不能为空！")
    private Integer serialNum;
    /**
     * 标签名(最长64字符)
     */
    @ApiModelProperty(value = "标签名(最长64字符)")
    @Length(max = 64,message = "标签名过长！")
    @NotBlank(message = "标签名不能为空！")
    private String labelName;

    /**
     * 标签编码(最长128字符)
     */
    @ApiModelProperty(value = "标签编码(最长128字符)")
    @Length(max = 128,message = "标签编码过长！")
    @NotBlank(message = "标签编码不能为空！")
    private String labelCode;

    /**
     * 标签描述(最长128字符)
     */
    @ApiModelProperty(value = "标签描述(最长128字符)")
    @Length(max = 128,message = "标签描述过长！")
    @NotBlank(message = "标签描述不能为空！")
    private String labelDesc;

    /**
     * 地市编码(最长32字符)
     */
    //todo 应当不为空，但之前版本默认的是空，搁置
    @ApiModelProperty(value = "地市编码(最长32字符)")
    @Length(max = 128,message = "地市编码过长！")
    //@NotBlank(message = "地市编码不能为空！")
    private String cityCode;

    /**
     * 标签目标表名(最长64字符)
     */
    @ApiModelProperty(value = "标签目标表名(最长64字符)")
    @Length(max = 64,message = "表名称过长！")
    @NotBlank(message = "表名称不能为空！")
    private String dataTableName;

    /**
     * 标签条件(最长500字符)
     */
    @ApiModelProperty(value = "客群标签的生成条件(JSON字符串，最长500字符)")
    @Length(max = 500,message = "条件字符过长！")
    @NotBlank(message = "条件字符不能为空！")
    private String labelCondition;
}
