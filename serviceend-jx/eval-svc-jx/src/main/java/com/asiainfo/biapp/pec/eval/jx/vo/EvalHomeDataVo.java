package com.asiainfo.biapp.pec.eval.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mamp
 * @date 2023/5/6
 */

@ApiModel("首页评估数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvalHomeDataVo {

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("值")
    private String value;
}
