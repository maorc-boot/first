package com.asiainfo.biapp.pec.preview.jx.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ranpf
 * @date 2023-02-03
 */
@Data
@ApiModel("渠道敏感客户群入参")
public class SensitiveCustDeleReq {

    @ApiModelProperty("敏感客群Id")
    private String custgroupId;

}
