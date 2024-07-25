package com.asiainfo.biapp.pec.eval.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mamp
 * @date 2023/5/8
 */
@ApiModel("评估-效果总览-渠道执行情况")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvalHomeChnExecVO {

    @ApiModelProperty("数据日期")
    private String statTime;
    @ApiModelProperty("营销总数")
    private String total;
    @ApiModelProperty("营销成功数")
    private String success;
    @ApiModelProperty("营销成功率")
    private String successRate;
}
