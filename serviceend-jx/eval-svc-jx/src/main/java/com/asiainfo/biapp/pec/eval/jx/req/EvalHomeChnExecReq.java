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
@ApiModel(value = "评估-效果总览-渠道执行情况请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvalHomeChnExecReq {

    @ApiModelProperty("当前月份:yyyy-MM")
    private String currentMonth;

    private String startMonth;

    private String endMonth;
}
