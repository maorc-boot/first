package com.asiainfo.biapp.pec.eval.jx.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 厅店评估入参对象
 *
 * @author: lvchaochao
 * @date: 2023/1/16
 */
@Data
@ApiModel("厅店评估入参对象")
public class TdEvalReq {

    @ApiModelProperty("日期类型 day:日 week:周 month:月")
    private String dateType;

    @ApiModelProperty("地市ID")
    private String cityId;

    @ApiModelProperty("区县ID")
    private String countyId;

    @ApiModelProperty("渠道ID")
    private String channelId;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("分页开始")
    private Integer pageStart;

    @ApiModelProperty("分页页数")
    private Integer pageNum = 1;

    @ApiModelProperty("分页大小")
    private Integer pageSize = 10;

    @ApiModelProperty("关键词")
    private String keyWords;

}
