package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.Enum.*;
import com.asiainfo.biapp.pec.approve.common.McdException;
import com.asiainfo.biapp.pec.approve.common.MetaHandler;
import com.asiainfo.biapp.pec.approve.jx.Enum.ApproverType;
import com.asiainfo.biapp.pec.approve.jx.Enum.CampLogType;
import com.asiainfo.biapp.pec.approve.jx.Enum.ConstApprove;
import com.asiainfo.biapp.pec.approve.jx.dao.McdCampDefDao;
import com.asiainfo.biapp.pec.approve.jx.dto.NodesApproverJx;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampChannelList;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampDef;
import com.asiainfo.biapp.pec.approve.jx.model.SysUser;
import com.asiainfo.biapp.pec.approve.jx.service.*;
import com.asiainfo.biapp.pec.approve.model.*;
import com.asiainfo.biapp.pec.approve.service.*;
import com.asiainfo.biapp.pec.approve.util.DataUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApproveServiceImpl implements ApproveService {

    @Setter(onMethod_ = {@Autowired})
    private ICmpApproveProcessNodeService nodeService;
    @Setter(onMethod_ = {@Autowired})
    private ICmpApprovalProcessService processService;
    @Setter(onMethod_ = {@Autowired})
    private ICmpApproveProcessInstanceService instanceService;
    @Setter(onMethod_ = {@Autowired})
    private ICmpApproveProcessRecordService recordService;
    @Setter(onMethod_ = {@Autowired})
    private ICmpApprovalProcessTriggerService triggerService;

    @Autowired
    private ICmpApprovalProcessFlowService flowService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private IMcdCampDefService campDefService;

    @Resource
    private IMcdCampChannelListService campChannelListService;
    @Autowired
    private IMcdDimMaterialJxService mcdDimMaterialJxService;

    @Autowired
    private IMcdCampOperateLogJxService logService;

    @Autowired
    private ICmpApprovalProcessJxService cmpApprovalProcessJxService;

    @Resource
    private McdCampDefDao mcdCampDefDao;

    /**
     * 区县管理员角色ID
     */
    @Value("${approve.countyManagerRoleId:10}")
    private String countyManagerRoleId;

    /**
     * 网格管理员角色ID
     */
    @Value("${approve.gridManagerRoleId:11}")
    private String gridManagerRoleId;

    private static final String TASK = "TASK";

    @Override
    @Transactional
    public void submitCampseg(SubmitProcessJxDTO req, User user) {
        // 广点通968渠道--需校验此需要保存的活动引用的素材是否有正在被引用且活动状态为草稿状态
        chkHasCampUsedMaterial(req, req.getBusinessId());
        String campsegId = req.getBusinessId();
        final List<McdCampDef> campDefs = campDefService.listByCampsegRootId(campsegId);
        Assert.notEmpty(campDefs, "当前策略不存在");
        final McdCampDef campDef1 = campDefs.get(0);
        Assert.isTrue(campDef1.getCampsegStatId().equals(CampStatus.DRAFT.getId())
                || campDef1.getCampsegStatId().equals(CampStatus.APPROVE_BACK.getId())
                || campDef1.getCampsegStatId().equals(Integer.valueOf(32)), "只有草稿,退回或者预演完成状态才能提交审批");
        Assert.isTrue(campDef1.getEndDate().after(DateUtil.date().toJdkDate()), "请检查策略结束时间");
        ActionResponse<Object> submit = new ActionResponse<>();
        SubmitProcessJxDTO submitProcessDTO = getSubmitProcess(req, user);
        Long instanceId = cmpApprovalProcessJxService.commitProcess(submitProcessDTO);
        if (instanceId > 0) {
            submit = ActionResponse.getSuccessResp(instanceId);
        }

        log.info("提交审批结果->{}", new JSONObject(submit));
        if (ResponseStatus.SUCCESS.equals(submit.getStatus())) {
            log.info("submit approval success, flowId->{}", submit.getData());
            for (McdCampDef campDef : campDefs) {
                campDef.setApproveFlowId(submit.getData().toString());
                campDef.setCampsegStatId(CampStatus.APPROVING.getId());
            }
            campDefService.updateBatchById(campDefs);
            final LambdaUpdateWrapper<McdCampChannelList> update = Wrappers.lambdaUpdate();
            update.set(McdCampChannelList::getStatus, CampStatus.APPROVING.getId());
            update.eq(McdCampChannelList::getCampsegRootId, campsegId).eq(McdCampChannelList::getCampClass, ConstApprove.SpecialNumber.ONE_NUMBER);
            campChannelListService.update(update);
            logService.markSuccLog(campsegId, CampLogType.CAMP_APPR, null, user);
        } else {
            throw new BaseException("策略提交审批失败");
        }
    }

    /**
     * 主题列表批量提交审批
     *
     * @param req  请求入参
     * @param user 用户信息
     */
    @Override
    @Transactional
    public void batchSubmitCampseg(SubmitProcessJxDTO req, User user) {
        // 1. 根据主题id查询下面的所有活动
        List<Map<String, Object>> allCampByThemeId = campDefService.queryAllCampByThemeId(req.getBusinessId());
        for (Map<String, Object> map : allCampByThemeId) {
            String campsegId = String.valueOf(map.get("CAMPSEG_ROOT_ID"));
            // 将BusinessId重新赋值为根活动id
            req.setBusinessId(campsegId);
            // 广点通968渠道--需校验此需要保存的活动引用的素材是否有正在被引用且活动状态为草稿状态
            chkHasCampUsedMaterial(req, campsegId);
            final List<McdCampDef> campDefs = campDefService.listByCampsegRootId(campsegId);
            Assert.notEmpty(campDefs, "当前策略不存在");
            final McdCampDef campDef1 = campDefs.get(0);
            Assert.isTrue(campDef1.getCampsegStatId().equals(CampStatus.DRAFT.getId())
                    || campDef1.getCampsegStatId().equals(CampStatus.APPROVE_BACK.getId())
                    || campDef1.getCampsegStatId().equals(Integer.valueOf(32)), "只有草稿,退回或者预演完成状态才能提交审批");
            Assert.isTrue(campDef1.getEndDate().after(DateUtil.date().toJdkDate()), "请检查策略结束时间");
            ActionResponse<Object> submit = new ActionResponse<>();
            SubmitProcessJxDTO submitProcessDTO = getSubmitProcess(req, user);
            Long instanceId = cmpApprovalProcessJxService.commitProcess(submitProcessDTO);
            if (instanceId > 0) {
                submit = ActionResponse.getSuccessResp(instanceId);
            }
            log.info("提交审批结果->{}", new JSONObject(submit));
            if (ResponseStatus.SUCCESS.equals(submit.getStatus())) {
                log.info("submit approval success, flowId->{}", submit.getData());
                for (McdCampDef campDef : campDefs) {
                    campDef.setApproveFlowId(submit.getData().toString());
                    campDef.setCampsegStatId(CampStatus.APPROVING.getId());
                }
                campDefService.updateBatchById(campDefs);
                final LambdaUpdateWrapper<McdCampChannelList> update = Wrappers.lambdaUpdate();
                update.set(McdCampChannelList::getStatus, CampStatus.APPROVING.getId());
                update.eq(McdCampChannelList::getCampsegRootId, campsegId).eq(McdCampChannelList::getCampClass, ConstApprove.SpecialNumber.ONE_NUMBER);
                campChannelListService.update(update);
                logService.markSuccLog(campsegId, CampLogType.CAMP_APPR, null, user);
            } else {
                throw new BaseException("策略提交审批失败");
            }
        }
    }

    /**
     * 968渠道判断该素材是否有草稿状态的活动使用
     *
     * @param req req
     */
    private void chkHasCampUsedMaterial(SubmitProcessJxDTO req, String campsegId) {
        if (req.getChannelId().contains("968")) {
            // 根据策略根id查询相关信息
            List<Map<String, Object>> mapList = mcdCampDefDao.qryChannelIdByCampsegRootId(campsegId);
            mapList.forEach(map -> {
                // 根据素材id判断该素材是否有非草稿、预演完成状态的活动使用
                Map<String, Object> resMap = campDefService.chkHasCampUsedMaterial((String) map.get("COLUMN_EXT2"));
                if (Integer.parseInt(String.valueOf(resMap.get("count"))) >= 1) {
                    log.warn("已有活动={}使用了改素材={}", resMap.get("campsegId"), resMap.get("materialId"));
                    throw new BaseException("当前素材ID已使用，请更换素材！");
                }
            });
        }
    }

    @Override
    @Transactional
    public CmpApprovalProcess copyCmpApprovalProcess(Long proceesId) {
        CmpApprovalProcess process = processService.getOne(new QueryWrapper<CmpApprovalProcess>().lambda().eq(CmpApprovalProcess::getProcessId, proceesId));
        if (null == process) {
            throw new BaseException(StrUtil.format("流程[{}]不存在", proceesId));
        }
        Long copyProcessId = DataUtil.generateId();
        process.setProcessId(copyProcessId);
        // 版本号初始化为0
        process.setBerv(0);
        String copyProcessName = StrUtil.format("复制_{}_{}", process.getProcessName(), System.currentTimeMillis());
        String copyProcessGetReadyName = StrUtil.format("复制_审批流{}", System.currentTimeMillis());
        copyProcessName = copyProcessName.length() > 20 ? copyProcessGetReadyName:copyProcessName;
        process.setProcessName(copyProcessName);
        process.setProcessDesc("复制_" + process.getProcessDesc());
        User user = MetaHandler.getUser();
        if (null != user) {
            process.setCreateBy(user.getUserId());
            process.setModifyBy(user.getUserId());
        }
        List<CmpApproveProcessNode> nodeList = nodeService.list(new QueryWrapper<CmpApproveProcessNode>().lambda().eq(CmpApproveProcessNode::getProcessId, proceesId));
        nodeList.stream().forEach(node -> {
            node.setProcessId(copyProcessId);
            node.setId(DataUtil.generateId());
            node.setProcessVersionNum(0);
        });
        List<CmpApprovalProcessFlow> flowList = flowService.list(new QueryWrapper<CmpApprovalProcessFlow>().lambda().eq(CmpApprovalProcessFlow::getProcessId, proceesId));
        flowList.stream().forEach(flow -> {
            flow.setProcessId(copyProcessId);
            flow.setId(DataUtil.generateId());
            flow.setProcessVersionNum(0);
        });
        processService.save(process);
        nodeService.saveBatch(nodeList);
        flowService.saveBatch(flowList);
        return process;
    }

    @Override
    public boolean isProcessUsed(Long proceesId) {
        QueryWrapper<CmpApproveProcessInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CmpApproveProcessInstance::getProcessId,proceesId);
        List<CmpApproveProcessInstance> instances = instanceService.list(wrapper);
        return CollectionUtil.isNotEmpty(instances);
    }


    @Override
    public SubmitProcessJxDTO getNodeApprover(SubmitProcessJxDTO submitProcessDTO) {
        if(null == submitProcessDTO.getUser()){
            //当前登录用户
            User  user = MetaHandler.getUser();
            submitProcessDTO.setUser(user);
        }
        // 当前审批节点
        CmpApproveProcessNode thisNode;

        //获取流程实例
        CmpApproveProcessInstance instance = instanceService.getById(submitProcessDTO.getInstanceId());
        if (null != instance) {
            submitProcessDTO.setBerv(instance.getProcessVersionNum());
            submitProcessDTO.setBusinessId(instance.getBusinessId());
            //查询活动触发条件
            setTriggerParam(submitProcessDTO);
        }
        //获取当前节点
        thisNode = getCurrentNode(submitProcessDTO, instance);
        if (thisNode == null) {
            McdException.throwMcdException(McdException.McdExceptionEnum.CURRENT_APPROVAL_NODE_IS_EMPTY);
        }
        submitProcessDTO.setNodeId(thisNode.getNodeId());
        submitProcessDTO.setProcessId(thisNode.getProcessId());


        // 审批模板流程
        CmpApprovalProcess approvalProcess = processService.getById(submitProcessDTO.getProcessId());

        // 获取下级审批结点(包含审批和事件结点)
        List<CmpApproveProcessNode> nodeList = getNextNodeList(submitProcessDTO, thisNode, instance, approvalProcess);
        if (Objects.nonNull(nodeList) && nodeList.size() > 0){
            log.info("查询到下级审批节点个数(包含审批和事件节点):"+ nodeList.size());
        }
        // 剔除事件和结束节点，只留下 审批节点
        List<CmpApproveProcessNode> nextApproveNodeList = getNextApproveNodeList(submitProcessDTO, thisNode, approvalProcess, nodeList);
        if (Objects.nonNull(nextApproveNodeList) && nextApproveNodeList.size() > 0){
            log.info("查询到下级审批节点个数(剔除事件和结束节点):"+ nextApproveNodeList.size());
        }else {
            log.info("未查询到下级审批节点" );
        }

        // 获取每个下级审批节点的具体审批人
        List<NodesApproverJx> nextNodesApprover = getNodesApprovers(submitProcessDTO, nextApproveNodeList);
        if (Objects.nonNull(nextNodesApprover) && nextNodesApprover.size() > 0){
            log.info("查询到下级审批节点个数(包含审批和事件节点):"+ nextNodesApprover.size());
        }
        submitProcessDTO.setNextNodesApprover(nextNodesApprover);
        return submitProcessDTO;
    }

    @Override
    public SubmitProcessJxDTO getH5NodeApprover(SubmitProcessJxDTO submitProcessDTO) {

        // 当前审批节点
        CmpApproveProcessNode thisNode;

        //获取流程实例
        CmpApproveProcessInstance instance = instanceService.getById(submitProcessDTO.getInstanceId());
        if (null != instance) {
            submitProcessDTO.setBerv(instance.getProcessVersionNum());
            submitProcessDTO.setBusinessId(instance.getBusinessId());
            //查询活动触发条件
            setTriggerParam(submitProcessDTO);
        }
        //获取当前节点
        thisNode = getCurrentNode(submitProcessDTO, instance);
        if (thisNode == null) {
            McdException.throwMcdException(McdException.McdExceptionEnum.CURRENT_APPROVAL_NODE_IS_EMPTY);
        }
        submitProcessDTO.setNodeId(thisNode.getNodeId());
        submitProcessDTO.setProcessId(thisNode.getProcessId());


        // 审批模板流程
        CmpApprovalProcess approvalProcess = processService.getById(submitProcessDTO.getProcessId());

        // 获取下级审批结点(包含审批和事件结点)
        List<CmpApproveProcessNode> nodeList = getNextNodeList(submitProcessDTO, thisNode, instance, approvalProcess);
        if (Objects.nonNull(nodeList) && nodeList.size() > 0){
            log.info("查询到下级审批节点个数(包含审批和事件节点):"+ nodeList.size());
        }
        // 剔除事件和结束节点，只留下 审批节点
        List<CmpApproveProcessNode> nextApproveNodeList = getNextApproveNodeList(submitProcessDTO, thisNode, approvalProcess, nodeList);
        if (Objects.nonNull(nextApproveNodeList) && nextApproveNodeList.size() > 0){
            log.info("查询到下级审批节点个数(剔除事件和结束节点):"+ nextApproveNodeList.size() );
        }else {
            log.info("未查询到下级审批节点" );
        }

        // 获取每个下级审批节点的具体审批人
        List<NodesApproverJx> nextNodesApprover = getNodesApprovers(submitProcessDTO, nextApproveNodeList);
        if (Objects.nonNull(nextNodesApprover) && nextNodesApprover.size() > 0){
            log.info("查询到下级审批节点个数(包含审批和事件节点):"+ nextNodesApprover.size() );
        }
        submitProcessDTO.setNextNodesApprover(nextNodesApprover);
        return submitProcessDTO;
    }

    /**
     * 获取下级审批节点
     *
     * @param submitProcessDTO
     * @param thisNode
     * @param approvalProcess
     * @param nodeList
     * @return
     */
    private List<CmpApproveProcessNode> getNextApproveNodeList(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessNode thisNode, CmpApprovalProcess approvalProcess, List<CmpApproveProcessNode> nodeList) {
        // 触发条件
        JSONObject triggerParm = submitProcessDTO.getTriggerParm();
        log.info("触发条件为: "+ triggerParm);
        // 流程版本号
        Integer berv = submitProcessDTO.getBerv();
        //后续的审批节点（非事件节点）
        List<CmpApproveProcessNode> nextApproveNodeList = new LinkedList<>();
        //后续的事件节点
        LinkedList<CmpApproveProcessNode> nextEventNodeList = new LinkedList<>();
        //查询当前节点是否是多人在审批，其余审批人是否为待审
        Integer approverRecordCount = approverRecordCount(submitProcessDTO, thisNode);
        //查询当前节点的下级节点（校验触发条件是否满足）
        if (Integer.valueOf(0).equals(approvalProcess.getInitApprover()) && berv != null && triggerParm != null) {
            queryNextNode(nodeList, nextApproveNodeList, nextEventNodeList, thisNode, approverRecordCount, triggerParm);
        }
        return nextApproveNodeList;
    }

    /**
     * 获取审批节点的所有审批人
     *
     * @param submitProcessDTO
     * @param nextApproveNodeList
     * @return
     */
    private List<NodesApproverJx> getNodesApprovers(SubmitProcessJxDTO submitProcessDTO, List<CmpApproveProcessNode> nextApproveNodeList) {
        List<NodesApproverJx> nextNodesApprover = new LinkedList<>();
        nextApproveNodeList.forEach(node -> {
            NodesApproverJx nodeApprover = new NodesApproverJx();
            nodeApprover.setNode(node);
            nodeApprover.setProcessVersionNum(node.getProcessVersionNum());
            submitProcessDTO.setBerv(node.getProcessVersionNum());

            if (node.getNodeType().toString().equals(NodeType.END_NODE)) {
                // 结束节点跳过
                return;
            }
            // 人员
            if (node.getApproverType().toString().equals(ApproverType.PERSONNEL)) {
                List<SysUser> userList = sysUserService.list(Wrappers.<SysUser>query().lambda()
                        .in(SysUser::getUserId, Arrays.asList(node.getApprover().split(","))));
                if (userList == null) {
                    McdException.throwMcdException(McdException.McdExceptionEnum.GET_APPROVER_ERROR);
                }
                nodeApprover.setApproverUser(userList);
                nextNodesApprover.add(nodeApprover);
                return;
            }
            // 部门
            if (node.getApproverType().toString().equals(ApproverType.BRANCH)) {
                List<SysUser> list = sysUserService.listByDepartmentId(node.getApprover());
                if (list == null) {
                    McdException.throwMcdException(McdException.McdExceptionEnum.GET_APPROVER_ERROR);
                }
                nodeApprover.setApproverUser(list);
                nextNodesApprover.add(nodeApprover);
                return;
            }
            // 角色
            if (node.getApproverType().toString().equals(ApproverType.ROLE)) {
                List<SysUser> list = sysUserService.listByRoleId(node.getApprover());
                log.info("businessId={},根据角色查询到的审批用户={}", submitProcessDTO.getBusinessId(), JSONUtil.toJsonStr(list));
                // 策略统筹角色审批逻辑单独处理
                if (TASK.equals(submitProcessDTO.getApprovalType())) {
                    // 1.判断是否第一次子任务审批 查询审批节点cmp_approve_process_record数据
                    CmpApproveProcessRecord recordServiceOne = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                            .eq(CmpApproveProcessRecord::getBusinessId, submitProcessDTO.getBusinessId())
                            .eq(CmpApproveProcessRecord::getNodeType, 1)); // 审批节点
                    if (ObjectUtil.isEmpty(recordServiceOne)) { // 表示第一次审批
                        list = getSysUsers(submitProcessDTO, node, list);
                        log.info("businessId={},策略统筹第一次审批的审批人信息={}", submitProcessDTO.getBusinessId(), JSONUtil.toJsonStr(list));
                    } else { // 表示第n次审批
                        // 只保留第一次审批的审批人信息
                        list = list.stream().filter(a -> a.getUserId().equals(recordServiceOne.getApprover())).collect(Collectors.toList());
                        log.info("businessId={},策略统筹保留第一次审批的审批人信息={}", submitProcessDTO.getBusinessId(), JSONUtil.toJsonStr(list));
                    }
                } else {
                    list = getSysUsers(submitProcessDTO, node, list);
                    log.info("businessId={},个性化处理后的审批用人信息={}", submitProcessDTO.getBusinessId(), JSONUtil.toJsonStr(list));
                }
                nodeApprover.setApproverUser(list);
            }
            nextNodesApprover.add(nodeApprover);
        });
        return nextNodesApprover;
    }

    /**
     * 获取审批用户信息
     *
     * @param submitProcessDTO 提交过程dto
     * @param node             节点
     * @param list             列表
     * @return {@link List}<{@link SysUser}>
     */
    private List<SysUser> getSysUsers(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessNode node, List<SysUser> list) {
        if (list == null) {
            McdException.throwMcdException(McdException.McdExceptionEnum.GET_APPROVER_ERROR);
        }
        // 江西个性化逻辑: 如果当前审批节点角色是 区县管理员 ,需要对根据角色查询的所有用户，再根据区县ID过滤一次
        if (countyManagerRoleId.equalsIgnoreCase(node.getApprover())) {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysUser::getUserId, submitProcessDTO.getUser().getUserId());
            SysUser sysUser = sysUserService.getOne(queryWrapper);
            String countyId = sysUser.getCountyId();
            log.info("当前区县管理员角色ID归属区县ID为: " + countyId);
            list = list.stream().filter(u -> countyId.equalsIgnoreCase(u.getCountyId())).collect(Collectors.toList());
        }
        // 江西个性化逻辑: 如果当前审批节点角色是 网格管理员 ,需要对根据角色查询的所有用户，再根据网格ID过滤一次
        if (gridManagerRoleId.equalsIgnoreCase(node.getApprover())) {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysUser::getUserId, submitProcessDTO.getUser().getUserId());
            SysUser sysUser = sysUserService.getOne(queryWrapper);
            String gridId = sysUser.getGridId();
            log.info("当前网格管理员角色ID归属网格ID为:" + gridId);
            list = list.stream().filter(u -> gridId.equalsIgnoreCase(u.getGridId())).collect(Collectors.toList());
        }
        if (list == null) {
            McdException.throwMcdException(McdException.McdExceptionEnum.GET_APPROVER_ERROR);
        }
        return list;
    }

    /**
     * 获取下级节点（包含审批，事件，结事节点等）
     *
     * @param submitProcessDTO 请求参数
     * @param thisNode         当前审批结点
     * @param instance         当前实例
     * @param approvalProcess  审批模板流程
     * @return
     */
    private List<CmpApproveProcessNode> getNextNodeList(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessNode thisNode, CmpApproveProcessInstance instance, CmpApprovalProcess approvalProcess) {
        List<CmpApproveProcessNode> nodeList = new ArrayList<>();

        if (submitProcessDTO.getInstanceId() == null) {
            //没有流程实例： 说明是第一次提交审批时获取下级审批人
            nodeList.addAll(nodeService.list(Wrappers.<CmpApproveProcessNode>query().lambda()
                    .eq(CmpApproveProcessNode::getProcessId, thisNode.getProcessId())
                    .eq(CmpApproveProcessNode::getProcessVersionNum, thisNode.getProcessVersionNum())
                    .in(CmpApproveProcessNode::getNodeId, Arrays.asList(thisNode.getNextNodeId().split(",")))));
        } else {
            //当前登录用户
            User user = submitProcessDTO.getUser();
            //存在流程实例：说明是已经提交审批，在审批过程中获取下级审批人
            // 当前审批节点在CmpApproveProcessRecord表中的记录
            CmpApproveProcessRecord thisRecord = getCurrentNodeRecord(submitProcessDTO, user, thisNode);
            if (thisRecord == null) {
                // 第一次提交审批时，会在Record表生成 所有审批节点的记录,正常情况下这里是可以查询到的
                McdException.throwMcdException(McdException.McdExceptionEnum.APPROVE_SUBMIT_USER_NO_ERROR);
            }
            //  0：继续审批 - 重新提交审批时上次已经审批通过的节点不再审批
            //  1：重新审批（默认值）-重新提交审批时所有节点需要重新审批
            String processType = String.valueOf(approvalProcess.getProcessType());
            // 审批实例状态
            String instanceStatus = String.valueOf(instance.getInstanceStatus());
            //流程模板为继续审批 且 流程实例的状态为被驳回
            if (processType.equals(ProcessType.CONTINUE_APPROVE) && instanceStatus.equals(InstanceStatus.APPROVAL_REJECTED)) {
                //继续审批  修改其余待提交审批节点节点
                List<CmpApproveProcessRecord> processRecords = recordService.list(Wrappers.<CmpApproveProcessRecord>query().lambda()
                        .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                        .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.TO_SUBMIT)
                        .eq(CmpApproveProcessRecord::getNodeType, NodeType.APPROVAL_NODE));
                if (!processRecords.isEmpty()) {
                    processRecords.forEach(record -> record.setDealStatus(Integer.parseInt(DealStatus.PENDING)));
                    recordService.updateBatchById(processRecords);
                }
                //查询审批驳回的哪个节点 直接创建驳回节点的record
                //CmpApproveProcessRecord preRecord = recordService.getById(thisRecord.getPreRecordId());
                CmpApproveProcessNode processNode = nodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                        .eq(CmpApproveProcessNode::getProcessId, thisNode.getProcessId())
                        .eq(CmpApproveProcessNode::getNodeId, thisRecord.getNodeId())
                        .eq(CmpApproveProcessNode::getProcessVersionNum, instance.getProcessVersionNum()));

                nodeList.add(processNode);
                submitProcessDTO.setSubmitStatus(Integer.parseInt(DealStatus.REJECT));
            } else {
                nodeList.addAll(nodeService.list(Wrappers.<CmpApproveProcessNode>query().lambda()
                        .eq(CmpApproveProcessNode::getProcessId, thisNode.getProcessId())
                        .eq(CmpApproveProcessNode::getProcessVersionNum, thisNode.getProcessVersionNum())
                        .in(CmpApproveProcessNode::getNodeId, Arrays.asList(thisNode.getNextNodeId().split(",")))
                ));
            }
        }
        // 是否初始化审批人: 1是，0否
        Integer initApprover = approvalProcess.getInitApprover();
        //如果初始化审批人获取所有节点
        if (Integer.valueOf(1).equals(initApprover) && nodeList.size() > 0) {
            LinkedList<CmpApproveProcessNode> tirNodeList = new LinkedList<>();
            nodeList.forEach(node -> {
                if (checkTriggerParm(node, submitProcessDTO.getTriggerParm())) {
                    tirNodeList.add(node);
                }
            });
            nodeList = getNodeApproverList(tirNodeList, nodeList);
        }
        return nodeList;
    }

    /**
     * 查询当前节点是否是多人在审批，其余审批人是否为待审
     *
     * @param submitProcessDTO
     * @param thisNode
     * @return
     */
    private Integer approverRecordCount(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessNode thisNode) {
        Integer approverRecordCount = 0;
        if (null != submitProcessDTO.getInstanceId()) {
            CmpApproveProcessRecord thisRecord = getCurrentNodeRecord(submitProcessDTO, submitProcessDTO.getUser(), thisNode);
            approverRecordCount = recordService.count(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getInstanceId, thisRecord.getInstanceId())
                    .eq(CmpApproveProcessRecord::getNodeId, thisRecord.getNodeId())
                    .ne(CmpApproveProcessRecord::getApprover, thisRecord.getApprover())
                    .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING));
        }
        return approverRecordCount;
    }

    /**
     * 获取当前审批结点在CmpApproveProcessRecord表的记录
     *
     * @param submitProcessDTO
     * @param user
     * @param thisNode
     * @return
     */
    private CmpApproveProcessRecord getCurrentNodeRecord(SubmitProcessJxDTO submitProcessDTO, User user, CmpApproveProcessNode thisNode) {
        CmpApproveProcessRecord thisRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                .eq(CmpApproveProcessRecord::getApprover, thisNode.getApprover())
                .eq(CmpApproveProcessRecord::getInstanceId, submitProcessDTO.getInstanceId())
                .eq(CmpApproveProcessRecord::getNodeId, submitProcessDTO.getNodeId())
                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.REJECT).last("LIMIT 1"));

        if (thisRecord == null) {
            thisRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getApprover, thisNode.getApprover())
                    .eq(CmpApproveProcessRecord::getInstanceId, submitProcessDTO.getInstanceId())
                    .eq(CmpApproveProcessRecord::getNodeId, submitProcessDTO.getNodeId())
                    .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING).last("LIMIT 1"));
        }

        if (thisRecord == null && NodeType.STARTING_NODE.equals(thisNode.getNodeType().toString())) {
            thisRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getApprover, user.getUserId())
                    .eq(CmpApproveProcessRecord::getInstanceId, submitProcessDTO.getInstanceId())
                    .eq(CmpApproveProcessRecord::getNodeId, submitProcessDTO.getNodeId()).last("LIMIT 1"));
        }


        if (thisRecord == null && NodeType.APPROVAL_NODE.equals(thisNode.getNodeType().toString())) {
            thisRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getApprover, user.getUserId())
                    .eq(CmpApproveProcessRecord::getInstanceId, submitProcessDTO.getInstanceId())
                    .eq(CmpApproveProcessRecord::getNodeId, thisNode.getNodeId()).last("LIMIT 1"));
        }
        return thisRecord;
    }

    /**
     * 获取当前审批节点
     *
     * @param submitProcessDTO
     * @param instance
     * @return
     */
    private CmpApproveProcessNode getCurrentNode(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance instance) {
        CmpApproveProcessNode thisNode;
        if (StringUtils.isBlank(submitProcessDTO.getNodeId()) && submitProcessDTO.getInstanceId() == null) {
            thisNode = nodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                    .eq(CmpApproveProcessNode::getProcessId, submitProcessDTO.getProcessId())
                    .eq(CmpApproveProcessNode::getProcessVersionNum, submitProcessDTO.getBerv())
                    .eq(CmpApproveProcessNode::getNodeType, NodeType.STARTING_NODE));
        } else {
            if (StringUtils.isNotBlank(submitProcessDTO.getNodeId())) {
                thisNode = nodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                        .eq(CmpApproveProcessNode::getProcessId, instance.getProcessId())
                        .eq(CmpApproveProcessNode::getProcessVersionNum, instance.getProcessVersionNum())
                        .eq(CmpApproveProcessNode::getNodeId, submitProcessDTO.getNodeId()));
            } else {
                //如果没有节点id 有流程实例id 则取当前流程实例的第一个record的node
                thisNode = getInstanceNode(submitProcessDTO).get(0);
            }
        }
        return thisNode;
    }

    /**
     * 设置审批实例的触发条件
     *
     * @param submitProcessDTO
     */
    private void setTriggerParam(SubmitProcessJxDTO submitProcessDTO) {
        cmpApprovalProcessJxService.setTriggerParam(submitProcessDTO);
    }

    private SubmitProcessJxDTO getSubmitProcess(SubmitProcessJxDTO req, User user) {

        SubmitProcessJxDTO submitProcessDTO = new SubmitProcessJxDTO();
        try {
            net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(req);
            submitProcessDTO = (SubmitProcessJxDTO) net.sf.json.JSONObject.toBean(object, SubmitProcessJxDTO.class);
            List<NodesApproverJx> nodesApproverQueryList = req.getNextNodesApprover();
            List<NodesApproverJx> nodesApproverList = new ArrayList<>();

            for (NodesApproverJx query : nodesApproverQueryList) {
                NodesApproverJx approver = new NodesApproverJx();

                CmpApproveProcessNode processNode = new CmpApproveProcessNode();
                List<SysUser> userList = new ArrayList<>();
                processNode.setId(query.getNode().getId());
                processNode.setNodeId(query.getNode().getNodeId());
                processNode.setProcessId(query.getNode().getProcessId());
                processNode.setNodeName(query.getNode().getNodeName());
                processNode.setNodeType(query.getNode().getNodeType());
                processNode.setApprover(query.getNode().getApprover());
                processNode.setApproverName(query.getNode().getApproverName());
                processNode.setApproverType(query.getNode().getApproverType());
                processNode.setNextNodeId(query.getNode().getNextNodeId());
                processNode.setProcessVersionNum(query.getNode().getProcessVersionNum());
                processNode.setAllowMultSelect(query.getNode().getAllowMultSelect());
                processNode.setMultipleChoiceType(query.getNode().getMultipleChoiceType());
                for (SysUser userQuery : query.getApproverUser()) {
                    SysUser appUser = new SysUser();
                    appUser.setUserId(userQuery.getUserId());
                    appUser.setUserName(userQuery.getUserName());
                    appUser.setCityId(userQuery.getCityId());
                    appUser.setDepartmentId(userQuery.getDepartmentId());
                    userList.add(appUser);
                }
                approver.setApproverUser(userList);
                approver.setNode(processNode);
                approver.setProcessVersionNum(query.getProcessVersionNum());
                nodesApproverList.add(approver);

            }
            submitProcessDTO.setNextNodesApprover(nodesApproverList);
            submitProcessDTO.setUser(user);

        } catch (Exception e) {
            log.error("审批类型转换异常 ", e);
        }

        return submitProcessDTO;
    }

    /**
     * @param submitProcessJxDTO 流程实例id
     * @return
     */
    private List<CmpApproveProcessNode> getInstanceNode(SubmitProcessJxDTO submitProcessJxDTO) {
        CmpApproveProcessInstance instance = instanceService.getById(submitProcessJxDTO.getInstanceId());
        submitProcessJxDTO.setBerv(instance.getProcessVersionNum());
        if (instance == null) {
            McdException.throwMcdException(McdException.McdExceptionEnum.PROCESS_INSTANCE_DOES_NOT_EXIST);
        }
        List<CmpApproveProcessRecord> list;
        CmpApprovalProcess approvalProcess = processService.getById(instance.getProcessId());
        if (approvalProcess.getProcessType().toString().equals(ProcessType.CONTINUE_APPROVE) &&
                instance.getInstanceStatus().toString().equals(InstanceStatus.APPROVAL_REJECTED)) {
            list = recordService.list(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getInstanceId, submitProcessJxDTO.getInstanceId())
                    .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.REJECT));
        } else if (approvalProcess.getProcessType().toString().equals(ProcessType.RE_APPROVE) &&
                instance.getInstanceStatus().toString().equals(InstanceStatus.APPROVAL_REJECTED)) {
            list = recordService.list(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getInstanceId, submitProcessJxDTO.getInstanceId())
                    .eq(CmpApproveProcessRecord::getNodeType, NodeType.STARTING_NODE).last("LIMIT 1"));
        } else {
            list = recordService.list(Wrappers.<CmpApproveProcessRecord>query().lambda()
                    .eq(CmpApproveProcessRecord::getInstanceId, submitProcessJxDTO.getInstanceId())
                    .eq(CmpApproveProcessRecord::getNodeType, NodeType.APPROVAL_NODE));
        }

        if (list.isEmpty()) {
            McdException.throwMcdException(McdException.McdExceptionEnum.APPROVE_NO_PENDING_RECORD);
        }

        Set<String> nodeIds = new HashSet<>();
        list.forEach(record -> {
            nodeIds.add(record.getNodeId());
        });

        List<CmpApproveProcessNode> nodeList = nodeService.list(Wrappers.<CmpApproveProcessNode>query().lambda()
                .eq(CmpApproveProcessNode::getProcessVersionNum, instance.getProcessVersionNum())
                .eq(CmpApproveProcessNode::getProcessId, instance.getProcessId())
                .in(CmpApproveProcessNode::getNodeId, nodeIds));

        return nodeList;
    }

    /**
     * 传入的触发条件只要满足最小的颗粒度即代表校验通过
     *
     * @param node        节点信息
     * @param triggerParm 触发条件
     * @return boolean
     */
    private boolean checkTriggerParm(CmpApproveProcessNode node, JSONObject triggerParm) {
        // 没配置触发条件直接校验通过
        if(node.getTriggerId() == null){
            return true;
        }
        CmpApprovalProcessTrigger processTrigger = triggerService.getByTriggerId(node.getTriggerId());
        JSONObject jsonObject = new JSONObject(processTrigger.getCondition());
        // 配置触发条件,但是触发条件为空，不需要校验直接通过
        if (jsonObject == null || jsonObject.size() == 0) {
            return true;
        }
        // 节点配置的触发条件不为空,但实例的触发条件为空，直接不通过
        if (triggerParm == null || triggerParm.size() == 0) {
            return false;
        }
        Set<String> triggerKeySet = triggerParm.keySet();
        Set<String> keySet = jsonObject.keySet();
        for (String conditionKey : keySet) {
            String condition = jsonObject.getStr(conditionKey);
            boolean flag = false;
            for (String triggerKey : triggerKeySet) {
                if (conditionKey.equals(triggerKey)) {
                    String trigger = triggerParm.getStr(triggerKey);
                    String[] splitList = trigger.split(",");
                    for (String split : splitList) {
                        if (condition.contains(split)) {
                            flag = true;
                            continue;
                        }
                    }
                    if(!flag){
                        return false;
                    }
                }
            }
            // 所有条件都必须满足,有一个不满足就返回false
            if(!flag){
                return false;
            }
        }
        return true;
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
                            .in(CmpApproveProcessNode::getNodeId, Arrays.asList(node.getNextNodeId().split(","))));

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
     * 递归获取后续所有节点
     *
     * @param nodeList
     * @param noList
     * @return
     */
    public List<CmpApproveProcessNode> getNodeApproverList(List<CmpApproveProcessNode> nodeList, List<CmpApproveProcessNode> noList) {
        List<CmpApproveProcessNode> thisNodeList = new ArrayList<>();
        nodeList.forEach(node -> {
            thisNodeList.addAll(nodeService.list(Wrappers.<CmpApproveProcessNode>query().lambda()
                    .eq(CmpApproveProcessNode::getProcessId, node.getProcessId())
                    .eq(CmpApproveProcessNode::getProcessVersionNum, node.getProcessVersionNum())
                    .in(CmpApproveProcessNode::getNodeId, Arrays.asList(node.getNextNodeId().split(",")))
                    .ne(CmpApproveProcessNode::getNodeType, NodeType.END_NODE)));

            if (thisNodeList.size() > 0) {
                noList.addAll(thisNodeList);
                getNodeApproverList(thisNodeList, noList);
            }
        });
        return noList;
    }


}
