package com.asiainfo.biapp.pec.eval.jx.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mamp
 * @date 2023/5/6
 */
@ApiModel(value = "首页评估数据请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvalHomeReq {

    @ApiModelProperty("请求类型: M-所有, D-本月")
    private String type;

    /**
     * 分组字段
     */
    @ApiModelProperty("趋势变化情况: STAT_DATE, 营销投放去向:CHANNEL_NAME,地区分布情况:CITY")
    private String groupByCol;

    /**
     * 数据开始日期
     */
    private String startDate;

    /**
     * 结束日志
     */
    private String endDate;

    /**
     * 当前登录人地市
     */
    private String cityId;
}
