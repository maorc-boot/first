package com.asiainfo.biapp.pec.plan.jx.link.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("江西短链接删除入参")
public class McdDeleteLinkQuery {
    @ApiModelProperty("链接id")
    private String linkId;

    @ApiModelProperty("链接url")
    private String linkUrl;

}
