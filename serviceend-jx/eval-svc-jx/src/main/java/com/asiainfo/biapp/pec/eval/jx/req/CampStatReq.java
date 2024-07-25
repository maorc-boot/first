package com.asiainfo.biapp.pec.eval.jx.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mamp
 * @date 2023/5/8
 */
@ApiModel(value = "评估-效果总览-策略统计")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampStatReq {
    @ApiModelProperty("地市ID")
    private String cityId;
    @ApiModelProperty("策略ID,多个用英文逗号分隔")
    private String campsegIds;
    @ApiModelProperty("开始时间:yyyy-MM-dd")
    private String startDate;
    @ApiModelProperty("结束时间:yyyy-MM-dd")
    private String endDate;

    private List<String> campsegIdList;
}
