package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : ranpf
 * @date : Created in 2023-1-5
 */

@Data
@ApiModel(value = "江西预览图片和文字响应")
public class MaterialLoadImageJxResponse {
    
    @ApiModelProperty(value = "素材类型0：图片1：文字2：视频", required = true, dataType = "integer")
    private Integer materialType;

    @ApiModelProperty(value = "文件存放地址")
    private String resourceUrl;
}
