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
@ApiModel("分地市分策略分渠道报表请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityCampPageQuery extends McdPageQuery {

    @ApiModelProperty("数据日期:yyyyMMdd")
    private String statDate;


}
