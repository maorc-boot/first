package com.asiainfo.biapp.pec.plan.jx.coordination.service;

import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessDTO;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCampCoordinationTaskModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.CampCoordinationApproveQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.CoordinationSubmitProcessQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.SubmTaskApproveQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.response.ApprTaskRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 * Description : 江西统筹任务审批服务
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-6-6
 */

public interface IMcdCampCoordinationApproveService extends IService<McdCampCoordinationTaskModel> {
    
    /**
     * 统筹任务提交审批
     *
     * @param req
     * @param user
     */
    void submitTaskAppr(CoordinationSubmitProcessQuery req, UserSimpleInfo user);

    
    
    /**
     * 获取统筹任务提交审批入参
     *
     * @param submApproveQuery
     * @return
     */
    SubmitProcessQuery getCmpApprovalProcess(SubmTaskApproveQuery submApproveQuery);
    
    /**
     * 统筹任务审批列表
     *
     * @param req
     * @return
     */
    IPage<ApprTaskRecord> approveTaskRecord(CampCoordinationApproveQuery req);

    /**
     * 统筹子任务待审批列表查询
     *
     * @param taskIds     父任务id
     * @param qryApproved qryApproved
     * @param status      状态
     * @return {@link List}<{@link ApprTaskRecord}>
     */
    List<ApprTaskRecord> approveChildTaskRecord(List<String> taskIds, boolean qryApproved, String status);

    /**
     * 获取审批流程以及下一节点审批人
     *
     * @param submitProcessDTO
     * @return
     */
    SubmitProcessDTO getTaskApprover(SubmitProcessDTO submitProcessDTO);


}
