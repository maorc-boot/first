package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.Enum.DealStatus;
import com.asiainfo.biapp.pec.approve.Enum.NodeType;
import com.asiainfo.biapp.pec.approve.dto.NodesApprover;
import com.asiainfo.biapp.pec.approve.dto.SubmitProcessDTO;
import com.asiainfo.biapp.pec.approve.jx.dto.NodesApproverJx;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.jx.model.McdEmisTask;
import com.asiainfo.biapp.pec.approve.jx.model.SysUser;
import com.asiainfo.biapp.pec.approve.jx.service.IApprovePostProcessor;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdCoordinationAppr2Emis;
import com.asiainfo.biapp.pec.approve.jx.service.McdEmisTaskService;
import com.asiainfo.biapp.pec.approve.model.*;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveProcessInstanceService;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveProcessNodeService;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveProcessRecordService;
import com.asiainfo.biapp.pec.approve.service.IMcdCampOperateLogService;
import com.asiainfo.biapp.pec.approve.util.DataUtil;
import com.asiainfo.biapp.pec.approve.util.NetUtils;
import com.asiainfo.biapp.pec.approve.util.RequestUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * description: 江西策略统筹审批&&增加emis代办service实现
 *
 * @author: lvchaochao
 * @date: 2023/7/28
 */
@Service
@Slf4j
public class McdCoordinationAppr2EmisImpl implements IMcdCoordinationAppr2Emis {

    @Autowired
    private ICmpApproveProcessNodeService nodeService;

    @Autowired
    private ICmpApproveProcessInstanceService instanceService;

    @Autowired
    private ICmpApproveProcessRecordService recordService;

    @Autowired
    private IMcdCampOperateLogService operateLogService;

    @Autowired
    private IApprovePostProcessor approvePostProcessor;

    @Autowired
    private McdEmisTaskService mcdEmisTaskService;

    @Override
    public Long commitProcess(SubmitProcessJxDTO submitProcessDTO) {
        //流程实例
        CmpApproveProcessInstance processInstance = new CmpApproveProcessInstance();
        Long instanceId = submitProcessDTO.getInstanceId();
        //策略操作记录实例
        McdCampOperateLog operateLog = McdCampOperateLog.builder()
                .id(DataUtil.generateId().toString())
                .logDesc("策划审批")
                .logTime(new Date())
                .campsegId(submitProcessDTO.getBusinessId())
                .logType(4)
                .userId(submitProcessDTO.getUser().getUserId())
                .userName(submitProcessDTO.getUser().getUserName())
                .userComment("审批通过")
                .opResult("成功")
                .build();
        final HttpServletRequest request = RequestUtils.getRequest();
        if (null != request) {
            operateLog.setUserIpAddr(NetUtils.getClientIp(request));
            operateLog.setUserBrower(NetUtils.getClientBrowser(request));
        }
        //获取当前节点
        CmpApproveProcessNode thisNode = nodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                .eq(CmpApproveProcessNode::getProcessId, submitProcessDTO.getProcessId())
                .eq(CmpApproveProcessNode::getProcessVersionNum, submitProcessDTO.getBerv())
                .eq(CmpApproveProcessNode::getNodeId, submitProcessDTO.getNodeId()).last("LIMIT 1")
        );
        User user = submitProcessDTO.getUser();
        // 用户对象信息转换
        SysUser sysUser = userConvert(user);
        //校验当前用户是否有当前节点的审批权限
        instanceId = DataUtil.generateId();
        submitProcessDTO.setInstanceId(instanceId);
        //流程实例业务流程ID
        processInstance.setBusinessId(submitProcessDTO.getBusinessId());
        //流程实例流程实例ID
        processInstance.setInstanceId(instanceId);
        //流程实例流程配置ID
        processInstance.setProcessId(submitProcessDTO.getProcessId());
        //流程实例流程版本号
        processInstance.setProcessVersionNum(submitProcessDTO.getBerv());
        //创建开始节点
        CmpApproveProcessRecord startRecord = createNoApproveRecord(thisNode, sysUser, instanceId, submitProcessDTO.getBusinessId());
        //保存流程实例
        CmpApproveProcessInstance instanceServiceOne = instanceService.getOne(Wrappers.<CmpApproveProcessInstance>query().lambda().eq(CmpApproveProcessInstance::getBusinessId, submitProcessDTO.getBusinessId()));
        // 审批实例表无此主任务的数据 则新增
        if (ObjectUtil.isEmpty(instanceServiceOne)) {
            instanceService.save(processInstance);
        } else {
            // 如果有这个主任务的实例数据 需要将流程实例状态改为1-审批中
            instanceService.update(Wrappers.<CmpApproveProcessInstance>lambdaUpdate()
                    .set(CmpApproveProcessInstance::getInstanceStatus, 1)
                    .set(CmpApproveProcessInstance::getModifyDate, new Date())
                    .eq(CmpApproveProcessInstance::getBusinessId, submitProcessDTO.getBusinessId()));

        }
        //保存审批流程记录
        List<NodesApproverJx> approvers = submitProcessDTO.getNextNodesApprover();
        approvers.forEach(approver -> {
            for (SysUser userApp : approver.getApproverUser()) {
                CmpApproveProcessNode node = approver.getNode();
                CmpApproveProcessRecord processRecord = createNoApproveRecord(node, userApp, submitProcessDTO.getInstanceId(), submitProcessDTO.getBusinessId());
                // 查询审批流程记录是否已有该任务信息 且节点类型是审批节点 需将状态改为0
                CmpApproveProcessRecord recordServiceOne = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                        .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                        .eq(CmpApproveProcessRecord::getNodeType, 1));
                if (ObjectUtil.isEmpty(recordServiceOne)) {
                    // 保存审批流程记录 (策略统筹子任务多次提交，审批流程记录只保存一次)
                    recordService.save(processRecord);
                } else {
                    // 将1-审批节点 状态改为0-待处理
                    recordService.update(Wrappers.<CmpApproveProcessRecord>lambdaUpdate()
                            .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                            .eq(CmpApproveProcessRecord::getNodeType, 1)
                            .set(CmpApproveProcessRecord::getDealStatus, 0)
                            .set(CmpApproveProcessRecord::getModifyDate, new Date()));
                }
                // 增加emis待办
                // addEmis(submitProcessDTO, processInstance, processRecord);
                if (ObjectUtil.isEmpty(instanceServiceOne)) {
                    // 添加 Emis 待办
                    approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstance, processRecord);
                } else {
                    // 1.若实例状态为1-审批中 则继续后续操作修改子任务状为审批中
                    // 2.若实例状态为2-审批通过 则删除待办
                    if (instanceServiceOne.getInstanceStatus() == 2) {
                        // 删除===》修改 mcd_emis_task 状态
                        mcdEmisTaskService.remove(Wrappers.<McdEmisTask>lambdaQuery().eq(McdEmisTask::getCampsegId, submitProcessDTO.getBusinessId()));
                        // 取消emis待办
                        approvePostProcessor.rejectProcess(submitProcessDTO, processInstance, processRecord);
                    }
                    // 3.若实例状态为3-驳回     则新增待办
                    if (instanceServiceOne.getInstanceStatus() == 3) {
                        // 删除===》修改 mcd_emis_task 状态
                        mcdEmisTaskService.remove(Wrappers.<McdEmisTask>lambdaQuery().eq(McdEmisTask::getCampsegId, submitProcessDTO.getBusinessId()));
                        // 添加 Emis 待办
                        approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstance, processRecord);
                    }
                }
            }
        });
        // 查询是否有开始节点
        CmpApproveProcessRecord recordServiceOne = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                .eq(CmpApproveProcessRecord::getNodeType, 0));
        if (ObjectUtil.isEmpty(recordServiceOne)) {
            // 保存审批流程记录 (策略统筹子任务多次提交，审批流程记录只保存一次)
            recordService.save(startRecord);
        } else {
            recordService.update(Wrappers.<CmpApproveProcessRecord>lambdaUpdate()
                    .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                    .eq(CmpApproveProcessRecord::getNodeType, 0)
                    .set(CmpApproveProcessRecord::getModifyDate, new Date()));
        }
        McdCampOperateLog one = operateLogService.getOne(Wrappers.<McdCampOperateLog>query().lambda().eq(McdCampOperateLog::getCampsegId, submitProcessDTO.getBusinessId()));
        if (ObjectUtil.isEmpty(one)) {
            // 保存操作记录 (策略统筹子任务多次提交，操作记录只保存一次)
            operateLogService.save(operateLog);
        }
        return instanceId;
    }

    /**
     * 增加emis待办
     *
     * @param submitProcessDTO 提交过程dto
     * @param processInstance  流程实例
     * @param processRecord    过程记录
     */
    private void addEmis(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance processInstance, CmpApproveProcessRecord processRecord) {
        // 判断此任务是否已有Emis代办-->根据主任务id查询mcd_emis_task表是否已有记录
        McdEmisTask mcdEmisTask = mcdEmisTaskService.getOne(Wrappers.<McdEmisTask>query().lambda().eq(McdEmisTask::getCampsegId, submitProcessDTO.getBusinessId()));
        log.info("addEmis-->mcdEmisTask={}", JSONUtil.toJsonStr(mcdEmisTask));
        if (ObjectUtil.isEmpty(mcdEmisTask)) {
            log.info("统筹任务={}没有Emis代办记录，添加Emis待办", submitProcessDTO.getBusinessId());
            // 添加 Emis 待办
            approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstance, processRecord);
        } else {
            // 此任务下有提交审批记录且状态是"审批不通过"或者"审批通过" （带审批的不发送待办）   0：审批不通过 1：审批通过 2：待审批
            McdEmisTask rejectTask = mcdEmisTaskService.getOne(Wrappers.<McdEmisTask>query().lambda().eq(McdEmisTask::getCampsegId, submitProcessDTO.getBusinessId())
                    .ne(McdEmisTask::getResult, 2));
            log.info("addEmis-->rejectTask={}", JSONUtil.toJsonStr(rejectTask));
            if (ObjectUtil.isNotEmpty(rejectTask)) {
                log.info("统筹任务={}有Emis代办且被驳回，添加Emis待办", mcdEmisTask.getCampsegId());
                // 添加 Emis 待办
                approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstance, processRecord);
            }
        }
    }

    /**
     * @param node       流程节点
     * @param instanceId 流程实例id
     * @return CmpApproveProcessRecord
     */
    private CmpApproveProcessRecord createNoApproveRecord(CmpApproveProcessNode node, SysUser user, Long instanceId, String businessId) {
        //结束节点只有一个
        if (node.getNodeType().toString().equals(NodeType.END_NODE)) {
            CmpApproveProcessRecord processRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getInstanceId, instanceId)
                    .eq(CmpApproveProcessRecord::getNodeId, node.getNodeId())
                    .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.TO_SUBMIT)
            );
            if (processRecord != null) {
                return processRecord;
            }
        }
        //审批流程记录表
        CmpApproveProcessRecord record = new CmpApproveProcessRecord();
        record.setId(DataUtil.generateId());
        record.setBusinessId(businessId);
        record.setDealTime(new Date());
        record.setInstanceId(instanceId);
        record.setNodeBusinessName(node.getNodeName());
        record.setNodeId(node.getNodeId());
        record.setNodeType(node.getNodeType());
        record.setNodeName(node.getNodeName());
        record.setApprover(user.getUserId());
        record.setApproverName(user.getUserName());
        record.setDealStatus(Integer.parseInt(DealStatus.PENDING));
        if (node.getNodeType().toString().equals(NodeType.STARTING_NODE)) {
            record.setDealStatus(Integer.parseInt(DealStatus.PASS));
            record.setDealOpinion("提交策划");
        } else if (node.getNodeType().toString().equals(NodeType.END_NODE)) {
            record.setDealOpinion("审批通过");
        } else {
            record.setDealOpinion("执行事件");
        }
        return record;
    }

    /**
     * User对象转换SysUser
     *
     * @param user 用户
     * @return {@link SysUser}
     */
    private SysUser userConvert(User user) {
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(user, sysUser);
        return sysUser;
    }
}
