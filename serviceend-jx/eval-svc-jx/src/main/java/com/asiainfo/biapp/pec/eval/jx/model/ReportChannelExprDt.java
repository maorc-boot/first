package com.asiainfo.biapp.pec.eval.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description: 产品分渠道订购报表实体对象（月表）
 *
 * @author: lvchaochao
 * @date: 2023/2/2
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "产品分渠道订购报表实体对象", description = "REPORT_CHANNEL_EXPR_202201表")
public class ReportChannelExprDt {

    @ApiModelProperty(value = "操作时间")
    @TableField("op_time")
    private String opTime;

    @ApiModelProperty(value = "地市id")
    @TableField("city_id")
    private String cityId;

    @ApiModelProperty(value = "地市名")
    @TableField("city_name")
    private String cityName;

    @ApiModelProperty(value = "区县id")
    @TableField("county_id")
    private String countyId;

    @ApiModelProperty(value = "区县名称")
    @TableField("COUNTY_NAME")
    private String countyName;

    @ApiModelProperty(value = "渠道ID")
    @TableField("CHANNEL")
    private String channel;

    @ApiModelProperty(value = "操作ID")
    @TableField("OPER_ID")
    private String operId;

    @ApiModelProperty(value = "当月推荐")
    @TableField("DYTJ")
    private String dytj;

    @ApiModelProperty(value = "立即办理")
    @TableField("LJBL")
    private String ljbl;

    @ApiModelProperty(value = "需要考虑")
    @TableField("XYKL")
    private String xykl;

    @ApiModelProperty(value = "暂时拒绝")
    @TableField("ZSJJ")
    private String zsjj;

    @ApiModelProperty(value = "是否操作")
    @TableField("IS_CZ")
    private String isCz;

    @ApiModelProperty(value = "是否办理")
    @TableField("IS_BANLI")
    private String isBanLi;

    @ApiModelProperty(value = "是否该营业员操作")
    @TableField("IS_SELF_BANLI")
    private String isSelfBanLi;

}
