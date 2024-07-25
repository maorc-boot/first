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
@ApiModel("5G效果评估请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class G5EvalPageQuery extends McdPageQuery {

    @ApiModelProperty("地市ID")
    private String cityId;
    @ApiModelProperty("区县")
    private String countyId;
    @ApiModelProperty("有无设置回落")
    private String fallbackConfig;
    @ApiModelProperty("回落类型")
    private String fallbackType;

}
