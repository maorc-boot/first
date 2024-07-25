package com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DNA方返回客群详细信息实体
 *
 * @author lvcc
 * @date 2023/12/12
 */
@Data
@ApiModel(value = "DNA方返回客群详细信息实体", description = "DNA方返回客群详细信息实体")
public class CustomerDetailRespDataVO {

    @ApiModelProperty(value = "返回编码（9000-成功 其他为失败）")
    private String code;

    @ApiModelProperty(value = "返回数据 ")
    private CustomerDetailDataVO data;

    @ApiModelProperty(value = "返回消息内容 ")
    private String msg;

    @ApiModelProperty(value = "请求唯一标识（uuid ")
    private String requestNo;

    @ApiModelProperty(value = "响应结果 ")
    private String sucess;
}
