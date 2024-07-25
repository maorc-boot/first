package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lizz9
 * @description: 问题反馈回复记录
 * @date 2021/2/2  9:57
 */
@Data
public class AddNewPictureReplyMcdFeedbackParam implements Serializable {
    @ApiModelProperty(value = "问题反馈的businessId，必填",required = true, dataType = "String", example = "123456788")
    @NotBlank(message = "业务id不能为空！")
    private String businessId;

    @ApiModelProperty(value = "图片回复，需选择图片文件，格式只能是png|jpg|jpeg",required = true)
    @NotNull(message = "请先选择图片文件，格式为：png|jpg|jpeg！")
    private MultipartFile pictureFile;
}
