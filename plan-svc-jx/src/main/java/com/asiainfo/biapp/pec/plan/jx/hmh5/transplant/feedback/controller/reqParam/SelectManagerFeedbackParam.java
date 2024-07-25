package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author lizz9
 * @description: 客户通（hmh5）问题反馈入参
 * @date 2021/2/2  9:52
 */
@Data
@ApiModel("问题反馈条件查询的参数模型")
public class SelectManagerFeedbackParam implements Serializable {
    //新增参数 2023-05-29 16:18:41
    @ApiModelProperty(value = "查询当前页，默认1", dataType = "Integer", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "查询页大小，默认10", dataType = "Integer", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "问题反馈的businessId，默认null", dataType = "String", example = "123456788")
    private String businessId;
    @ApiModelProperty(value = "问题反馈人的工号，默认null", dataType = "String", example = "0052")
    private String managerId;   //此处解释为工号
    @ApiModelProperty(value = "问题反馈类型 0-其他 1-功能异常 2-产品建议，默认null", dataType = "String", example = "0")
    private String feedbackType;//问题反馈类型 0-其他 1-功能异常 2-产品建议
    @ApiModelProperty(value = "查询创建时间在参数时间后的问题，默认null", dataType = "DateTime", example = "2021-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTimeStart;
    @ApiModelProperty(value = "查询创建时间在参数时间前的问题，默认null", dataType = "DateTime", example = "2024-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTimeEnd;
    @ApiModelProperty(value = "问题反馈人的姓名，默认null", dataType = "String", example = "兰兵")
    private String staffName;//经理姓名
    @ApiModelProperty(value = "问题反馈人的电话，默认null", dataType = "String", example = "19898808852")
    private String managerProductNo;//经理电话号码
    @ApiModelProperty(value = "查询最新回复时间在参数时间后的回复，默认null", dataType = "DateTime", example = "2021-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String answerTimeStart;//回复时间
    @ApiModelProperty(value = "查询最新回复时间在参数时间前的回复，默认null", dataType = "DateTime", example = "2024-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String answerTimeEnd;//回复时间

    public Integer getPageNum() {
        return pageNum != null && pageNum > 0 ? pageNum : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }

    //以下被注释的是前端没有请求的数据  2023-05-29 16:18:41
    //private String feedbackDescription;
    //private String pictireInfo;
    //private String createTime;
    //private String qaContent;//回复内容
    //private String answerTime;//回复时间
    //private String qaCreateBy;//回复创建人
    //private Integer readState;//读的状态


}
