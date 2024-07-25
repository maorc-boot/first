package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lizz9
 * @description: 客户通（hmh5）问题反馈业务的最新回复
 * @date 2021/2/2  9:37
 */
@Data
@ApiModel("问题反馈查询结果")
public class McdManagerFeedbackRecentlyReply {

    @ApiModelProperty("业务id")
    private String businessId;

    @ApiModelProperty("问题反馈者id")
    private String managerId;

    @ApiModelProperty("问题反馈类型 0-其他 1-功能异常 2-产品建议")
    private String feedbackType;//问题反馈类型 0-其他 1-功能异常 2-产品建议

    @ApiModelProperty("反馈描述")
    private String feedbackDescription;

    @ApiModelProperty("附加图片信息")
    private String pictireInfo;

    @ApiModelProperty("反馈日期")
    private String createTime;

    @ApiModelProperty("类型名称")
    private String feedbackTypeName;//类型名称

    @ApiModelProperty("经理姓名")
    private String staffName;//经理姓名

    @ApiModelProperty("经理电话号码")
    private String managerProductNo;//经理电话号码

    @ApiModelProperty("回复内容")
    private String qaContent;//回复内容

    @ApiModelProperty("回复时间")
    private String answerTime;//回复时间

    @ApiModelProperty("回复创建人")
    private String qaCreateBy;//回复创建人

    @ApiModelProperty("读的状态")
    private Integer readState;//读的状态
}
