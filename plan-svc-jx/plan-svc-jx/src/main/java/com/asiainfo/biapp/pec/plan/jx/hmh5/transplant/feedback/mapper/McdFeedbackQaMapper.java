package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.mapper;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.SelectManagerFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.model.McdFeedbackQa;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdManagerFeedbackRecentlyReply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
public interface McdFeedbackQaMapper extends BaseMapper<McdFeedbackQa> {
    Page<McdManagerFeedbackRecentlyReply> getRecentlyReplyFeedback(Page page, @Param("feedbackVO") SelectManagerFeedbackParam feedbackVO);
}
