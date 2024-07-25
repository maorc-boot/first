package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.dto.SubmitProcessDTO;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessInstance;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessNode;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;

import java.util.LinkedList;
import java.util.List;

/**
 * @author mamp
 * @date 2022/9/19
 */
public interface IApprovePostProcessor {

    /**
     * 审批通过后处理
     */
    void approvedProcess(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance processInstance, CmpApproveProcessNode thisNode, CmpApproveProcessRecord thisRecord, List<CmpApproveProcessNode> nextNodeList, LinkedList<CmpApproveProcessRecord> nextRecordList);

    /**
     * 驳回后处理
     *
     * @param submitProcessDTO
     * @param processInstance
     * @param record
     */
    void rejectProcess(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance processInstance, CmpApproveProcessRecord record);

    /**
     * 驳回后处理增加发送emis阅知待办
     *
     * @param submitProcessDTO 提交流程信息
     * @param processInstance 流程实例信息
     */
    void rejectProcessAndSendEmis(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance processInstance, CmpApproveProcessRecord thisRecord) throws Exception;

    /**
     * 待审批后处理
     *
     * @param processInstance 审批实例
     * @param record          审批记录
     */
    void approvalPendingProcess(SubmitProcessJxDTO submitProcessDTO,CmpApproveProcessInstance processInstance, CmpApproveProcessRecord record);


}
