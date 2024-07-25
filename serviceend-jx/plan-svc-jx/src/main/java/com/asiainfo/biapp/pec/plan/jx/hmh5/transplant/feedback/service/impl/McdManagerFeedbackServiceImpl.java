package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.mapper.McdManagerFeedbackMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.model.McdManagerFeedback;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.McdManagerFeedbackService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdManagerFeedbackResultInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Service
public class McdManagerFeedbackServiceImpl extends ServiceImpl<McdManagerFeedbackMapper, McdManagerFeedback> implements McdManagerFeedbackService {
    @Override
    public McdManagerFeedbackResultInfo selectManagerFeedbackDetail(String businessId) {
        McdManagerFeedback mcdManagerFeedback = baseMapper.selectOne(new QueryWrapper<McdManagerFeedback>()
                .eq("BUSINESS_ID", businessId)
        );
        if (mcdManagerFeedback == null) return null;
        return BeanUtil.copyProperties(mcdManagerFeedback, McdManagerFeedbackResultInfo.class);
    }
}
