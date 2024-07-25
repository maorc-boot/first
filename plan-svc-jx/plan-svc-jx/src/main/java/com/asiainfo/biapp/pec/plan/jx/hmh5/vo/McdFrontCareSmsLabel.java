package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 关怀短信模板标签查询返回对象
 *
 * @author lvcc
 * @date 2024/02/26
 */
@ApiModel("关怀短信模板标签查询返回对象")
@Data
public class McdFrontCareSmsLabel {

    @ApiModelProperty("流水序列号")
    private Integer serialNum;

    @ApiModelProperty("标签名")
    private String labelName;

    @ApiModelProperty("标签编码")
    private String labelCode;

    @ApiModelProperty("标签描述")
    private String labelDesc;

    @ApiModelProperty("地市编码")
    private String cityCode;

    @ApiModelProperty("数据状态")
    private Integer dataState;

    @ApiModelProperty("数据状态名")
    private String dataStateName;

    @ApiModelProperty("标签目标表名")
    private String dataTableName;

    @ApiModelProperty("标签条件")
    private String labelCondition;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
