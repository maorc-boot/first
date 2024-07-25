package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo;

import lombok.Data;

import java.util.Date;

/**
 * @author lizz9
 * @description: 问题反馈记录对象
 * @date 2021/2/2  9:38
 */
@Data
public class McdFeedbackQaResultInfo {

    private String businessId;
    private String qaContent;
    private String qaMsgType;
    private String createBy;
    private String createUsername; //创建人的姓名
    private Date createTime;
    private Integer state;
}
