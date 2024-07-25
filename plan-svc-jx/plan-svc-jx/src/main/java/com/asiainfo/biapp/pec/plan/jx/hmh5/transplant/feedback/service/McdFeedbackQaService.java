package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.AddNewPictureReplyMcdFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.AddNewReplyMcdFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.SelectManagerFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.model.McdFeedbackQa;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdFeedbackQaResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdManagerFeedbackRecentlyReply;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
public interface McdFeedbackQaService extends IService<McdFeedbackQa> {

    Page<McdManagerFeedbackRecentlyReply> getRecentlyReplyFeedback(SelectManagerFeedbackParam feedbackVO);


    Page<McdFeedbackQaResultInfo> getAllRepliesByBusinessId(Integer pageNum, Integer pageSize, String businessId);

    void addNewReply(UserSimpleInfo user, AddNewReplyMcdFeedbackParam newReplyMcdFeedbackParam);

    void addNewReplyPicture(UserSimpleInfo user, AddNewPictureReplyMcdFeedbackParam newReplyMcdFeedbackParam) throws Exception;

    void downloadImage(String pictureName, HttpServletResponse response);
}
