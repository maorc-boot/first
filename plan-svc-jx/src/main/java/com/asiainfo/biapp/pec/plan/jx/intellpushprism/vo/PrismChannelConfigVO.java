package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 智推棱镜渠道默认配置信息返回实体对象
 *
 * @author: lvchaochao
 * @date: 2024/7/8
 */
@ApiModel("智推棱镜渠道默认配置信息返回实体对象")
@Data
public class PrismChannelConfigVO {

    @ApiModelProperty(value = "字段1")
    private String columnExt1;

    @ApiModelProperty(value = "字段2")
    private String columnExt2;

    @ApiModelProperty(value = "字段3")
    private String columnExt3;

    @ApiModelProperty(value = "字段4")
    private String columnExt4;

    @ApiModelProperty(value = "字段5")
    private String columnExt5;

    @ApiModelProperty(value = "字段6")
    private String columnExt6;

    @ApiModelProperty(value = "字段7")
    private String columnExt7;

    @ApiModelProperty(value = "字段8")
    private String columnExt8;

    @ApiModelProperty(value = "字段9")
    private String columnExt9;

    @ApiModelProperty(value = "字段10")
    private String columnExt10;

    @ApiModelProperty(value = "字段11")
    private String columnExt11;

    @ApiModelProperty(value = "字段12")
    private String columnExt12;

    @ApiModelProperty(value = "字段13")
    private String columnExt13;

    @ApiModelProperty(value = "字段14")
    private String columnExt14;

    @ApiModelProperty(value = "字段15")
    private String columnExt15;

    @ApiModelProperty(value = "字段16")
    private String columnExt16;

    @ApiModelProperty(value = "字段17")
    private String columnExt17;

    @ApiModelProperty(value = "字段18")
    private String columnExt18;

    @ApiModelProperty(value = "字段19")
    private String columnExt19;

    @ApiModelProperty(value = "字段20")
    private String columnExt20;

    @ApiModelProperty(value = "活动根id")
    @JsonIgnore
    private String campsegId;

    @ApiModelProperty(value = "产品id")
    @JsonIgnore
    private String planId;

    @ApiModelProperty(value = "渠道id")
    @JsonIgnore
    private String channelId;

    @ApiModelProperty(value = "运营位id")
    private String adivId;

    @ApiModelProperty(value = "运营位名称")
    private String adivName;

    @ApiModelProperty(value = "营销话术")
    private String execContent;

    @ApiModelProperty(value = "频次控制参数，格式：几天_几次")
    private String frequency;

    public PrismChannelConfigVO(String columnExt1) {
        this.columnExt1 = columnExt1;
    }
}
