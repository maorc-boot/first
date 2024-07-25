package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.model.McdManagerFeedback;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdManagerFeedbackResultInfo;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
public interface McdManagerFeedbackService extends IService<McdManagerFeedback> {

    McdManagerFeedbackResultInfo selectManagerFeedbackDetail(String businessId);


}
