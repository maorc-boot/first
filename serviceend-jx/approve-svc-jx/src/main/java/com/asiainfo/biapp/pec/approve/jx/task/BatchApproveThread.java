package com.asiainfo.biapp.pec.approve.jx.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.jx.Enum.ConstApprove;
import com.asiainfo.biapp.pec.approve.jx.dto.NodesApproverJx;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.jx.model.McdBatchApproveRecord;
import com.asiainfo.biapp.pec.approve.jx.model.McdBatchApproveTask;
import com.asiainfo.biapp.pec.approve.jx.model.SysUser;
import com.asiainfo.biapp.pec.approve.jx.po.McdBatchApproveTaskPO;
import com.asiainfo.biapp.pec.approve.jx.service.McdBatchApproveRecordService;
import com.asiainfo.biapp.pec.approve.jx.service.McdBatchApproveTaskService;
import com.asiainfo.biapp.pec.approve.model.User;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mamp
 * @date 2023/11/8
 */
@Slf4j
public class BatchApproveThread implements Runnable {

    private McdBatchApproveTaskService taskService;
    private McdBatchApproveRecordService recordService;
    private McdBatchApproveTask approveTask;
    private List<McdBatchApproveRecord> records;
    private UserSimpleInfo user;


    public BatchApproveThread(McdBatchApproveTaskService taskService,
                              McdBatchApproveRecordService recordService,
                              McdBatchApproveTask approveTask,
                              List<McdBatchApproveRecord> records, UserSimpleInfo user) {
        this.taskService = taskService;
        this.recordService = recordService;
        this.approveTask = approveTask;
        this.records = records;
        this.user = user;
    }

    @Override
    public void run() {
        // 审批成功列表
        List<String> successList = new ArrayList<>(records.size());
        // 审批失败列表
        List<String> failList = new ArrayList<>(records.size());

        try {

            // 1. 遍历审批
            for (McdBatchApproveRecord record : records) {
                approveByRecord(successList, failList, record,user);
            }

            // 2. 更新任务状态
            if (CollectionUtil.isNotEmpty(failList)) {
                String result = StrUtil.format("失败记录:{},成功记录:{}", JSONUtil.toJsonStr(failList), JSONUtil.toJsonStr(successList));
                // 状态失败
                approveTask.setStatuas(3);
                // 执行结果
                approveTask.setResult(result);
            } else {
                // 状态成功
                approveTask.setStatuas(2);
            }
            taskService.saveOrUpdate(approveTask);
        } catch (Exception ex) {
            log.error("批量审批任务执行失败,task:{}", JSONUtil.toJsonStr(approveTask), ex);
        } finally {
            // 3执行完成释放锁
            String key = StrUtil.concat(false, ConstApprove.USER_BATCH_APPROVE_LOCK_PRIFIX, approveTask.getCreateUser());
            RedisUtils.deleteKey(key);
        }

    }

    /**
     * 单条审批
     *
     * @param successList
     * @param failList
     * @param record
     */
    private void approveByRecord(List<String> successList, List<String> failList, McdBatchApproveRecord record,UserSimpleInfo userInfo) {
        try {
            SubmitProcessJxDTO submitProcessDTO = new SubmitProcessJxDTO();
            submitProcessDTO.setApprovalType(record.getSystemId());
            submitProcessDTO.setInstanceId(Long.valueOf(record.getInstanceId()));
            submitProcessDTO.setBusinessId(record.getBusinessId());
            submitProcessDTO.setNodeId(record.getNodeId());
            submitProcessDTO.setDealOpinion(record.getDealOpinion());
            User user = new User();
            user.setUserId(userInfo.getUserId());
            user.setUserName(userInfo.getUserName());
            user.setCityId(userInfo.getCityId());
            user.setDepartmentId(userInfo.getDepartmentId());
            submitProcessDTO.setUser(user);
            SubmitProcessJxDTO nodeApprover = taskService.getNodeApprover(submitProcessDTO);
            nodeApprover.setSubmitStatus(record.getStatus());


            List<NodesApproverJx> nextNodesApprover = nodeApprover.getNextNodesApprover();
            for (NodesApproverJx nodesApproverJx : nextNodesApprover) {
                List<SysUser> approverUser = nodesApproverJx.getApproverUser();
                if (CollectionUtil.isEmpty(approverUser)) {
                    throw new Exception("获取下级审批人为空");
                }
                // 默认保留第一个
                List<SysUser> sysUsers = approverUser.subList(0, 1);
                nodesApproverJx.setApproverUser(sysUsers);
            }

            log.info("批量审批当前审批状态为:  {}", nodeApprover.getSubmitStatus());
            taskService.submitProcess(nodeApprover);
            // 更新record状态
            updateRecordStatus(record, 1);
            log.info("批量审批成功,审批详情:{}", JSONUtil.toJsonStr(record));
            successList.add(record.getRecordId());
        } catch (Exception e) {
            log.error("批量审批失败,审批详情:{}", JSONUtil.toJsonStr(record), e);
            failList.add(record.getRecordId());
            // 更新record状态
            updateRecordStatus(record, 2);
        }
    }

    private void updateRecordStatus(McdBatchApproveRecord record, Integer status) {
        // 更新record状态
        UpdateWrapper<McdBatchApproveRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(McdBatchApproveRecord::getStatus, status)
                .set(McdBatchApproveRecord::getUpdateTime, new Date())
                .eq(McdBatchApproveRecord::getRecordId, record.getRecordId())
                .eq(McdBatchApproveRecord::getBatchId, record.getBatchId());
        recordService.update(updateWrapper);
    }
}
