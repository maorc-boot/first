package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "江西客户通客户称谓查询入参")
@Data
public class McdCustomTitleQuery {

    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

    @ApiModelProperty(value = "手机号码")
    private String attrPhone;

    @ApiModelProperty(value = "地市ID")
    private String attrCity;

    @ApiModelProperty(value = "开始日期")
    private String attrStartTime;

    @ApiModelProperty(value = "结束日期")
    private String attrEndTime;

    @ApiModelProperty(value = "上传人")
    private String createUserName;

}
