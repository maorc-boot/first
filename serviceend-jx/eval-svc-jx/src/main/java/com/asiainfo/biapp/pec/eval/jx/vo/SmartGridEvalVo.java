package com.asiainfo.biapp.pec.eval.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 智慧网格效果评估结果
 */
@ApiModel("智慧网格效果评估结果")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmartGridEvalVo {
    @ApiModelProperty("活动名称")
    private String campsegName;
    @ApiModelProperty("活动编号")
    private String campsegRootId;
    @ApiModelProperty("产品名称")
    private String planName;
    @ApiModelProperty("产品编号")
    private String planId;
    @ApiModelProperty("开始时间")
    private String startDate;
    @ApiModelProperty("结束时间")
    private String endDate;
    @ApiModelProperty("地市编码")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("区县编码")
    private String countyId;
    @ApiModelProperty("区县名称")
    private String countyName;
    @ApiModelProperty("网格编码")
    private String gridId;
    @ApiModelProperty("网格名称")
    private String gridName;
    @ApiModelProperty("客户数")
    private Long customerNum;
    @ApiModelProperty("接触数")
    private Long contactNum;
    @ApiModelProperty("接触率")
    private Double contactRate;
    @ApiModelProperty("营销成功数")
    private Long marketSuccessNum;
    @ApiModelProperty("营销成功率")
    private Double marketSuccessRate;
    @ApiModelProperty("接触类型")
    private String contactType;
    @ApiModelProperty("接触方式")
    private String contactMode;
    @ApiModelProperty("渠道编码")
    private String channelId;
    @ApiModelProperty("渠道名称")
    private String channelName;
    @ApiModelProperty("数据日期")
    private String dataDate;

}
