package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lizz9
 * @description: 问题反馈回复记录
 * @date 2021/2/2  9:57
 */
@Data
public class AddNewReplyMcdFeedbackParam implements Serializable {
    @ApiModelProperty(value = "问题反馈的businessId，必填",required = true, dataType = "String", example = "123456788")
    @NotBlank(message = "业务id不能为空！")
    private String businessId;

    @ApiModelProperty(value = "文字回复",required = true, dataType = "String", example = "哈喽哈喽！")
    @NotBlank(message = "回复内容不能为空！")
    private String qaContent;

}
