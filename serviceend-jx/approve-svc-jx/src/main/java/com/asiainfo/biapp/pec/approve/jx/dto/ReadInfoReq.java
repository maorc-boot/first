package com.asiainfo.biapp.pec.approve.jx.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/12/6
 */
@Data
public class ReadInfoReq {
    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "业务名称或者编码")
    private String businessKey;

    @ApiModelProperty(value = "提交人")
    private String submitter;

    @ApiModelProperty(value = "状态,0:待阅,1:已阅,不传默认为0")
    private Integer status = 0;

    private String loginUserId;

}
