package com.asiainfo.biapp.pec.approve.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 同步活动信息到DNA侧接口响应实体
 *
 * @author lvcc
 * @date 2023/12/13
 */
@Data
@ApiModel(value = "同步活动信息到DNA侧接口响应实体", description = "同步活动信息到DNA侧接口响应实体")
public class DnaResponseDataVO {

    @ApiModelProperty(value = "返回编码（9000-成功 其他为失败）")
    private String code;

    @ApiModelProperty(value = "请求编号（uuid）")
    private String requestNo;

    @ApiModelProperty(value = "返回消息内容")
    private String msg;

    @ApiModelProperty(value = "响应成功/失败标志")
    private boolean success;
}
