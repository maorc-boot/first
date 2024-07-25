package com.asiainfo.biapp.pec.eval.jx.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/1/9
 */
@Data
@ApiModel(value = "活动评估请求参数")
public class CampEvalReq {
    @ApiModelProperty(value = "地市ID")
    private String cityId;
    @ApiModelProperty("区县ID")
    private String countyId;
    @ApiModelProperty("渠道ID")
    private String channelId;
    @ApiModelProperty("活动类型ID")
    private String campsegType;
    @ApiModelProperty("事件ID")
    private String[] eventList;
    @ApiModelProperty("视图类型: M-月视图, W-周视图, D-日视图")
    private String veiwType;
    @ApiModelProperty("事件引用类型:0-全部, 1-已引用,2-未引用")
    private String eventType;
    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

    @ApiModelProperty(value = "搜索关键字")
    private String keyWords;

    /**
     * 数据周期开始时间
     */
    @ApiModelProperty(hidden = true)
    private String startDate;
    /**
     * 数据周期结束时间
     */
    @ApiModelProperty(hidden = true)
    private String endDate;

}
