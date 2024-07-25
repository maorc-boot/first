package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * description: DNA客群裂变接口返回的统一实体
 *
 * @author: lvchaochao
 * @date: 2024/4/22
 */
@ApiModel(value = "DNACustomActionResponse", description = "DNA客群裂变接口返回的统一实体")
public class DNACustomActionResponse<T> implements Serializable {

    @ApiModelProperty("返回码，9000-成功，其他失败")
    private String code;

    @ApiModelProperty("返回描述")
    private T data;

    @ApiModelProperty("请求唯一标识")
    private String requestNo;

    @ApiModelProperty("返回数据内容")
    private String msg;

    public String getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public String getRequestNo() {
        return requestNo;
    }
}
