package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo;

import lombok.Data;

import java.util.Date;

/**
 * @author lizz9
 * @description: 客户通（hmh5）问题反馈对象
 *
 * @date 2021/2/2  9:39
 */
@Data
public class McdManagerFeedbackResultInfo {

    private String businessId;
    private String managerId;
    private String feedbackType;//问题反馈类型 0-其他 1-功能异常 2-产品建议
    private String feedbackDescription;
    private String pictireInfo;
    private Date createTime;
}
