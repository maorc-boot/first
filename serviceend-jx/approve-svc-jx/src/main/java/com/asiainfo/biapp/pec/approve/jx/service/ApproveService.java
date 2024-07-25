package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.model.CmpApprovalProcess;
import com.asiainfo.biapp.pec.approve.model.User;

public interface ApproveService {

    /**
     * 获取下级审批人新接口
     *
     * @param submitProcessDTO
     * @return
     */
    SubmitProcessJxDTO getNodeApprover(SubmitProcessJxDTO submitProcessDTO);



    SubmitProcessJxDTO getH5NodeApprover(SubmitProcessJxDTO submitProcessDTO);

    /**
     * 营销活动提交审批
     *
     * @param req
     */
    void submitCampseg(SubmitProcessJxDTO req, User user);

    /**
     * 主题列表批量提交审批
     *
     * @param req 请求入参
     * @param user 用户信息
     */
    void batchSubmitCampseg(SubmitProcessJxDTO req, User user);

    /**
     * 复制审批流程
     * @param proceesId 模板流程ID
     * @return
     */
    CmpApprovalProcess copyCmpApprovalProcess(Long proceesId);

    /**
     * 判断审批模板是否在使用中
     * @param proceesId
     * @return
     */
    boolean isProcessUsed(Long proceesId);
}
