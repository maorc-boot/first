package com.asiainfo.biapp.pec.eval.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/5/8
 */
@ApiModel("评估-效果总览-策略统计")
@Data
public class EvalHomeCampStatVO {

    @ApiModelProperty("地市ID")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("营销总数")
    private String total;
    @ApiModelProperty("营销成功数")
    private String success;
    @ApiModelProperty("营销成功率")
    private String successRate;
}
