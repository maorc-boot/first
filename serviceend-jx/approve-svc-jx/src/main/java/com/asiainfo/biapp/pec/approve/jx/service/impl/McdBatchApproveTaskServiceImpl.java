package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.jx.dao.McdBatchApproveTaskMapper;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.jx.model.McdBatchApproveRecord;
import com.asiainfo.biapp.pec.approve.jx.model.McdBatchApproveTask;
import com.asiainfo.biapp.pec.approve.jx.po.McdBatchApproveTaskPO;
import com.asiainfo.biapp.pec.approve.jx.service.ApproveService;
import com.asiainfo.biapp.pec.approve.jx.service.ICmpApprovalProcessJxService;
import com.asiainfo.biapp.pec.approve.jx.service.McdBatchApproveRecordService;
import com.asiainfo.biapp.pec.approve.jx.service.McdBatchApproveTaskService;
import com.asiainfo.biapp.pec.approve.jx.task.BatchApproveThread;
import com.asiainfo.biapp.pec.approve.jx.utils.IdUtils;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * 批量审批任务 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2023-11-07
 */
@Service
@Slf4j
public class McdBatchApproveTaskServiceImpl extends ServiceImpl<McdBatchApproveTaskMapper, McdBatchApproveTask> implements McdBatchApproveTaskService {

    @Resource
    private McdBatchApproveRecordService mcdBatchApproveRecordService;

    @Resource
    private ApproveService approveService;

    @Resource
    private ICmpApprovalProcessJxService cmpApprovalProcessJxService;

    @Resource
    private McdBatchApproveTaskService mcdBatchApproveTaskService;

    /**
     * 实时活动同步redis任务线程池
     */
    private ExecutorService executor = ThreadUtil.newExecutor(5);

    /**
     * 提交批量任务
     *
     * @param list
     */
    @Override
    public boolean submit(List<McdBatchApproveRecord> list, UserSimpleInfo user) {

        try {
            String batchId = IdUtils.generateId();
            McdBatchApproveTask task = new McdBatchApproveTask();
            task.setBatchId(batchId);
            task.setBatchSize(list.size());
            task.setStatuas(list.get(0).getStatus());
            task.setCreateUser(user.getUserId());
            task.setDealOpinion(list.get(0).getDealOpinion());
            // 1. 生成任务相关数据
            mcdBatchApproveTaskService.save(task);
            for (McdBatchApproveRecord record : list) {
                record.setBatchId(task.getBatchId());
                record.setStatus(task.getStatuas());
            }
            mcdBatchApproveRecordService.saveBatch(list);

            BatchApproveThread thread = new BatchApproveThread(this, mcdBatchApproveRecordService, task, list,user);
            executor.submit(thread);
            return true;
        } catch (Exception e) {
            log.error("提交审批任务失败,user:{},list:{}", JSONUtil.toJsonStr(user),JSONUtil.toJsonStr(list));
            return false;
        }
    }

    /**
     * 获取下级审批人新接口
     *
     * @param submitProcessDTO
     * @return
     */
    @Override
    public SubmitProcessJxDTO getNodeApprover(SubmitProcessJxDTO submitProcessDTO) {
        return approveService.getNodeApprover(submitProcessDTO);
    }

    /**
     * 提交审批结果（审批通过或者审批驳回）
     *
     * @param submitProcessDTO
     * @return
     */
    @Override
    public Long submitProcess(SubmitProcessJxDTO submitProcessDTO) {
        return cmpApprovalProcessJxService.submitProcess(submitProcessDTO);
    }
}
