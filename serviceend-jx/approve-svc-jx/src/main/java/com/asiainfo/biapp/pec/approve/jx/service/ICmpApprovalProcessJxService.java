package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.dto.ApproveUserTaskBo;
import com.asiainfo.biapp.pec.approve.jx.dto.BatchApproveResult;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessInstance;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessNode;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.approve.model.User;

/**
 * @author mamp
 * @date 2022/9/19
 */
public interface ICmpApprovalProcessJxService {
    /**
     * 提交审批（在草稿状状态或者预演完成状态提交）
     * @param submitProcessDTO
     * @return
     */
    Long commitProcess(SubmitProcessJxDTO submitProcessDTO);

    /**
     * 提交审批结果（审批通过或者审批驳回）
     * @param submitProcessDTO
     * @return
     */

    Long submitProcess(SubmitProcessJxDTO submitProcessDTO);

    /**
     * 处理异常数据
     *
     * @param submitProcessDTO
     * @param instanceId
     */
    void checkAndHandelErrorData(SubmitProcessJxDTO submitProcessDTO, Long instanceId);

    /**
     * 批量提交审批
     * @param submitProcessDTO
     * @return
     */
    BatchApproveResult batchSubmitProcess(SubmitProcessJxDTO submitProcessDTO);

    /**
     * 审批驳回
     *
     * @param processInstance processInstance
     * @param thisNode thisNode
     * @param thisRecord thisRecord
     * @param user user
     * @param submitProcessDTO submitProcessDTO
     */
    void approvalRejected(CmpApproveProcessInstance processInstance, CmpApproveProcessNode thisNode, CmpApproveProcessRecord thisRecord, User user, SubmitProcessJxDTO submitProcessDTO);

    /**
     * 转至沟通人
     * @param task
     * @param user
     */
    void addCommunicationTask(ApproveUserTaskBo task, User user) throws Exception;


    /**
     * 转至沟通人
     * @param task
     * @param user
     */
    BatchApproveResult addCommunicationTaskBatch(ApproveUserTaskBo task, User user) throws Exception;

    /**
     * 设置当前审批实例的触发条件
     *
     * @param submitProcessDTO
     */
    void setTriggerParam(SubmitProcessJxDTO submitProcessDTO);
}
