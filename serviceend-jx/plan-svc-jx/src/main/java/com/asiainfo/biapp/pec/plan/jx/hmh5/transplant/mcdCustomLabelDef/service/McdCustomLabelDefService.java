package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.model.McdCustomLabelDef;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo.SelfDefinedLabelResultConfInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo.SelfDefinedLabelResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签定义表 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-26
 */
public interface McdCustomLabelDefService extends IService<McdCustomLabelDef> {

    Page<SelfDefinedLabelResultInfo> selectSelfDefinedLabel(String cityId, SelectSelfDefinedLabelConfParam selfDefinedLabelConfParam);

    void modifySelfDefinedLabel(String userId, ModifySelfDefinedLabelParam selfDefinedLabelParam);

    Page<SelfDefinedLabelResultConfInfo> selectSelfDefinedLabelConfDetail(String cityId, SelectSelfDefinedLabelParam selfDefinedLabelParam);

    Map<String, List<SelfDefinedLabelResultInfo>> selectSelfDefinedLabelConf(String cityId);

    void modifySelfDefinedLabelConf(String cityId, ModifySelfDefinedLabelConfParam selfDefinedLabelConfParam);

    void modifyLabelStatus(ModifySelfLabelStatusParam labelStatusParam);


    ActionResponse<SubmitProcessJxDTO> getNodeApprover();

    void commitProcess(LabelCommitProcessParam processParam, UserSimpleInfo user);

    String selectLabelApproveFlowId(Integer labelId);

    /**
     * 根据labelId值删除自定义预警
     *
     * @param user 登录用户信息
     * @param labelId 标签id
     */
    void deleteLabel(UserSimpleInfo user, String labelId);

}
