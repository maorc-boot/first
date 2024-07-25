package com.asiainfo.biapp.pec.plan.jx.hmh5.service;

import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdHmh5BlacklistTask;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.BlacklistApprRecord;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.BlacklistApproveJxQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 客户通黑名单批量导入任务 服务类
 * </p>
 *
 * @author mamp
 * @since 2024-05-28
 */
public interface McdHmh5BlacklistTaskService extends IService<McdHmh5BlacklistTask> {


    ActionResponse createOrUpdate(MultipartFile multiFile, String taskId, String taskName, String remark, UserSimpleInfo user );

    /**
     * 客户通黑名单提交审批
     *
     * @param req  要求事情
     * @param user 用户
     */
    void submitBlacklist(SubmitProcessQuery req, UserSimpleInfo user);

    /**
     * 获取客户通黑名单审批流程实例节点下级审批人
     *
     * @param submitProcessDTO submitProcessDTO
     * @return SubmitProcessDTO
     */
    ActionResponse<SubmitProcessJxDTO> getBlacklistApprover(SubmitProcessJxDTO submitProcessDTO);

    /**
     * 客户通黑名单审批列表
     *
     * @param req 客户通黑名单审批列表分页(或根据条件)查询入参
     * @return {@link IPage}<{@link BlacklistApprRecord}>
     */
    IPage<BlacklistApprRecord> approveBlacklistRecord(BlacklistApproveJxQuery req);

    /**
     * 通导入任务ID，导入黑名单清单
     * @param taskId
     * @throws Exception
     */
     void importBlacklistByTaskId(String taskId) throws Exception;
}
