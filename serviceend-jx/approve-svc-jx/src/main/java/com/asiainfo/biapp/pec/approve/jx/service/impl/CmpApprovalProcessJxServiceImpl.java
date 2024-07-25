package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.Enum.*;
import com.asiainfo.biapp.pec.approve.common.EventInterface;
import com.asiainfo.biapp.pec.approve.common.McdException;
import com.asiainfo.biapp.pec.approve.config.EventConfig;
import com.asiainfo.biapp.pec.approve.dto.UpdateCampStatusDTO;
import com.asiainfo.biapp.pec.approve.jx.Enum.ConstApprove;
import com.asiainfo.biapp.pec.approve.jx.dto.*;
import com.asiainfo.biapp.pec.approve.jx.event.EventInterfaceJx;
import com.asiainfo.biapp.pec.approve.jx.model.*;
import com.asiainfo.biapp.pec.approve.jx.service.*;
import com.asiainfo.biapp.pec.approve.jx.service.feign.*;
import com.asiainfo.biapp.pec.approve.jx.service.feign.param.*;
import com.asiainfo.biapp.pec.approve.model.McdCampOperateLog;
import com.asiainfo.biapp.pec.approve.model.*;
import com.asiainfo.biapp.pec.approve.service.*;
import com.asiainfo.biapp.pec.approve.service.feign.PlanService;
import com.asiainfo.biapp.pec.approve.util.DataUtil;
import com.asiainfo.biapp.pec.approve.util.NetUtils;
import com.asiainfo.biapp.pec.approve.util.RequestUtils;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author mamp
 * @date 2022/9/19
 */
@Service
@Slf4j
@RefreshScope
public class CmpApprovalProcessJxServiceImpl implements ICmpApprovalProcessJxService {

    @Autowired
    private ICmpApprovalProcessService cmpApprovalProcessService;
    @Autowired
    private ICmpApproveProcessNodeService nodeService;
    @Autowired
    private ICmpApprovalProcessService processService;
    @Autowired
    private ICmpApprovalProcessFlowService flowService;
    @Autowired
    private ICmpApproveProcessInstanceService instanceService;
    @Autowired
    private ICmpApproveProcessRecordService recordService;
    @Autowired
    private ICmpApprovalProcessTriggerService triggerService;
    @Autowired
    private IMcdCampOperateLogService operateLogService;

    @Autowired
    private PlanService planService;

    @Resource
    private ElementMatermialJxService elementMatermialJxService;
    @Autowired
    private IApprovePostProcessor approvePostProcessor;
    @Autowired
    private IMcdDimMaterialJxService mcdDimMaterialJxService;

    @Autowired
    private IMcdCampDefService campDefService;

    @Resource
    private IMcdCampChannelListService campChannelListService;

    @Resource
    private McdCampCoordinationFeignClient mcdCampCoordinationFeignClient;

    @Autowired
    private CustomAlarmFeignClient customAlarmFeignClient;

    @Autowired
    private McdTermFeignClient termFeignClient;

    @Autowired
    private KhtCareSmsTemplateFeignClient khtCareSmsTemplateFeignClient;

    @Autowired
    private KhtBlacklistFeignClient blacklistFeignClient;

    @Autowired
    private KhtCalloutRuleFeignClient calloutRuleFeignClient;

    @Autowired
    private CustomLabelFeignClient customLabelFeignClient;

    @Autowired
    private McdEmisTaskService mcdEmisTaskService;

    @Autowired
    private IUserService userService;

    @Resource
    private McdApprovalAdviceService mcdApprovalAdviceService;

    private static final String INSTANCE_LOCK_KEY = "INSTANCE_LOCK_KEY";
    private static final long LOCK_TIMEOUT = 10;
    private static final String MATERIAL = "MATERIAL";
    private static final String IMCD = "IMCD";
    private static final String TASK = "TASK";
    private static final String TERM = "TERM";
    private static final String MESSAGE_TEMPLATE = "MESSAGE_TEMPLATE";
    private static final String BATTLE_MAP = "BATTLE_MAP";
    private static final String HMH5_BLACKLIST = "HMH5_BLACKLIST";
    private static final String HMH5_CALLOUT_RULE = "MCD_RULES";

    /**
     * 提交审批（在草稿状状态或者预演完成状态提交）
     *
     * @param submitProcessDTO
     * @return
     */
    @Override
    @Transactional
    public Long commitProcess(SubmitProcessJxDTO submitProcessDTO) {
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

        //获取终止节点
        CmpApproveProcessNode lastNode = nodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                .eq(CmpApproveProcessNode::getProcessId, submitProcessDTO.getProcessId())
                .eq(CmpApproveProcessNode::getProcessVersionNum, submitProcessDTO.getBerv())
                .eq(CmpApproveProcessNode::getNodeType, 2).last("LIMIT 1")
        );

        //获取审批流程模板
        CmpApprovalProcess approvalProcess = cmpApprovalProcessService.getOne(Wrappers.<CmpApprovalProcess>query().lambda()
                .eq(CmpApprovalProcess::getProcessId, thisNode.getProcessId()).last("LIMIT 1"));

        CmpApproveProcessInstance processInstance = instanceService.getOne(Wrappers.<CmpApproveProcessInstance>query().lambda()
                .eq(CmpApproveProcessInstance::getBusinessId, submitProcessDTO.getBusinessId())
                .eq(CmpApproveProcessInstance::getProcessId, submitProcessDTO.getProcessId()
                ).last("LIMIT 1")
        );
        // 当前节点审批人
        User user = submitProcessDTO.getUser();
        SysUser sysUser = userConvert(user);
        //继续审批流程
        if (approvalProcess.getProcessType() == 0) {
            processInstance = continueApproveProcessInstance(submitProcessDTO, thisNode, lastNode, processInstance, sysUser);
        } else {
            processInstance = reApproveProcessInstance(submitProcessDTO, thisNode, lastNode, sysUser);
        }

        //TODO 2023-07-05 10:47:55 是否保存日志，暂定
/*        if (CUSTOM_LABEL.equals(submitProcessDTO.getApprovalType())){
            operateLog.setLogType(0);   //用0表示其他的，与策略区分
            operateLog.setLogDesc("提交自定义标签审批");
            operateLog.setUserComment("审批提交成功");
        }else if (CUSTOM_ALARM.equals(submitProcessDTO.getApprovalType())){
            operateLog.setLogType(0);   //用0表示其他的，与策略区分
            operateLog.setLogDesc("提交自定义预警审批");
            operateLog.setUserComment("审批提交成功");
        }*/
        //TODO 2023-07-07 16:55:15 如果不是自定义标签或者预警，就保存日志
        if (!Arrays.asList(CustomLabelFeignClient.CUSTOM_LABEL, CustomAlarmFeignClient.CUSTOM_ALARM).contains(submitProcessDTO.getApprovalType()))
            operateLogService.save(operateLog);
        return processInstance.getInstanceId();
    }

    /**
     * 重新审批流程-提交审批
     *
     * @param submitProcessDTO
     * @param thisNode
     * @param lastNode
     * @param user
     * @return
     */
    private CmpApproveProcessInstance reApproveProcessInstance(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessNode thisNode, CmpApproveProcessNode lastNode, SysUser user) {
        CmpApproveProcessInstance processInstance;//重新审批流程

        //流程实例
        processInstance = new CmpApproveProcessInstance();
        //校验当前用户是否有当前节点的审批权限
        Long instanceId = DataUtil.generateId();
        submitProcessDTO.setInstanceId(instanceId);
        //流程实例业务流程ID
        processInstance.setBusinessId(submitProcessDTO.getBusinessId());
        //流程实例流程实例ID
        processInstance.setInstanceId(instanceId);
        //流程实例流程配置ID
        processInstance.setProcessId(submitProcessDTO.getProcessId());
        //流程实例流程版本号
        processInstance.setProcessVersionNum(submitProcessDTO.getBerv());
        //保存流程实例
        instanceService.save(processInstance);
        //创建开始节点
        CmpApproveProcessRecord startRecord = createNoApproveRecord(thisNode, user, processInstance.getInstanceId(), submitProcessDTO.getBusinessId());
        recordService.save(startRecord);
        //创建结束节点
        CmpApproveProcessRecord endRecord = createNoApproveRecord(lastNode, user, processInstance.getInstanceId(), submitProcessDTO.getBusinessId());
        recordService.save(endRecord);

        final CmpApproveProcessInstance processInstanceNew = processInstance;
        //保存审批流程记录
        List<NodesApproverJx> approvers = submitProcessDTO.getNextNodesApprover();
        approvers.forEach(approver -> {
            for (SysUser userApp : approver.getApproverUser()) {
                //重新审批流程
                CmpApproveProcessNode node = approver.getNode();
                CmpApproveProcessRecord processRecord = createNoApproveRecord(node, userApp, submitProcessDTO.getInstanceId(), submitProcessDTO.getBusinessId());
                //保存审批流程记录
                recordService.save(processRecord);
                if (!MATERIAL.equals(submitProcessDTO.getApprovalType())) {
                    // 添加 Emis 待办
                    approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstanceNew, processRecord);
                }
            }
        });
        return processInstance;
    }

    /**
     * 继续审批流程-提交审批
     *
     * @param submitProcessDTO
     * @param thisNode
     * @param lastNode
     * @param processInstance
     * @param user
     * @return
     */
    private CmpApproveProcessInstance continueApproveProcessInstance(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessNode thisNode, CmpApproveProcessNode lastNode, CmpApproveProcessInstance processInstance, SysUser user) {
        // 第一次提交审批
        if (processInstance == null) {
            //流程实例
            processInstance = new CmpApproveProcessInstance();
            //校验当前用户是否有当前节点的审批权限
            Long instanceId = DataUtil.generateId();
            submitProcessDTO.setInstanceId(instanceId);
            //流程实例业务流程ID
            processInstance.setBusinessId(submitProcessDTO.getBusinessId());
            //流程实例流程实例ID
            processInstance.setInstanceId(instanceId);
            //流程实例流程配置ID
            processInstance.setProcessId(submitProcessDTO.getProcessId());
            //流程实例流程版本号
            processInstance.setProcessVersionNum(submitProcessDTO.getBerv());
            //保存流程实例
            instanceService.save(processInstance);
            //创建开始节点
            CmpApproveProcessRecord startRecord = createNoApproveRecord(thisNode, user, processInstance.getInstanceId(), submitProcessDTO.getBusinessId());
            recordService.save(startRecord);
            //创建结束节点
            CmpApproveProcessRecord endRecord = createNoApproveRecord(lastNode, user, processInstance.getInstanceId(), submitProcessDTO.getBusinessId());
            recordService.save(endRecord);
        } else {
            //驳回修改再次提交,修改实例状态
            processInstance.setInstanceStatus(0);
            instanceService.saveOrUpdate(processInstance);
        }

        final CmpApproveProcessInstance processInstanceNew = processInstance;
        //保存审批流程记录
        List<NodesApproverJx> approvers = submitProcessDTO.getNextNodesApprover();
        approvers.forEach(approver -> {
            for (SysUser userApp : approver.getApproverUser()) {
                CmpApproveProcessNode node = approver.getNode();
                CmpApproveProcessRecord oldProcessRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                        .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                        .eq(CmpApproveProcessRecord::getInstanceId, submitProcessDTO.getInstanceId())
                        .eq(CmpApproveProcessRecord::getNodeId, node.getNodeId()).last("LIMIT 1")
                );

                if (oldProcessRecord == null) {
                    // 如果记录不存在,创建新的记录
                    CmpApproveProcessRecord processRecord = createNoApproveRecord(node, userApp, submitProcessDTO.getInstanceId(), submitProcessDTO.getBusinessId());
                    //保存审批流程记录
                    recordService.save(processRecord);
                    if (!MATERIAL.equals(submitProcessDTO.getApprovalType())) {
                        // 添加 Emis 待办
                        approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstanceNew, processRecord);
                    }
                    continue;
                }
                // 节点存在且不是经审批通过（有两种可以：未审批或者申请驳回）
                // 1.未审批
                if (oldProcessRecord.getDealStatus().equals(Integer.valueOf(0))) {
                    oldProcessRecord.setDealStatus(Integer.valueOf(DealStatus.PENDING));
                    //保存审批流程记录
                    recordService.saveOrUpdate(oldProcessRecord);
                    if (!MATERIAL.equals(submitProcessDTO.getApprovalType())) {
                        // 添加 Emis 待办
                        approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstanceNew, oldProcessRecord);
                    }
                    continue;
                }
                // 2. 如果原来的记录是审批驳回, 创建新记录，保留原来的记录
                CmpApproveProcessRecord processRecord = createNoApproveRecord(node, userApp, submitProcessDTO.getInstanceId(), submitProcessDTO.getBusinessId());
                //保存审批流程记录
                recordService.save(processRecord);
                if (!MATERIAL.equals(submitProcessDTO.getApprovalType())) {
                    // 添加 Emis 待办
                    approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstanceNew, processRecord);
                }
            }
        });
        return processInstance;
    }

    /**
     * 审批（通过或者驳回）入口
     *
     * @param submitProcessDTO
     * @return
     */
    @Override
    @Transactional
    public Long submitProcess(SubmitProcessJxDTO submitProcessDTO) {
        //流程实例
        Long instanceId = submitProcessDTO.getInstanceId();
        CmpApproveProcessInstance processInstance = instanceService.getById(instanceId);
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

        //加锁
        String lockKey;
        if (instanceId == null) {
            lockKey = INSTANCE_LOCK_KEY + DataUtil.generateId();
        } else {
            lockKey = INSTANCE_LOCK_KEY + instanceId;
        }

        String lockId = RedisUtils.getRedisLock(lockKey, LOCK_TIMEOUT);
        if (StringUtils.isBlank(lockId)) {
            McdException.throwMcdException(McdException.McdExceptionEnum.PROCESS_INSTANCE_APPROVAL);
        }
        try {
            subApproveProcess(submitProcessDTO, processInstance, operateLog);
        } finally {
            RedisUtils.releaseRedisLock(lockKey, lockId);
        }
        // operateLog.setUserComment("审批通过");
        operateLog.setCampsegId(processInstance.getBusinessId());
        if (!TASK.equals(submitProcessDTO.getApprovalType())) {
            operateLogService.save(operateLog);
        } else {
            McdCampOperateLog one = operateLogService.getOne(Wrappers.<McdCampOperateLog>query().lambda().eq(McdCampOperateLog::getCampsegId, submitProcessDTO.getBusinessId()));
            if (ObjectUtil.isEmpty(one)) {
                // 保存操作记录 (策略统筹子任务多次提交审批，操作记录只保存一次)
                operateLogService.save(operateLog);
            } else {
                // 更新入库时间内
                operateLogService.update(Wrappers.<McdCampOperateLog>update().lambda().set(McdCampOperateLog::getLogTime, new Date()).eq(McdCampOperateLog::getCampsegId, submitProcessDTO.getBusinessId()));
            }
        }
        return instanceId;
    }

    /**
     * 处理异常数据
     *
     * @param submitProcessDTO
     * @param instanceId
     */
    public  void checkAndHandelErrorData(SubmitProcessJxDTO submitProcessDTO, Long instanceId) {
        log.info("开始检查是否有异常数据");
        try {
            if (!submitProcessDTO.getSubmitStatus().equals(1)) {
                return;
            }
            //校验当前用户是否有当前节点的审批权限
            int count = recordService.count(Wrappers.<CmpApproveProcessRecord>query().lambda().eq(CmpApproveProcessRecord::getInstanceId, instanceId).eq(CmpApproveProcessRecord::getNodeType, 1)
                    //处理状态
                    .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING));
            log.info("instance:{},状态是0的记录有：{}条", submitProcessDTO.getInstanceId(), count);

            if (count <= 0) {
                count = recordService.count(Wrappers.<CmpApproveProcessRecord>query().lambda().eq(CmpApproveProcessRecord::getInstanceId, instanceId).eq(CmpApproveProcessRecord::getNodeType, 1)
                        //处理状态
                        .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.TO_SUBMIT));
                log.info("instance:{},状态是4的记录有：{}条", submitProcessDTO.getInstanceId(), count);

            }else {
                // 有状为0的数据，直接返回不处理
                return ;
            }
            if (count > 0) {
                UpdateWrapper<CmpApproveProcessRecord> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().eq(CmpApproveProcessRecord::getInstanceId, submitProcessDTO.getInstanceId()).eq(CmpApproveProcessRecord::getDealStatus, DealStatus.TO_SUBMIT).eq(CmpApproveProcessRecord::getNodeType, 1).set(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING);
                log.info("instance:{},已经更新所有状态为4的数据为 0", instanceId);
                recordService.update(updateWrapper);
            }
        } catch (Exception e) {
            log.error("checkAndHandelErrorData error: ", e);
        }
    }

    /**
     * 批量提交审批
     *
     * @param submitProcessDTO
     * @return
     */
    @Override
    public BatchApproveResult batchSubmitProcess(SubmitProcessJxDTO submitProcessDTO) {
        List<String> success = new ArrayList<>();
        List<String> fail = new ArrayList<>();
        for (int i = 0; i < submitProcessDTO.getInstanceIds().length; i++) {
            SubmitProcessJxDTO temp =  new SubmitProcessJxDTO();
            BeanUtil.copyProperties(submitProcessDTO,temp);
            temp.setBusinessId(submitProcessDTO.getBusinessIds()[i]);
            temp.setInstanceId(Long.valueOf(submitProcessDTO.getInstanceIds()[i]));
            try {
                Long instanceId = submitProcess(temp);
                log.error("提交审批成功:{}",JSONUtil.toJsonStr(temp));
                success.add(String.valueOf(instanceId));
            }catch (Exception e){
                log.error("提交审批失败:{}",JSONUtil.toJsonStr(temp),e);
                fail.add(String.valueOf(temp.getInstanceId()));
            }
        }
        BatchApproveResult result = new BatchApproveResult();
        result.setFail(fail);
        result.setSuccess(success);
        return result;
    }



    /**
     * 审批处理过程抽取该当
     *
     * @param submitProcessDTO
     * @param processInstance
     * @param operateLog
     * @return
     */
    public McdCampOperateLog subApproveProcess(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance processInstance, McdCampOperateLog operateLog) {

        //获取当前节点
        CmpApproveProcessNode thisNode = nodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                .eq(CmpApproveProcessNode::getProcessId, submitProcessDTO.getProcessId())
                .eq(CmpApproveProcessNode::getProcessVersionNum, submitProcessDTO.getBerv())
                .eq(CmpApproveProcessNode::getNodeId, submitProcessDTO.getNodeId()).last("LIMIT 1")
        );
        //获取当前登录用户
        User user = submitProcessDTO.getUser();
        // 用户对象信息转换
        SysUser sysUser = userConvert(user);
        //校验当前用户是否有当前节点的审批权限
        CmpApproveProcessRecord thisRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                //审批人Ids
                .eq(CmpApproveProcessRecord::getApprover, user.getUserId())
                //流程实例ID
                .eq(CmpApproveProcessRecord::getInstanceId, submitProcessDTO.getInstanceId())
                .eq(CmpApproveProcessRecord::getNodeId, submitProcessDTO.getNodeId())
                //处理状态
                .in(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING));
        log.info("888888880001:{}",JSONUtil.toJsonStr(thisRecord));
        if (thisRecord == null) {
            //无权限抛异常
            McdException.throwMcdException(McdException.McdExceptionEnum.APPROVE_SUBMIT_USER_NO_ERROR);
        }
        //如果是初始化审批人了  就不要执行
        //下节点的审批人
        List<NodesApproverJx> nextNodesApprover = submitProcessDTO.getNextNodesApprover();

        //查询活动触发条件
        if (submitProcessDTO.getTriggerParm() == null) {
            setTriggerParam(submitProcessDTO);
        }
        //审批意见
        String dealOpinion = submitProcessDTO.getDealOpinion();
        thisRecord.setDealOpinion(dealOpinion);
        //通过审批
        UpdateCampStatusDTO updateCampStatusDTO = new UpdateCampStatusDTO();
        MaterialStatusQuery materialStatusQuery = new MaterialStatusQuery();
        CampCoordinationStatusQuery statusQuery = new CampCoordinationStatusQuery();
        TermApproveStatus termApproveStatus = new TermApproveStatus();
        ModifyCareSmsTemplateStatusParam careSmsTemplateStatusParam = new ModifyCareSmsTemplateStatusParam();
        ModifyCalloutRuleStatusParam calloutRuleStatusParam = new ModifyCalloutRuleStatusParam();
        calloutRuleStatusParam.setScenarioId(processInstance.getBusinessId());
        ModifyBlacklistApprStatusParam blacklistApprStatusParam = new ModifyBlacklistApprStatusParam();
        blacklistApprStatusParam.setTaskId(processInstance.getBusinessId());
        String approveName = CollectionUtil.isEmpty(nextNodesApprover) ? "" : (ObjectUtil.isNotNull(nextNodesApprover.get(0).getApproverUser()) ? nextNodesApprover.get(0).getApproverUser().get(0).getUserName():"");
        blacklistApprStatusParam.setApproverName(approveName);
        careSmsTemplateStatusParam.setApprovalStatus(41);
        careSmsTemplateStatusParam.setTemplateCode(processInstance.getBusinessId());
        termApproveStatus.setStatus(4);
        termApproveStatus.setTermId(processInstance.getBusinessId());
        statusQuery.setChildTaskIds(submitProcessDTO.getChildBusinessId());
        statusQuery.setTaskId(processInstance.getBusinessId());
        updateCampStatusDTO.setCampsegRootId(processInstance.getBusinessId());
        updateCampStatusDTO.setFlowId(processInstance.getInstanceId().toString());
        materialStatusQuery.setFlowId(processInstance.getInstanceId().toString());
        statusQuery.setFlowId(processInstance.getInstanceId().toString());
        if (submitProcessDTO.getSubmitStatus().equals(1)) {
            //通过审批
            processApproved(processInstance, thisNode, thisRecord, sysUser, nextNodesApprover, submitProcessDTO.getTriggerParm(), submitProcessDTO);
            operateLog.setUserComment("审批通过");
            operateLog.setLogDesc("审批通过");
            if(HMH5_BLACKLIST.equals(submitProcessDTO.getApprovalType())) {
                blacklistApprStatusParam.setApprovalStatus(1);
                blacklistFeignClient.modifyBlacklistApprStatus(blacklistApprStatusParam);
            }
            if(HMH5_CALLOUT_RULE.equals(submitProcessDTO.getApprovalType())){
                calloutRuleStatusParam.setApprovalStatus("5");
                calloutRuleFeignClient.modifyCalloutRuleStatus(calloutRuleStatusParam);
            }
        } else {
            //驳回审批
            approvalRejected(processInstance, thisNode, thisRecord, user, submitProcessDTO);
            operateLog.setUserComment("审批驳回");
            operateLog.setLogDesc("审批驳回");
            updateCampStatusDTO.setCampStat(41);
            materialStatusQuery.setMaterialStat(2);
            statusQuery.setExecStatus(41);
            if (MATERIAL.equals(submitProcessDTO.getApprovalType())) {
                elementMatermialJxService.updateMaterialStatus(materialStatusQuery);
            } else if(TERM.equals(submitProcessDTO.getApprovalType())) {
                termFeignClient.updateTaskStatus(termApproveStatus);
            } else if(MESSAGE_TEMPLATE.equals(submitProcessDTO.getApprovalType()) || BATTLE_MAP.equals(submitProcessDTO.getApprovalType())) {
                khtCareSmsTemplateFeignClient.modifyCareSmsTemplateStatus(careSmsTemplateStatusParam);
            } else if(HMH5_BLACKLIST.equals(submitProcessDTO.getApprovalType())) {
                blacklistApprStatusParam.setApprovalStatus(2);
                blacklistFeignClient.modifyBlacklistApprStatus(blacklistApprStatusParam);
            } else if(HMH5_CALLOUT_RULE.equals(submitProcessDTO.getApprovalType())) {
                calloutRuleStatusParam.setApprovalStatus("4");
                calloutRuleFeignClient.modifyCalloutRuleStatus(calloutRuleStatusParam);
            } else if (IMCD.equals(submitProcessDTO.getApprovalType())) {
                planService.updateCampStatus(updateCampStatusDTO);
            } else if (CustomLabelFeignClient.CUSTOM_LABEL.equals(submitProcessDTO.getApprovalType())) { // 2023-07-07 17:18:06 添加自定义标签驳回时修改状态
                customLabelFeignClient.modifyLabelStatus(
                        new ModifySelfLabelStatusParam(Integer.valueOf(processInstance.getBusinessId()), 53, String.valueOf(processInstance.getInstanceId()))
                );
            } else if (CustomAlarmFeignClient.CUSTOM_ALARM.equals(submitProcessDTO.getApprovalType())) { // 2023-07-07 17:18:06 添加自定义预警驳回时修改状态
                customAlarmFeignClient.modifyAlarmStatus(
                        new ModifyAlarmStatusParam(Integer.valueOf(processInstance.getBusinessId()), 4, String.valueOf(processInstance.getInstanceId()))
                );
            } else {
                // 策略统筹任务--更改主任务、子任务状态
                mcdCampCoordinationFeignClient.updateTaskStatus(statusQuery);
            }
        }
        return operateLog;
    }

    /**
     * 设置当前审批实例的触发条件
     *
     * @param submitProcessDTO
     */
    @Override
    public   void setTriggerParam(SubmitProcessJxDTO submitProcessDTO) {
        if (null != submitProcessDTO.getTriggerParm()) {
            return;
        }
        if (ConstApprove.APPROVE_TYPE_MATERIAL.equals(submitProcessDTO.getApprovalType())) {
            // channelId
            LambdaQueryWrapper<McdDimMaterialJxModel> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(McdDimMaterialJxModel::getMaterialId, submitProcessDTO.getBusinessId());
            List<McdDimMaterialJxModel> channelLists = mcdDimMaterialJxService.list(queryWrapper);
            Map<String, Object> tiggerMap = new HashMap<>();
            StringBuffer channels = new StringBuffer();
            channelLists.forEach(channel -> {
                channels.append(channel.getChannelId() + ",");
            });
            tiggerMap.put(ConstApprove.TRIGGER_CHANNEL_ID, channels);
            JSONObject obj = new JSONObject();
            obj.putAll(tiggerMap);
            submitProcessDTO.setTriggerParm(obj);
            return;
        }
        if (ConstApprove.APPROVE_TYPE_CAMP.equals(submitProcessDTO.getApprovalType())) {
            setImcdTriggerParam(submitProcessDTO);
            return;
        }
        Map<String, Object> tiggerMap = new HashMap<>();
        tiggerMap.put(ConstApprove.TRIGGER_CHANNEL_ID, "");
        JSONObject obj = new JSONObject();
        obj.putAll(tiggerMap);
        submitProcessDTO.setTriggerParm(obj);
    }

    /**
     * 策略审批触发条件设置
     * @param submitProcessDTO
     */
    private void setImcdTriggerParam(SubmitProcessJxDTO submitProcessDTO){
        // channelId
        LambdaQueryWrapper<McdCampChannelList> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(McdCampChannelList::getCampsegRootId, submitProcessDTO.getBusinessId());
        List<McdCampChannelList> channelLists = campChannelListService.list(queryWrapper);
        // 802 811  外呼类型
        Map<String, Object> tiggerMap = new HashMap<>();
        StringBuffer channels = new StringBuffer();
        int i = 0;
        for (McdCampChannelList channel : channelLists) {
            if( i > 0){
                channels.append( ",");
            }
            channels.append(channel.getChannelId());
            i++;
            // 10085外呼
            if (ConstApprove.CHN_802.equals(channel.getChannelId())) {
                List<Map<String, Object>> campExtList = campDefService.queryCampExtByCampId(channel.getCampsegId());
                if (CollectionUtil.isEmpty(campExtList)) {
                    tiggerMap.put(ConstApprove.TRIGGER_CALL_TYPE, ConstApprove.TRIGGER_VALUE_1);
                    continue;
                }
                Object ext3 = campExtList.get(0).get(ConstApprove.COLUMN_EXT3);
                if (null == ext3) {
                    // 扩展字段为空是 默认 callType 为 1
                    tiggerMap.put(ConstApprove.TRIGGER_CALL_TYPE, ConstApprove.TRIGGER_VALUE_1);
                    continue;
                }
                // 调研类 callType为 2，
                // 其它为 1
                if (ext3.toString().equals(ConstApprove.TRIGGER_CALL_NAME_2)) {
                    tiggerMap.put(ConstApprove.TRIGGER_CALL_TYPE, ConstApprove.TRIGGER_VALUE_2);
                }  else {
                    tiggerMap.put(ConstApprove.TRIGGER_CALL_TYPE, ConstApprove.TRIGGER_VALUE_1);
                }
            }
            // 10088外呼
            if (ConstApprove.CHN_811.equals(channel.getChannelId())) {
                String channelAdivId = channel.getChannelAdivId();
                // 高危预警运营位 whPosition 为 1,其它为2
                if (ConstApprove.CHN_ADIV_811_1.equals(channelAdivId)) {
                    tiggerMap.put(ConstApprove.TRIGGER_WH_POSITON, ConstApprove.TRIGGER_VALUE_1);
                } else {
                    tiggerMap.put(ConstApprove.TRIGGER_WH_POSITON, ConstApprove.TRIGGER_VALUE_2);
                }
            }
        }
        tiggerMap.put(ConstApprove.TRIGGER_CHANNEL_ID, channels);
        // 策略类型
        List<McdCampDef> campDefList = campDefService.listByCampsegRootId(submitProcessDTO.getBusinessId());
        //（IOP）策略类型 1：策略类 2：调研类
        tiggerMap.put(ConstApprove.TRIGGER_CAMP_TYPE, campDefList.get(0).getCampsegTypeId());
        JSONObject obj = new JSONObject();
        obj.putAll(tiggerMap);
        submitProcessDTO.setTriggerParm(obj);
    }


    /**
     * 审批通过后的操作
     *
     * @param processInstance   流程实例
     * @param thisNode          当前节点
     * @param thisRecord        当前节点实例
     * @param user              当前登录用户
     * @param nextNodesApprover 下节点的审批人
     * @param triggerParm       触发条件
     */
    private void processApproved(CmpApproveProcessInstance processInstance, CmpApproveProcessNode thisNode, CmpApproveProcessRecord thisRecord, SysUser user, List<NodesApproverJx> nextNodesApprover, JSONObject triggerParm, SubmitProcessJxDTO submitProcessDTO) {


        //修改流程实例为审批中
        processInstance.setInstanceStatus(Integer.parseInt(InstanceStatus.APPROVAL));

        //记录下节点的状态 是待提交还是待处理  false为待处理 true为待提交
        //ps:待处理即流程流转到下节点 下节点审批人可以进行审批
        //ps:待提交为流程流转到下节点 但是当前节点还有其他人需要会审或当前节点还有其他的分支需要审批到该节点
        boolean flag = false;

        //查询当前节点是否是多人在审批，其余审批人是否为待审
        int approverRecordCount = recordService.count(Wrappers.<CmpApproveProcessRecord>query().lambda()
                .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                .eq(CmpApproveProcessRecord::getNodeId, thisRecord.getNodeId())
                .ne(CmpApproveProcessRecord::getApprover, thisRecord.getApprover())
                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING)
        );
        if (approverRecordCount != 0) {
            if (thisNode.getMultipleChoiceType().toString().equals(MultipleChoiceType.PARALLEL)) {
                //并行审批 当前节点审批人审批通过 删除其他审批人的记录
                recordService.remove(Wrappers.<CmpApproveProcessRecord>query().lambda()
                        .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                        .eq(CmpApproveProcessRecord::getNodeId, thisRecord.getNodeId())
                        .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING)
                );
            } else {
                //会审  当前审批操作完成
                flag = true;
            }
        }

        //实际
        //轮询下节点list 校验触发条件 遇到事件节点将事件节点的下一个节点加到list中
        //后续的节点  如果是驳回节点 且审批类型为继续审批，则下一节点直接是审批的那个节点
        List<CmpApproveProcessNode> nodeList = new ArrayList<>();
        //CmpApprovalProcess:审批流程模板主表
        CmpApprovalProcess approvalProcess = processService.getById(processInstance.getProcessId());
        if (approvalProcess == null) {
            McdException.throwMcdException(McdException.McdExceptionEnum.PROCESS_INSTANCE_DOES_NOT_EXIST);
        }
        //流程模板为继续审批  流程实例的状态为被驳回 当前节点实例存在上节点实例id
        log.info("------" + thisRecord.getPreRecordId() + "---------" + approvalProcess.getProcessType() + " 1重新审批;0继续审批" + "-------------" + processInstance.getInstanceStatus() + "-------------  修改其余待提交审批节点节点------------------------------");
        if (approvalProcess.getProcessType().toString().equals(ProcessType.CONTINUE_APPROVE) &&
                processInstance.getInstanceStatus() != null && processInstance.getInstanceStatus().toString().equals(InstanceStatus.APPROVAL_REJECTED)) {
            processInstance.setInstanceStatus(Integer.parseInt(InstanceStatus.APPROVAL));
            //继续审批  修改其余待提交审批节点节点
            List<CmpApproveProcessRecord> processRecords = recordService.list(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                    .in(CmpApproveProcessRecord::getDealStatus, DealStatus.TO_SUBMIT, DealStatus.REJECT)
                    .eq(CmpApproveProcessRecord::getNodeType, NodeType.APPROVAL_NODE)
            );
            thisRecord.setDealStatus(0);
            if (!processRecords.isEmpty()) {
                /*processRecords.forEach(record -> record.setDealStatus(DealStatus.PENDING));
                recordService.updateBatchById(processRecords);*/
                for (CmpApproveProcessRecord processRecord : processRecords) {
                    processRecord.setDealStatus(Integer.parseInt(DealStatus.PENDING));
                    processRecord.setDealTime(new Date());
                    recordService.saveOrUpdate(processRecord);
                }
            }

            //查询审批驳回的哪个节点 直接创建驳回节点的record
            CmpApproveProcessRecord preRecord = recordService.getById(thisRecord.getPreRecordId());
            CmpApproveProcessNode processNode = nodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                    .eq(CmpApproveProcessNode::getProcessId, thisNode.getProcessId())
                    .eq(CmpApproveProcessNode::getNodeId, preRecord.getNodeId())
                    .eq(CmpApproveProcessNode::getProcessVersionNum, processInstance.getProcessVersionNum())
            );
            nodeList.add(processNode);
        } else {
            nodeList.addAll(nodeService.list(Wrappers.<CmpApproveProcessNode>query().lambda()
                    .eq(CmpApproveProcessNode::getProcessId, thisNode.getProcessId())
                    .eq(CmpApproveProcessNode::getProcessVersionNum, thisNode.getProcessVersionNum())
                    .in(CmpApproveProcessNode::getNodeId, Arrays.asList(thisNode.getNextNodeId().split(",")))
            ));
        }

        //后续的正常节点
        List<CmpApproveProcessNode> nextNodeList = new LinkedList<>();
        //后续的事件节点
        LinkedList<CmpApproveProcessNode> nextEventNodeList = new LinkedList<>();

        //查询当前节点的下级节点
        queryNextNode(nodeList, nextNodeList, nextEventNodeList, thisNode, approverRecordCount, triggerParm);


        //查询当前流程实例其他节点待审批的record  如果有则代表存在其他的分支正在进行审批
        //如此其他分支在审批
        int count = recordService.count(Wrappers.<CmpApproveProcessRecord>query().lambda()
                .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                .ne(CmpApproveProcessRecord::getNodeId, thisRecord.getNodeId())
                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING)
                .eq(CmpApproveProcessRecord::getNodeType, 1)
        );
        if (count > 0) {
            flag = true;
        }

        //创建下节点的record
        LinkedList<CmpApproveProcessRecord> nextRecordList = createNextRecord(thisRecord, user, nextNodeList, nextNodesApprover, processInstance, flag);
        //创建事件record

        LinkedList<CmpApproveProcessRecord> nextEventRecord = createEventRecord(thisRecord, nextEventNodeList, flag);
        if (!nextEventRecord.isEmpty()) {
            log.info("----------------nextRecordList-------------------" + nextRecordList);
            log.info("----------------nextEventRecord-------------------" + nextEventRecord);
            if (!TASK.equals(submitProcessDTO.getApprovalType())) {
                recordService.saveOrUpdateBatch(nextEventRecord);
            } else {
                CmpApproveProcessRecord recordServiceOne = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                        .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                        .eq(CmpApproveProcessRecord::getNodeType, 3));
                if (ObjectUtil.isEmpty(recordServiceOne)) {
                    recordService.saveOrUpdateBatch(nextEventRecord);
                } else {
                    recordService.update(Wrappers.<CmpApproveProcessRecord>lambdaUpdate()
                            .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                            .eq(CmpApproveProcessRecord::getNodeType, 3)
                            .set(CmpApproveProcessRecord::getModifyDate, new Date()));
                }
            }

            //事件执行

            // 2023-07-06 18:16:38 修改相应活动的状态
            if (TASK.equals(submitProcessDTO.getApprovalType())) {
                // 策略统筹任务--调用事件节点处理方法
                coordinationTaskInvokeEvent(nextEventRecord, submitProcessDTO);
            } else {
                List<CmpApproveProcessRecord> listEvtRecord = new ArrayList<>();
                log.info("====================进入事件=============");
                for (CmpApproveProcessRecord evtRecord : nextEventRecord) {
                    String evtRecordNodeId = evtRecord.getNodeId();
                    log.info("【事件】evtRecordNodeId:{}",evtRecordNodeId);
                    QueryWrapper<CmpApproveProcessInstance> queryWrapperInstance = new QueryWrapper<>();
                    queryWrapperInstance.lambda().eq(CmpApproveProcessInstance::getInstanceId, evtRecord.getInstanceId());
                    CmpApproveProcessInstance instance = instanceService.getOne(queryWrapperInstance);
                    Long processId = instance.getProcessId();
                    log.info("【事件】processId:{}",processId);
                    QueryWrapper<CmpApproveProcessNode> queryWrapperNode = new QueryWrapper<>();
                    queryWrapperNode.lambda().eq(CmpApproveProcessNode::getNextNodeId, evtRecordNodeId)
                            .eq(CmpApproveProcessNode::getProcessId, processId)
                            .eq(CmpApproveProcessNode::getNodeType, 1);
                    List<CmpApproveProcessNode> nodes = nodeService.list(queryWrapperNode);
                    log.info("【事件】processId:{}",processId);
                    List<String> nodeIds = new ArrayList<>();
                    for (CmpApproveProcessNode node : nodes) {
                        nodeIds.add(node.getNodeId());
                    }

                    QueryWrapper<CmpApproveProcessRecord> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(CmpApproveProcessRecord::getInstanceId, evtRecord.getInstanceId())
                            .eq(CmpApproveProcessRecord::getNodeType, 1)
                            // .in(CmpApproveProcessRecord::getNodeId, nodeIds)
                            .eq(CmpApproveProcessRecord::getDealStatus, 4)
                            .orderByAsc(CmpApproveProcessRecord::getCreateDate);
                    List<CmpApproveProcessRecord> list = recordService.list(queryWrapper);
                    log.info("【事件】状态为4的节点:{}", JSONUtil.toJsonStr(list));
                    if (CollectionUtil.isNotEmpty(list)) {
                        CmpApproveProcessRecord cmpApproveProcessRecord = list.get(0);
                        UpdateWrapper<CmpApproveProcessRecord> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.lambda().set(CmpApproveProcessRecord::getDealStatus, 0).eq(CmpApproveProcessRecord::getId, cmpApproveProcessRecord.getId());
                        recordService.update(updateWrapper);
                        log.info("【事件】状态为更新成功", JSONUtil.toJsonStr(list));
                        cmpApproveProcessRecord.setDealStatus(0);
                        // 添加待办
                        approvePostProcessor.approvalPendingProcess(submitProcessDTO,processInstance,cmpApproveProcessRecord);

                        // 将当前节点状态改为已办
                        updateWrapper = new UpdateWrapper<>();
                        updateWrapper.lambda().set(CmpApproveProcessRecord::getDealStatus, 1).eq(CmpApproveProcessRecord::getId, thisRecord.getId());
                        recordService.update(updateWrapper);
                        // 待办转已办
                        thisRecord.setDealStatus(1);
                        approvePostProcessor.approvedProcess(submitProcessDTO, processInstance, thisNode, thisRecord, nextNodeList, nextRecordList);
                        log.info("【事件】添加emis待办成功", JSONUtil.toJsonStr(list));
                    } else {
                        listEvtRecord.add(evtRecord);
                    }
                }
                if (CollectionUtil.isNotEmpty(listEvtRecord)) {
                    invokeEvent(listEvtRecord);
                } else {
                    return;
                }
            }

            //获取终止节点
            CmpApproveProcessNode lastNode = nodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                    .eq(CmpApproveProcessNode::getProcessId, submitProcessDTO.getProcessId())
                    .eq(CmpApproveProcessNode::getProcessVersionNum, submitProcessDTO.getBerv())
                    .eq(CmpApproveProcessNode::getNodeType, 2).last("LIMIT 1")
            );
            CmpApproveProcessRecord endRecord = createNoApproveRecord(lastNode, user, thisRecord.getInstanceId(), submitProcessDTO.getBusinessId());
            endRecord.setDealStatus(1);
            if (!TASK.equals(submitProcessDTO.getApprovalType())) {
                //修改流程实例为审批完成
                processInstance.setInstanceStatus(Integer.parseInt(InstanceStatus.APPROVAL_COMPLETED));
                // 结束节点处理
                recordService.saveOrUpdate(endRecord);
            } else {
                ActionResponse actionResponse = chkApprovingTask(submitProcessDTO);
                if (ResponseStatus.SUCCESS.equals(actionResponse.getStatus())) {
                    // 修改流程实例为审批完成
                    processInstance.setInstanceStatus(Integer.parseInt(InstanceStatus.APPROVAL_COMPLETED));
                    // 删除===》修改 mcd_emis_task 状态
                    mcdEmisTaskService.remove(Wrappers.<McdEmisTask>lambdaQuery().eq(McdEmisTask::getCampsegId, submitProcessDTO.getBusinessId()));
                    // 删除待办
                    approvePostProcessor.rejectProcess(submitProcessDTO, processInstance, thisRecord);
                }

                // 结束节点处理
                CmpApproveProcessRecord recordServiceOne = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                        .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                        .eq(CmpApproveProcessRecord::getNodeType, 2));
                if (ObjectUtil.isEmpty(recordServiceOne)) {
                    recordService.saveOrUpdate(endRecord);
                } else {
                    recordService.update(Wrappers.<CmpApproveProcessRecord>lambdaUpdate()
                            .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                            .eq(CmpApproveProcessRecord::getNodeType, 2)
                            .set(CmpApproveProcessRecord::getModifyDate, new Date()));
                }
            }
        }
        instanceService.saveOrUpdate(processInstance);
        if (!TASK.equals(submitProcessDTO.getApprovalType())) {
            thisRecord.setDealStatus(1);
        } else {
            // 判断策略统筹主任务下面是否还有审批中的子任务 没有才更改 DEAL_STATUS=1 通过
            // 注：需要在上述 coordinationTaskInvokeEvent 方法执行更改完任务状态后再判断
            ActionResponse actionResponse = chkApprovingTask(submitProcessDTO);
            if (ResponseStatus.SUCCESS.equals(actionResponse.getStatus())) {
                thisRecord.setDealStatus(1);
            }
        }
        thisRecord.setDealTime(new Date());
        log.info("88888888:{}",JSONUtil.toJsonStr(thisRecord));
        recordService.saveOrUpdate(thisRecord);


        if (approvalProcess.getInitApprover() != 1) {
            //对汇总节点审批状态处理
            for (CmpApproveProcessRecord recordInfo : nextRecordList) {
                // 2023-07-11 16:44:40 如果不是自定义预警或者标签的审批，才执行下述代码
                if (!Arrays.asList(CustomLabelFeignClient.CUSTOM_LABEL, CustomAlarmFeignClient.CUSTOM_ALARM).contains(submitProcessDTO.getApprovalType())) {
                    LambdaQueryWrapper<McdCampChannelList> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(McdCampChannelList::getCampsegRootId, submitProcessDTO.getBusinessId());
                    List<McdCampChannelList> channelLists = campChannelListService.list(queryWrapper);
                    //判断是否多渠道审批活动
                    if (channelLists != null && channelLists.size() <= 1) {
                        continue;
                    }
                }
                //获取审批节点
                List<CmpApproveProcessNode> processNodeList = nodeService.list(Wrappers.<CmpApproveProcessNode>query().lambda()
                        .eq(CmpApproveProcessNode::getProcessId, submitProcessDTO.getProcessId())
                        .eq(CmpApproveProcessNode::getNextNodeId, recordInfo.getNodeId())
                        .eq(CmpApproveProcessNode::getNodeType, 1));
                if (processNodeList != null && processNodeList.size() > 1) {
                    int num = 0;
                    for (CmpApproveProcessNode node : processNodeList) {
                        CmpApproveProcessRecord processRecord1 = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                                .eq(CmpApproveProcessRecord::getInstanceId, recordInfo.getInstanceId())
                                .eq(CmpApproveProcessRecord::getNodeId, node.getNodeId())
                                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING));

                        //上级节点还有未审批完的
                        if (processRecord1 != null) {
                            recordInfo.setDealStatus(Integer.parseInt(DealStatus.TO_SUBMIT));
                        }

                        CmpApproveProcessRecord processRecord2 = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                                .eq(CmpApproveProcessRecord::getInstanceId, recordInfo.getInstanceId())
                                .eq(CmpApproveProcessRecord::getNodeId, node.getNodeId())
                                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PASS));
                        if (processRecord2 != null) {
                            ++num;
                        }

                    }
                    //上级节点还有未审批完的
                    if (num <= 1) {
                        recordInfo.setDealStatus(Integer.parseInt(DealStatus.TO_SUBMIT));
                    }
                }
            }

            recordService.saveOrUpdateBatch(nextRecordList);
        }
        if (!MATERIAL.equals(submitProcessDTO.getApprovalType())) {
            if (TASK.equals(submitProcessDTO.getApprovalType())) {
                // 统筹任务单独处理
                // 判断该主任务下是否还有审批中状态的子任务
                ActionResponse actionResponse = chkApprovingTask(submitProcessDTO);
                if (ResponseStatus.SUCCESS.equals(actionResponse.getStatus())) {
                    // 调用emis待办,将代办转已办
                    approvePostProcessor.approvedProcess(submitProcessDTO, processInstance, thisNode, thisRecord, nextNodeList, nextRecordList);
                }
            } else {
                // 调用emis待办,将代办转已办
                approvePostProcessor.approvedProcess(submitProcessDTO, processInstance, thisNode, thisRecord, nextNodeList, nextRecordList);
            }
        }

    }


    /**
     * 驳回后的操作
     *
     * @param processInstance 流程实例
     * @param thisNode        当前节点
     * @param thisRecord      当前节点实例record
     * @param user            当前节点审批人
     */
    @Override
    public void approvalRejected(CmpApproveProcessInstance processInstance, CmpApproveProcessNode thisNode, CmpApproveProcessRecord thisRecord, User user, SubmitProcessJxDTO submitProcessDTO) {
        //设置流程实例的状态为被驳回
        if (!TASK.equals(submitProcessDTO.getApprovalType())) {
            processInstance.setInstanceStatus(3);
            instanceService.updateById(processInstance);
            //修改当前节点实例为驳回
            thisRecord.setDealStatus(2);
            thisRecord.setDealTime(new Date());
            recordService.updateById(thisRecord);
        }

        if (!MATERIAL.equals(submitProcessDTO.getApprovalType())) {
            if (!TASK.equals(submitProcessDTO.getApprovalType())) {
                // 取消emis待办
                approvePostProcessor.rejectProcess(submitProcessDTO, processInstance, thisRecord);
            } else {
                // 策略统筹--判断是否还有"审批中"状态的任务
                ActionResponse actionResponse = chkApprovingTask(submitProcessDTO);
                if (ResponseStatus.SUCCESS.equals(actionResponse.getStatus())) { // 没有
                    // 1. 修改实例表状态为审批驳回
                    processInstance.setInstanceStatus(3);
                    instanceService.updateById(processInstance);
                    // 删除===》修改 mcd_emis_task 状态
                    mcdEmisTaskService.remove(Wrappers.<McdEmisTask>lambdaQuery().eq(McdEmisTask::getCampsegId, submitProcessDTO.getBusinessId()));
                    // 2. 取消emis待办
                    approvePostProcessor.rejectProcess(submitProcessDTO, processInstance, thisRecord);
                    // 3. 修改子任务状态为审批驳回
                    updateChildTaskReject(submitProcessDTO);
                } else { // 有审批中状态子任务
                    // 1. 修改子任务状态为审批驳回
                    updateChildTaskReject(submitProcessDTO);
                }
            }
        }


        //其余节点上的待审批节点 如果是重新审批则删除 如果是继续审批则修改为待提交
        CmpApprovalProcess process = processService.getById(thisNode.getProcessId());
        List<CmpApproveProcessRecord> cmpApproveProcessRecords = recordService.list(Wrappers.<CmpApproveProcessRecord>query().lambda()
                .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING)
                .ne(CmpApproveProcessRecord::getNodeId, thisRecord.getNodeId())
        );

        //当前节点其余审批人的 未审批的record删除 审批通过的保留
        recordService.remove(Wrappers.<CmpApproveProcessRecord>query().lambda()
                .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                .eq(CmpApproveProcessRecord::getNodeId, thisRecord.getNodeId())
                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING)
                .ne(CmpApproveProcessRecord::getApprover, thisRecord.getApprover())
        );

//      0：重新提交审批时上次已经审批通过的节点不再审批
//      1（默认值）：重新提交审批时所有节点需要重新审批
        if (process.getProcessType().toString().equals(ProcessType.CONTINUE_APPROVE)) {
            if (!cmpApproveProcessRecords.isEmpty()) {
                cmpApproveProcessRecords.forEach(record -> record.setDealStatus(Integer.parseInt(DealStatus.TO_SUBMIT)));
                recordService.updateBatchById(cmpApproveProcessRecords);
            }
        } else {
            recordService.remove(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                    .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING)
                    .ne(CmpApproveProcessRecord::getNodeId, thisRecord.getNodeId())
            );
        }
        // 删除emis待办
        if (!cmpApproveProcessRecords.isEmpty() && !MATERIAL.equals(submitProcessDTO.getApprovalType())) {
            cmpApproveProcessRecords.forEach(record -> {
                record.setDealStatus(Integer.parseInt(DealStatus.TO_SUBMIT));
                approvePostProcessor.rejectProcess(submitProcessDTO, processInstance, record);
            });
        }
        // 客户通渠道活动&自定义预警驳回增加EMIS阅知待办
        approveRrejectSendEmis(processInstance, thisRecord, submitProcessDTO);
    }

    /**
     * 客户通渠道活动&自定义预警驳回增加EMIS阅知待办
     *
     * @param processInstance 审批流程信息
     * @param thisRecord 当前节点
     * @param submitProcessDTO 入参对象
     */
    private void approveRrejectSendEmis(CmpApproveProcessInstance processInstance, CmpApproveProcessRecord thisRecord, SubmitProcessJxDTO submitProcessDTO) {
        try {
            if (CustomAlarmFeignClient.CUSTOM_ALARM.equals(submitProcessDTO.getApprovalType())) {
                log.info("预警id={},自定义预警审批驳回准备发送emis阅知待办", submitProcessDTO.getBusinessId());
                approvePostProcessor.rejectProcessAndSendEmis(submitProcessDTO, processInstance, thisRecord);
            } else {
                if (ConstApprove.APPROVE_TYPE_CAMP.equals(submitProcessDTO.getApprovalType())) {
                    // 根据根id查询渠道id
                    LambdaQueryWrapper<McdCampChannelList> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(McdCampChannelList::getCampsegRootId, submitProcessDTO.getBusinessId());
                    List<McdCampChannelList> channelLists = campChannelListService.list(queryWrapper);
                    for (McdCampChannelList channelList : channelLists) {
                        if (ConstApprove.CHN_809.equals(channelList.getChannelId())) { // 客户通渠道
                            log.info("活动根id={},渠道={}审批驳回准备发送emis阅知待办", submitProcessDTO.getBusinessId(), channelList.getChannelId());
                            approvePostProcessor.rejectProcessAndSendEmis(submitProcessDTO, processInstance, thisRecord);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("活动={}审批驳回准备发送emis阅知待办异常：", submitProcessDTO.getBusinessId(), e);
        }
    }


    /**
     * 更新子任务为驳回--41
     *
     * @param submitProcessDTO submitProcessDTO
     */
    private void updateChildTaskReject(SubmitProcessJxDTO submitProcessDTO) {
        CampCoordinationStatusQuery query = new CampCoordinationStatusQuery();
        query.setTaskId(submitProcessDTO.getBusinessId());
        query.setChildTaskIds(submitProcessDTO.getChildBusinessId());
        query.setExecStatus(41);
        mcdCampCoordinationFeignClient.updateChildTask(query);
    }

    /**
     * 更新主任务状态为审批通过--54
     *
     * @param submitProcessDTO submitProcessDTO
     */
    private void updateMainTaskComplete(SubmitProcessJxDTO submitProcessDTO) {
        CampCoordinationStatusQuery query = new CampCoordinationStatusQuery();
        query.setTaskId(submitProcessDTO.getBusinessId());
        query.setExecStatus(54);
        mcdCampCoordinationFeignClient.updateMainTaskStat(query);
    }


    /**
     * @param nodeList            当前节点的下级节点（下级节点可能是事件或其他可能）
     * @param nextNodeList        本次请求真实创建流转到的非事件节点
     * @param nextEventNodeList   本次请求真实创建流转到的事件节点
     * @param thisNode            流程实例当前所在节点
     * @param approverRecordCount 当前节点其他热在审的数量（=0代表当前人什么后当前节点一定是通过了的）
     * @param triggerParm         节点触发条件
     */
    private void queryNextNode(List<CmpApproveProcessNode> nodeList, List<CmpApproveProcessNode> nextNodeList, LinkedList<CmpApproveProcessNode> nextEventNodeList, CmpApproveProcessNode thisNode, int approverRecordCount, JSONObject triggerParm) {
        nodeList.forEach(node -> {
            if (node.getNodeType().toString().equals(NodeType.EVENT_NODE)) {
                //当前节点后续的事件节点record创建及事件执行需等当前节点会审通过  如果节点是并行节点的话，直接执行事件
                if (approverRecordCount == 0 || thisNode.getMultipleChoiceType().toString().equals(MultipleChoiceType.PARALLEL)) {
                    nextEventNodeList.add(node);
                    //事件节点查后续的事件节点
                    List<CmpApproveProcessNode> list = nodeService.list(Wrappers.<CmpApproveProcessNode>query().lambda()
                            .eq(CmpApproveProcessNode::getProcessId, thisNode.getProcessId())
                            .eq(CmpApproveProcessNode::getProcessVersionNum, thisNode.getProcessVersionNum())
                            .in(CmpApproveProcessNode::getNodeId, Arrays.asList(node.getNextNodeId().split(",")))
                    );
                    queryNextNode(list, nextNodeList, nextEventNodeList, thisNode, approverRecordCount, triggerParm);
                }
            } else if (!node.getNodeType().toString().equals(NodeType.END_NODE)) {
                //审批节点校验触发条件
                boolean checkData = checkTriggerParm(node, triggerParm);
                if (checkData) {
                    nextNodeList.add(node);
                }
            }
        });
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
            record.setDealStatus(Integer.parseInt(DealStatus.TO_SUBMIT));
            record.setDealOpinion("审批完成");
        } else {
            record.setDealOpinion("执行事件");
        }
        return record;
    }

    /**
     * 传入的触发条件只要满足最小的颗粒度即代表校验通过
     *
     * @param node        节点信息
     * @param triggerParm 触发条件
     * @return boolean
     */

    private boolean checkTriggerParm(CmpApproveProcessNode node, JSONObject triggerParm) {
        if (node.getTriggerId() != null) {
            if (triggerParm == null || triggerParm.size() == 0) {
                return false;
            }
            CmpApprovalProcessTrigger processTrigger = triggerService.getByTriggerId(node.getTriggerId());
            JSONObject jsonObject = new JSONObject(processTrigger.getCondition());
            Set<String> keySet = jsonObject.keySet();
            for (String conditionKey : keySet) {
                String condition = jsonObject.getStr(conditionKey);
                Set<String> triggerKeySet = triggerParm.keySet();
                for (String triggerKey : triggerKeySet) {
                    if (conditionKey.equals(triggerKey)) {
                        String trigger = triggerParm.getStr(triggerKey);
                        String[] splitList = trigger.split(",");
                        for (String split : splitList) {
                            if (condition.contains(split)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    /**
     * @param thisRecord   当前正在审批的节点实例
     * @param user         当前的审批人
     * @param nextNodeList 下节点的列表
     * @param flag         当前审批的状态 主要用于创建下节点record的状态 ture为待提交
     * @return LinkedList<CmpApproveProcessRecord>
     */
    private LinkedList<CmpApproveProcessRecord> createNextRecord(CmpApproveProcessRecord thisRecord,
                                                                 SysUser user,
                                                                 List<CmpApproveProcessNode> nextNodeList,
                                                                 List<NodesApproverJx> nextNodesApprover,
                                                                 CmpApproveProcessInstance processInstance,
                                                                 boolean flag) {
        LinkedList<CmpApproveProcessRecord> nextRecordList = new LinkedList<>();
        Map<String, NodesApproverJx> nextNodesApproverMap = new HashMap<>(nextNodesApprover.size());
        nextNodesApprover.forEach(nodesApprover -> {
            nextNodesApproverMap.put(nodesApprover.getNode().getNodeId(), nodesApprover);
        });
        nextNodeList.forEach(nextNode -> {
            if (nextNode.getNodeType().toString().equals(NodeType.END_NODE)) {
                CmpApproveProcessRecord endRecord = createNoApproveRecord(nextNode, user, thisRecord.getInstanceId(), thisRecord.getBusinessId());
                if (flag) {
                    endRecord.setDealStatus(Integer.parseInt(DealStatus.TO_SUBMIT));
                } else {
                    processInstance.setInstanceStatus(Integer.parseInt(InstanceStatus.APPROVAL_COMPLETED));
                    endRecord.setDealStatus(1);
                }
                nextRecordList.add(endRecord);
            } else {
                NodesApproverJx nodesApprover = nextNodesApproverMap.get(nextNode.getNodeId());
                if (nodesApprover != null) {
                    List<SysUser> approverUser = nodesApprover.getApproverUser();
                    if (approverUser.isEmpty()) {
                        McdException.throwMcdException(McdException.McdExceptionEnum.PARA_NO_APPROVER);
                    }
                    approverUser.forEach(nextApproveUser -> {
                        CmpApproveProcessRecord approveRecord = createApproveRecord(nextNode, nextApproveUser, thisRecord, flag);
                        nextRecordList.add(approveRecord);
                    });
                }
            }
        });
        return nextRecordList;
    }

    /**
     * @param nextNode   要创建实例record的节点
     * @param user       节点审批人
     * @param thisRecord 当前用户正在操作的节点
     * @param flag       当前操作节点的状态 是否有其他人会审或有其他分支正在审批中
     * @return CmpApproveProcessRecord
     */
    private CmpApproveProcessRecord createApproveRecord(CmpApproveProcessNode nextNode, SysUser user, CmpApproveProcessRecord thisRecord, boolean flag) {
        //判断当前节点是否存在同一个人的待提交recor 存在直接返回
        CmpApproveProcessRecord record = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                .eq(CmpApproveProcessRecord::getNodeId, nextNode.getNodeId())
                .eq(CmpApproveProcessRecord::getApprover, user.getUserId())
                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.TO_SUBMIT)
        );
        if (record == null) {
            record = new CmpApproveProcessRecord();
            record.setId(DataUtil.generateId());
            record.setBusinessId(thisRecord.getBusinessId());
            record.setPreRecordId(thisRecord.getId());
            record.setDealTime(new Date());
            record.setNodeId(nextNode.getNodeId());
            record.setNodeType(nextNode.getNodeType());
            record.setInstanceId(thisRecord.getInstanceId());
            record.setPreRecordId(thisRecord.getId());
            record.setDealStatus(Integer.parseInt(DealStatus.PASS));
            record.setNodeBusinessName(nextNode.getNodeName());
            record.setNodeName(nextNode.getNodeName());
            record.setApprover(user.getUserId());
            record.setApproverName(user.getUserName());
            record.setCreateBy(thisRecord.getCreateBy());
            record.setModifyBy(thisRecord.getModifyBy());
        }
        //判断当前节点是否为多分支汇聚 多分支则该record为待提交 如果不上则是待审批
        List<CmpApprovalProcessFlow> flowList = flowService.list(Wrappers.<CmpApprovalProcessFlow>query().lambda()
                .eq(CmpApprovalProcessFlow::getProcessId, nextNode.getProcessId())
                .eq(CmpApprovalProcessFlow::getEndNode, nextNode.getNodeId())
                .eq(CmpApprovalProcessFlow::getProcessVersionNum, nextNode.getProcessVersionNum())
        );
        if (flowList.size() > 1 && flag) {
            record.setDealStatus(Integer.parseInt(DealStatus.TO_SUBMIT));
        } else {
            record.setDealStatus(Integer.parseInt(DealStatus.PENDING));
        }
        return record;
    }

    /**
     * 创建事件节点的流程节点实例
     *
     * @param thisRecord        当前节点record
     * @param nextEventNodeList 事件节点node
     * @return LinkedList<CmpApproveProcessRecord>
     */
    private LinkedList<CmpApproveProcessRecord> createEventRecord(CmpApproveProcessRecord thisRecord, LinkedList<CmpApproveProcessNode> nextEventNodeList, boolean flag) {
        LinkedList<CmpApproveProcessRecord> nextEventRecordList = new LinkedList<>();
        nextEventNodeList.forEach(node -> {
            //判断当前节点是否为多分支汇聚 是的话看是否有其他节点待审，有的话当前事件暂不执行
            List<CmpApprovalProcessFlow> flowList = flowService.list(Wrappers.<CmpApprovalProcessFlow>query().lambda()
                    .eq(CmpApprovalProcessFlow::getProcessId, node.getProcessId())
                    .eq(CmpApprovalProcessFlow::getEndNode, node.getNodeId())
                    .eq(CmpApprovalProcessFlow::getProcessVersionNum, node.getProcessVersionNum())
            );
            if (!(flowList.size() > 1 && flag)) {
                //记录事件节点
                CmpApproveProcessRecord eventRecord = new CmpApproveProcessRecord();
                eventRecord.setId(DataUtil.generateId());
                eventRecord.setDealStatus(Integer.parseInt(DealStatus.PASS));
                eventRecord.setInstanceId(thisRecord.getInstanceId());
                eventRecord.setNodeName(node.getNodeName());
                eventRecord.setPreRecordId(thisRecord.getId());
                Map<String, String> eventMap = EventConfig.getEventMap();
                eventRecord.setDealOpinion(eventMap.get(node.getEventId()));
                eventRecord.setNodeBusinessName(node.getNodeName());
                eventRecord.setBusinessId(thisRecord.getBusinessId());
                eventRecord.setEventId(node.getEventId());
                eventRecord.setDealTime(new Date());
                eventRecord.setNodeId(node.getNodeId());
                eventRecord.setNodeType(node.getNodeType());
                eventRecord.setApprover(thisRecord.getApprover());
                eventRecord.setApproverName(thisRecord.getApproverName());
                eventRecord.setCreateBy(thisRecord.getCreateBy());
                eventRecord.setModifyBy(thisRecord.getModifyBy());
                nextEventRecordList.add(eventRecord);
            }
        });
        return nextEventRecordList;
    }

    /**
     * 策略统筹任务--调用事件节点处理方法
     *
     * @param nextEventRecord  下一个事件记录
     * @param submitProcessDTO 提交过程dto
     */
    private void coordinationTaskInvokeEvent(List<CmpApproveProcessRecord> nextEventRecord, SubmitProcessJxDTO submitProcessDTO) {
        nextEventRecord.forEach(eventRecord -> {
            EventInterfaceJx bean = SpringUtil.getBean(eventRecord.getEventId(), EventInterfaceJx.class);
            CmpApproveProcessJxRecord jxRecord = new CmpApproveProcessJxRecord();
            BeanUtil.copyProperties(eventRecord, jxRecord);
            jxRecord.setChildBusinessId(submitProcessDTO.getChildBusinessId());
            bean.invoke(jxRecord);
        });
    }

    /**
     * 调用事件节点处理方法
     *
     * @param nextEventRecord
     */
    private void invokeEvent(List<CmpApproveProcessRecord> nextEventRecord) {
        nextEventRecord.forEach(eventRecord -> {
            EventInterface bean = SpringUtil.getBean(eventRecord.getEventId(), EventInterface.class);
            bean.invoke(eventRecord);
        });
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

    /**
     * 判断该主任务下是否还有审批中状态的子任务
     *
     * @param submitProcessDTO 提交过程dto
     * @return {@link ActionResponse}
     */
    private ActionResponse chkApprovingTask(SubmitProcessJxDTO submitProcessDTO) {
        CampCoordinationStatusQuery coordinationStatusQuery = new CampCoordinationStatusQuery();
        coordinationStatusQuery.setTaskId(submitProcessDTO.getBusinessId());
        coordinationStatusQuery.setChildTaskIds(submitProcessDTO.getChildBusinessId());
        ActionResponse actionResponse = mcdCampCoordinationFeignClient.chkApprovingTask(coordinationStatusQuery);
        log.info("判断是否还有审批中状态的子任务入参={}，返回={}", JSONUtil.toJsonStr(coordinationStatusQuery), JSONUtil.toJsonStr(actionResponse));
        return actionResponse;
    }

    /**
     * 转至沟通人
     *
     * @param task
     * @param user
     */
    @Override
    public void addCommunicationTask(ApproveUserTaskBo task, User user) throws Exception {
        addCommunication(task);
        SubmitProcessJxDTO submitProcessDTO = new SubmitProcessJxDTO();
        submitProcessDTO.setUser(user);
        submitProcessDTO.setApprovalType(ConstApprove.APPROVE_TYPE_CAMP);
        addCommunicationTaskSyncEmis(task, submitProcessDTO);
    }

    /**
     * 转至沟通人
     *
     * @param task
     * @param user
     */
    @Override
    public BatchApproveResult addCommunicationTaskBatch(ApproveUserTaskBo task, User user) throws Exception {
        List<String> success = new ArrayList<>();
        List<String> fail = new ArrayList<>();

        for (int i = 0; i < task.getInstanceIds().length; i++) {
            ApproveUserTaskBo taskTemp = new ApproveUserTaskBo();
            BeanUtil.copyProperties(task, taskTemp);
            taskTemp.setInstanceId(task.getInstanceIds()[i]);
            taskTemp.setServiceType(task.getServiceTypes()[i]);
            taskTemp.setServiceName(task.getServiceNames()[i]);
            try {
                addCommunicationTask(taskTemp, user);
                success.add(task.getInstanceIds()[i]);
            } catch (Exception e) {
                log.error("转至沟通人失败:{}", taskTemp, e);
                fail.add(task.getInstanceIds()[i]);
            }
        }
        BatchApproveResult result = new BatchApproveResult();
        result.setFail(fail);
        result.setSuccess(success);
        return result;
    }

    /**
     *
     * 生成转至沟通人记录
     * @param task
     * @throws Exception
     */
    private void addCommunication(ApproveUserTaskBo task) throws Exception {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, task.getSubmitter());
        User user = userService.getOne(wrapper);
        McdApprovalAdviceModel model = new McdApprovalAdviceModel();
        model.setId(DataUtil.generateId());
        model.setInstanceId(task.getInstanceId());
        model.setNodeId(task.getNodeId());
        model.setApprover(task.getApprovalUser());
        //第一次是提交人名称,第二次是审批人名称
        model.setApproverName(user.getUserName());
        model.setDealStatus(0);
        model.setDealOpinion(task.getAdvice());
        model.setSubmitter(task.getSubmitter());
        mcdApprovalAdviceService.save(model);
    }

    /**
     * 转至沟通人添加emsi待办
     * @param task
     * @param submitProcessDTO
     */
    private void addCommunicationTaskSyncEmis(ApproveUserTaskBo task, SubmitProcessJxDTO submitProcessDTO) {
        CmpApproveProcessInstance processInstance = instanceService.getOne(Wrappers.<CmpApproveProcessInstance>query().lambda()
                .eq(CmpApproveProcessInstance::getBusinessId, task.getServiceType())
                .eq(CmpApproveProcessInstance::getProcessId, task.getProcessId()
                ).last("LIMIT 1"));
        if (Objects.isNull(processInstance)){
            processInstance = instanceService.getOne(Wrappers.<CmpApproveProcessInstance>query().lambda()
                    .eq(CmpApproveProcessInstance::getInstanceId, task.getInstanceId()).last("LIMIT 1"));
        }
        CmpApproveProcessRecord processRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                .eq(CmpApproveProcessRecord::getInstanceId, task.getInstanceId())
                .eq(CmpApproveProcessRecord::getNodeId, task.getNodeId())
                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING));
        processRecord.setDealStatus(1);
        //删除emis代办
        approvePostProcessor.rejectProcess(submitProcessDTO, processInstance, processRecord);

        processRecord.setDealStatus(0);
        processRecord.setApprover(task.getApprovalUser());
        //增加emis代办
        approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstance, processRecord);
    }
}
