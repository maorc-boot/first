package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.AddNewPictureReplyMcdFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.AddNewReplyMcdFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.SelectManagerFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.mapper.McdFeedbackQaMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.model.McdFeedbackQa;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.McdFeedbackQaService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdFeedbackQaResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdManagerFeedbackRecentlyReply;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils.CustomPictureStorageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Service
public class McdFeedbackQaServiceImpl extends ServiceImpl<McdFeedbackQaMapper, McdFeedbackQa> implements McdFeedbackQaService {

    @Autowired
    private CustomPictureStorageUtil pictureStorageUtil;

    private final String LAST_FOLDER = "feedback";
    @Override
    public Page<McdManagerFeedbackRecentlyReply> getRecentlyReplyFeedback(SelectManagerFeedbackParam feedbackVO) {
        return baseMapper.getRecentlyReplyFeedback(
                new Page<>(feedbackVO.getPageNum(), feedbackVO.getPageSize()),
                feedbackVO
        );
    }

    @Override
    public Page<McdFeedbackQaResultInfo> getAllRepliesByBusinessId(Integer pageNum, Integer pageSize, String businessId) {
        Page allReplies = baseMapper.selectPage(
                new Page(pageNum, pageSize),
                new QueryWrapper<McdFeedbackQa>().eq("BUSINESS_ID", businessId)
        );
        return allReplies.setRecords(BeanUtil.copyToList(allReplies.getRecords(), McdFeedbackQaResultInfo.class));
    }

    @Override
    public void addNewReply(UserSimpleInfo user, AddNewReplyMcdFeedbackParam newReplyMcdFeedbackParam) {
        McdFeedbackQa mcdFeedbackQa = BeanUtil.copyProperties(newReplyMcdFeedbackParam, McdFeedbackQa.class);
        mcdFeedbackQa.setQaMsgType(0);
        mcdFeedbackQa.setSerialNumber(String.valueOf(System.currentTimeMillis()));
        mcdFeedbackQa.setState(0);
        mcdFeedbackQa.setCreateBy(user.getUserId());
        // mcdFeedbackQa.setCreateUsername(user.getUserName());
        mcdFeedbackQa.setCreateTime(new Date());
        save(mcdFeedbackQa);
    }

    @Override
    public void addNewReplyPicture(UserSimpleInfo user, AddNewPictureReplyMcdFeedbackParam newReplyMcdFeedbackParam) throws Exception {
        Map<String, String> uploadResult = pictureStorageUtil.uploadImage(LAST_FOLDER, newReplyMcdFeedbackParam.getPictureFile());
        if ("0".equals(uploadResult.get(CustomPictureStorageUtil.STATUS)))
            throw new BaseException(uploadResult.get(CustomPictureStorageUtil.ERROR));

        McdFeedbackQa mcdFeedbackQa = new McdFeedbackQa();

        mcdFeedbackQa.setSerialNumber(String.valueOf(System.currentTimeMillis()));
        mcdFeedbackQa.setBusinessId(newReplyMcdFeedbackParam.getBusinessId());
        mcdFeedbackQa.setQaContent(uploadResult.get(CustomPictureStorageUtil.UPLOADRESULT));
        mcdFeedbackQa.setQaMsgType(1);
        mcdFeedbackQa.setState(0);
        mcdFeedbackQa.setCreateBy(user.getUserId());
        // mcdFeedbackQa.setCreateUsername(user.getUserName());
        mcdFeedbackQa.setCreateTime(new Date());
        save(mcdFeedbackQa);
    }

    @Override
    public void downloadImage(String pictureName, HttpServletResponse response) {
        pictureStorageUtil.downloadImage(LAST_FOLDER,pictureName,response);
    }

}
