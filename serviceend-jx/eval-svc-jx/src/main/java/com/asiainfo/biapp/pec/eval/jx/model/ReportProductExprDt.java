package com.asiainfo.biapp.pec.eval.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * description: 产品分类订购报表实体
 *
 * @author: lvchaochao
 * @date: 2023/2/2
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("REPORT_CHANNEL_EXPR_DT")
@ApiModel(value = "产品分类订购报表实体对象", description = "REPORT_CHANNEL_EXPR_DT表")
public class ReportProductExprDt {

    @ApiModelProperty(value = "操作时间")
    @TableField("op_time")
    private String opTime;

    @ApiModelProperty(value = "地市id")
    @TableField("city_id")
    private String cityId;

    @ApiModelProperty(value = "地市名")
    @TableField("city_name")
    private String cityName;

    @ApiModelProperty(value = "产品类型")
    @TableField("plan_type")
    private String planType;

    @ApiModelProperty(value = "当天推荐")
    @TableField("tuijian_ds")
    private String tuijianDs;

    @ApiModelProperty(value = "当天立即办理")
    @TableField("ljbl_ds")
    private String ljblDs;

    @ApiModelProperty(value = "当天需要考虑")
    @TableField("xykl_ds")
    private String xyklDs;

    @ApiModelProperty(value = "当天暂时拒绝")
    @TableField("zsjj_ds")
    private String zsjjDs;

    @ApiModelProperty(value = "当天是否办理")
    @TableField("succ_ds")
    private String succDs;

    @ApiModelProperty(value = "当月推荐")
    @TableField("tuijian_dt")
    private String tuijianDt;

    @ApiModelProperty(value = "当月立即办理")
    @TableField("ljbl_dt")
    private String ljblDt;

    @ApiModelProperty(value = "当月需要考虑")
    @TableField("xykl_dt")
    private String xyklDt;

    @ApiModelProperty(value = "当月暂时拒绝")
    @TableField("zsjj_dt")
    private String zsjjDt;

    @ApiModelProperty(value = "当月是否办理")
    @TableField("succ_dt")
    private String succDt;
}
