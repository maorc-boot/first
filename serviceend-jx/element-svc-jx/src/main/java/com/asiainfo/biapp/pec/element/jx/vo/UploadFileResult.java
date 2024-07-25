package com.asiainfo.biapp.pec.element.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/1/3
 */
@Data
@ApiModel(value = "上传文件返回结果")
public class UploadFileResult {
    @ApiModelProperty("上传状态,1-成功,0-失败")
    private int status;
    @ApiModelProperty("服务端文件名称(路径)")
    private String fileName;
    @ApiModelProperty("上传错误信息")
    private String errorMsg;
}
