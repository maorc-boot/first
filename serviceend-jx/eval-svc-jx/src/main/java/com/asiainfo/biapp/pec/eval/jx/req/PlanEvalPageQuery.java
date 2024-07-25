package com.asiainfo.biapp.pec.eval.jx.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mamp
 * @date 2022/12/15
 */
@ApiModel("产品评估请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanEvalPageQuery extends McdPageQuery {

    @ApiModelProperty("数据日期开始,日视图:yyyyMMdd,月试图:yyyyMM")
    private String statStartDate;
    @ApiModelProperty("数据日期结束,日视图:yyyyMMdd,月试图:yyyyMM")
    private String statEndDate;
    @ApiModelProperty("渠道ID")
    private String channelId;
    @ApiModelProperty("地市ID")
    private String cityId;
    @ApiModelProperty("区县ID")
    private String countyId;
    @ApiModelProperty("视图类型，日视图:D,月视图: M")
    private String viewType;


}
