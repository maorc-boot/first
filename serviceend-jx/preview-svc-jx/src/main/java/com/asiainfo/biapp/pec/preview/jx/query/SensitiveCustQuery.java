package com.asiainfo.biapp.pec.preview.jx.query;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/10/12
 */
@Data
@ApiModel("渠道敏感客户群分页查询")
public class SensitiveCustQuery extends McdPageQuery {
    @ApiModelProperty("渠道ID")
    private String channelId;

    @ApiModelProperty("敏感客群类型")
    private String custgroupType;

}
