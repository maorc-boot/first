package com.asiainfo.biapp.pec.eval.jx.vo;

import com.asiainfo.biapp.pec.core.enums.TransPropEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/1/10
 */
@ApiModel("活动评估结果")
@Data
public class CampEvalVo {
    @ApiModelProperty("活动开始时间")
    private String startDate;
    @ApiModelProperty("活动结束时间")
    private String endDate;
    @ApiModelProperty("活动类型")
    @TransPropEnum(type = "activity_type", keyCol = "campsegTypeName")
    private String campsegTypeId;
    @ApiModelProperty("活动类型名称")
    private String campsegTypeName;
    @ApiModelProperty("活动ID")
    private String campsegRootId;
    @ApiModelProperty("活动名称")
    private String campsegName;
    @ApiModelProperty("产品类型")
    private String planTypeId;
    @ApiModelProperty("产品类型名称")
    private String planTypeName;
    @ApiModelProperty("产品名称")
    private String planName;
    @ApiModelProperty("地市ID")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("区县Id")
    private String countyId;
    @ApiModelProperty("区县名称")
    private String countyName;
    @ApiModelProperty("渠道ID")
    private String channelId;
    @ApiModelProperty("渠道名称")
    private String channelName;
    @ApiModelProperty("总用户数")
    private Long totalNum;
    @ApiModelProperty("接触用户数")
    private Long contactNum;
    @ApiModelProperty("接触率")
    private Double contactRate;
    @ApiModelProperty("成功用户数")
    private Long successNum;
    @ApiModelProperty("成功率")
    private Double successRate;

}
