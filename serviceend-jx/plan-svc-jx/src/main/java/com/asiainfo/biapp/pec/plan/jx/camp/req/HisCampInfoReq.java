package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/4/4
 */
@Data
@ApiModel(value = "江西:历史活动信息查询入参", description = "江西:历史活动信息查询入参")
public class HisCampInfoReq extends McdPageQuery {
    @ApiModelProperty(value = "客户群ID")
    private String custgroupId;
    @ApiModelProperty(value = "渠道ID,多个用英文逗号分隔")
    private String channelIds;
}
