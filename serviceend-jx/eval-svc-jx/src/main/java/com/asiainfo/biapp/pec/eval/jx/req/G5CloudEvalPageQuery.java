package com.asiainfo.biapp.pec.eval.jx.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 5G云卡效果评估请求入参对象
 *
 * @author lvcc
 * @date 2023/10/12
 */
@ApiModel("5G云卡效果评估请求入参对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class G5CloudEvalPageQuery extends McdPageQuery {

    @ApiModelProperty("地市ID 明细== -1 汇总== 0")
    private String cityId;

    @ApiModelProperty("区县 明细== -1 汇总== 0")
    private String countyId;

    @ApiModelProperty("回调类型 明细== -1 汇总== 0")
    private String callbackType;

}
