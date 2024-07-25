package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@TableName(value = "sjx_customer_model")
@ApiModel(value = "江西大屏数据对象",description = "江西大屏数据对象")
public class McdSjxCustomerModel {

    //模型编号
    @ApiModelProperty("模型编号")
    @TableField("MODEL_ID")
    private String modelId;

    //模型名称
    @ApiModelProperty("模型名称")
    @TableField("MODEL_NAME")
    private String modelName;


    //客户量
    @ApiModelProperty("客户量")
    @TableField("CUSTOMER_NUMBER")
    private int customerNumber;



    //策略数
    @ApiModelProperty("策略数")
    @TableField("STRATEGY_NUMBER")
    private int stratgyNumber;

    //标签数
    @ApiModelProperty("标签数")
    @TableField("LABEL_NUMBER")
    private int labelNumber;

    //查准率
    @ApiModelProperty("查准率")
    @TableField("PRECISION_RATE")
    private double precisionRate;

    //查全率
    @ApiModelProperty("查全率")
    @TableField("RECALL_RATE")
    private double recallRate;

    //月营销次数
    @ApiModelProperty("月营销次数")
    @TableField("MARKETING_NUMBER")
    private int marketingNumber;

    //月营销成功数
    @ApiModelProperty("月营销成功数")
    @TableField("SUCCESS_MARKETING_NUMBER")
    private int successMarketingNumber;

    //月份
    @ApiModelProperty("月份")
    @TableField("MONTH_TIME")
    private String monthTime;


    //转化率CONVERSION_RATE
    @ApiModelProperty("转化率CONVERSION_RATE")
    @TableField("CONVERSION_RATE")
    private double conversionRate;
}
