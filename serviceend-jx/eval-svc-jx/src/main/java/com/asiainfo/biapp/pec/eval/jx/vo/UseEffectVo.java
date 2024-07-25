package com.asiainfo.biapp.pec.eval.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 效果总览-日/月使用效果
 *
 * @author mamp
 * @date 2023/1/9
 */
@Data
@ApiModel(value = "效果总览:使用效果", description = "效果总览:使用效果")
public class UseEffectVo {
    @ApiModelProperty("数据周期")
    private String dataDate;
    @ApiModelProperty("地市ID")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("营销数")
    private Long campNum;
    @ApiModelProperty("营销成功数")
    private Long succNum;
    @ApiModelProperty("营销成功率")
    private Double succRate;
}
