package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.jx.model.McdBatchApproveRecord;
import com.asiainfo.biapp.pec.approve.jx.model.McdBatchApproveTask;
import com.asiainfo.biapp.pec.approve.jx.po.McdBatchApproveTaskPO;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 批量审批任务 服务类
 * </p>
 *
 * @author mamp
 * @since 2023-11-07
 */
public interface McdBatchApproveTaskService extends IService<McdBatchApproveTask> {
    /**
     * 提交批量任务
     *
     * @param list
     */
    boolean submit(List<McdBatchApproveRecord> list, UserSimpleInfo user);

    /**
     * 获取下级审批人新接口
     *
     * @param submitProcessDTO
     * @return
     */
    SubmitProcessJxDTO getNodeApprover(SubmitProcessJxDTO submitProcessDTO);

    /**
     * 提交审批结果（审批通过或者审批驳回）
     *
     * @param submitProcessDTO
     * @return
     */

    Long submitProcess(SubmitProcessJxDTO submitProcessDTO);

}
