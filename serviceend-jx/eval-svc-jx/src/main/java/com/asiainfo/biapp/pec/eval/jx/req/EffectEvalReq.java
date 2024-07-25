package com.asiainfo.biapp.pec.eval.jx.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 营销效果评估入参对象
 *
 * @author: lvchaochao
 * @date: 2023/1/12
 */
@Data
@ApiModel("营销效果评估入参对象")
public class EffectEvalReq {

    @ApiModelProperty("日期类型 day:日 week:周 month:月")
    private String dateType;

    @ApiModelProperty("地市ID")
    private String cityId;

    @ApiModelProperty("渠道ID")
    private String channelId;

    @ApiModelProperty("产品类型")
    private String productType;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("下钻类型 time:时间下钻 province:省级下钻 city:地市下钻 productType:产品类型下钻 channel:渠道下钻 adiv:运营位下钻")
    private String drillDownType;

    @ApiModelProperty("导出类型 true:下钻导出  false:汇总导出")
    private boolean isDrillDownExport;

    @ApiModelProperty("是否查询详情 true:是  false:否")
    private boolean isDetail;

    @ApiModelProperty("分页开始")
    private Integer pageStart;

    @ApiModelProperty("分页页数")
    private Integer pageNum = 1;

    @ApiModelProperty("分页大小")
    private Integer pageSize = 10;
}
