package com.asiainfo.biapp.pec.approve.jx.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.approve.Enum.DealStatus;
import com.asiainfo.biapp.pec.approve.common.MetaHandler;
import com.asiainfo.biapp.pec.approve.dto.RecordPageDTO;
import com.asiainfo.biapp.pec.approve.jx.Enum.ConstApprove;
import com.asiainfo.biapp.pec.approve.jx.controller.reqParam.ApproverProcessDTONew;
import com.asiainfo.biapp.pec.approve.jx.dto.*;
import com.asiainfo.biapp.pec.approve.jx.model.McdApprovalAdviceModel;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampDef;
import com.asiainfo.biapp.pec.approve.jx.service.*;
import com.asiainfo.biapp.pec.approve.jx.service.feign.*;
import com.asiainfo.biapp.pec.approve.jx.task.CheckEmisTask;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessInstance;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessNode;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.approve.model.User;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveProcessInstanceService;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveProcessNodeService;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveProcessRecordService;
import com.asiainfo.biapp.pec.approve.service.IUserService;
import com.asiainfo.biapp.pec.approve.util.DataUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 审批后台控制台
 *
 * @author: feify
 * @create: 2022-06-06 10:15
 **/
@Api(value = "江西:审批流程实例管理", tags = {"江西:审批流程实例管理"})
@RestController
@RequestMapping("/jx/cmpApproveProcessInstance")
@Slf4j
public class ApproveController {

    @Autowired
    private ApproveService approveService;

    @Autowired
    private ApproveRecordService approveRecordService;

    @Autowired
    private ICmpApprovalProcessJxService cmpApprovalProcessJxService;

    @Autowired
    private IMcdCampDefService campDefService;
    @Autowired
    private CheckEmisTask checkEmisTask;
    @Autowired
    private IUserService userService;

    @Resource
    private IMcdSysCityService mcdSysCityService;

    @Resource
    private IMcdSysDepartmentService mcdSysDepartmentService;
    @Resource
    private McdApprovalAdviceService mcdApprovalAdviceService;
    @Autowired
    private IApprovePostProcessor approvePostProcessor;
    @Autowired
    private ICmpApproveProcessInstanceService instanceService;
    @Autowired
    private ICmpApproveProcessRecordService recordService;

    @Autowired
    private ICmpApproveProcessNodeService cmpApproveProcessNodeService;

    @Resource
    private HisApproveRecordFeignClient hisApproveRecordFeignClient;


    @Autowired
    private CustomLabelFeignClient customLabelFeignClient;

    @Autowired
    private CustomAlarmFeignClient customAlarmFeignClient;

    @Autowired
    private KhtCareSmsTemplateFeignClient khtCareSmsTemplateFeignClient;

    @Autowired
    private KhtCalloutRuleFeignClient calloutRuleFeignClient;

    @Autowired
    private KhtBlacklistFeignClient blacklistFeignClient;

    @Resource
    private HttpServletRequest request;

    /**
     * 客户通短信模板类型
     */
    public static final String APPROVE_TYPE = "MESSAGE_TEMPLATE";
    private static final String HMH5_BLACKLIST = "HMH5_BLACKLIST";

    private static final String HMH5_CALLOUT_RULE = "MCD_RULES";

    @PostMapping(path = "/getNodeApprover")
    @ApiOperation(value = "获取流程实例节点下级审批人", notes = "获取流程实例节点下级审批人")
    public ActionResponse<SubmitProcessJxDTO> getNodeApprover(@RequestBody SubmitProcessJxDTO submitProcessDTO) {
        log.info("start getNodeApproverNew param:{}", new JSONObject(submitProcessDTO).toString());
        return ActionResponse.getSuccessResp(approveService.getNodeApprover(submitProcessDTO));
    }

    @PostMapping(path = "/selectApproverProcess")
    @ApiOperation(value = "审批流程状态查询", notes = "审批流程状态查询")
    // 2023-07-10 16:41:46 将传入参数ApproverProcessDTO改为ApproverProcessDTONew以接收审批类型字段
    public ActionResponse<List<ApproverProcessNewDTO>> selectApproverProcess(@RequestBody ApproverProcessDTONew approverProcessDTO) {
        log.info("start getApproverProcessNew param:{}", new JSONObject(approverProcessDTO).toString());
        String businessId = approverProcessDTO.getBusinessId();

        long instanceId = 0;
        if (CustomLabelFeignClient.CUSTOM_LABEL.equals(approverProcessDTO.getEnumKey())) {  //从
            ActionResponse<String> response = customLabelFeignClient.selectLabelApproveFlowId(Integer.valueOf(businessId));
            if (response.getStatus().getCode() != ResponseStatus.SUCCESS.getCode()) {
                throw new BaseException(response.getData());
            }
            instanceId = Long.parseLong(response.getData());
        } else if (CustomAlarmFeignClient.CUSTOM_ALARM.equals(approverProcessDTO.getEnumKey())) {
            ActionResponse<String> response = customAlarmFeignClient.selectAlarmApproveFlowId(Integer.valueOf(businessId));
            if (response.getStatus().getCode() != ResponseStatus.SUCCESS.getCode()) {
                throw new BaseException(response.getData());
            }
            instanceId = Long.parseLong(response.getData());
        } else if (APPROVE_TYPE.equals(approverProcessDTO.getEnumKey())) { // 关怀短信
            ActionResponse<String> response = khtCareSmsTemplateFeignClient.selectCareSmsTmpApprFlowId(businessId);
            if (response.getStatus().getCode() != ResponseStatus.SUCCESS.getCode()) {
                throw new BaseException(response.getData());
            }
            instanceId = Long.parseLong(response.getData());
        } else if (HMH5_BLACKLIST.equals(approverProcessDTO.getEnumKey())) { // 客户通黑名单
            ActionResponse<String> response = blacklistFeignClient.selectBlacklistApprFlowId(businessId);
            if (response.getStatus().getCode() != ResponseStatus.SUCCESS.getCode()) {
                throw new BaseException(response.getData());
            }
            instanceId = Long.parseLong(response.getData());
        } else if (HMH5_CALLOUT_RULE.equals(approverProcessDTO.getEnumKey())) { // 客户通外呼规则
            ActionResponse<String> response = calloutRuleFeignClient.selectCalloutRuleApprFlowId(businessId);
            if (response.getStatus().getCode() != ResponseStatus.SUCCESS.getCode()) {
                throw new BaseException(response.getData());
            }
            instanceId = Long.parseLong(response.getData());
        } else {
            // 4.0的活动ID是16位
            if (businessId.length() <= 19) {
                ActionResponse response = hisApproveRecordFeignClient.listRecord(businessId);
                return ActionResponse.getSuccessResp(response.getData());
            }

            McdCampDef campDef = campDefService.getOne(Wrappers.<McdCampDef>lambdaQuery()
                    .eq(McdCampDef::getCampsegId, approverProcessDTO.getBusinessId()).last(" limit 1 "));
            if (null != campDef && StrUtil.isNotEmpty(campDef.getApproveFlowId())) {
                instanceId = Long.parseLong(campDef.getApproveFlowId());
            }
        }

        approverProcessDTO.setInstanceId(instanceId);

        List<ApproverProcessNewDTO> approverProcessDTOList = approveRecordService.getApproverProcessNew(approverProcessDTO);
        // 生成所有节点nodeId的集合
        Set<String> nodeIds = approverProcessDTOList.stream().map(o -> o.getNodeId()).collect(Collectors.toSet());
        // 查询所有的审批节点信息
        List<CmpApproveProcessRecord> list = approveRecordService.list(Wrappers.<CmpApproveProcessRecord>lambdaQuery()
                .eq(CmpApproveProcessRecord::getBusinessId, approverProcessDTO.getBusinessId()));
        boolean flag = false;
        String endNodeId = null;
        for (CmpApproveProcessRecord vo : list) {
            if (3 == vo.getNodeType() && 2 == vo.getDealStatus()) { // 如果事件节点的状态是2-驳回 则表示经过事件触发驳回(广点通的逻辑) 返回true
                flag = true;
            }
            if (2 == vo.getNodeType()) {
                endNodeId = vo.getNodeId(); // 获取终止节点id
            }
        }
        // boolean flag = approverProcessDTOList.stream().anyMatch(o -> recordServiceOne.getNodeId().equals(o.getNodeId()) && recordServiceOne.getDealStatus().equals(o.getSubmitStatus()));
        if (flag) {
            String finalEndNodeId = endNodeId;
            // 排除掉结束节点
            approverProcessDTOList = approverProcessDTOList.stream().filter(o -> StrUtil.isNotEmpty(finalEndNodeId) && !finalEndNodeId.equals(o.getNodeId())).collect(Collectors.toList());
        }
        // 最后审批时间
        String maxDealTime = "";
        for (ApproverProcessNewDTO processDTO : approverProcessDTOList) {
            // 取审批时间的最大时间
            if(StrUtil.isNotEmpty(processDTO.getDealTime()) && processDTO.getNodeType() == 1 ){
                int compare = StrUtil.compare( processDTO.getDealTime(), maxDealTime,true);
                if(compare > 0){
                    maxDealTime =  processDTO.getDealTime();
                }
            }
            // 事件和结束节点的处理时间设置为审批的最大时间
            if((processDTO.getNodeType() == 2 || processDTO.getNodeType() == 3) && StrUtil.isNotEmpty(maxDealTime)){
                processDTO.setCreateDate(maxDealTime);
                processDTO.setDealTime(maxDealTime);
            }
            // 待审批结点，处理时间为空
            if(processDTO.getSubmitStatus() == 0){
                processDTO.setDealTime("");
            }
            handleNextNodes(nodeIds, processDTO);
        }
        return ActionResponse.getSuccessResp(approverProcessDTOList);
    }


    /**
     * 保存下级审批节点都在当前返回的结果列表中
     *
     * @param nodeIds
     * @param processDTO
     */
    private void handleNextNodes(Set<String> nodeIds, ApproverProcessNewDTO processDTO) {
        if (CollectionUtil.isEmpty(nodeIds) || processDTO == null) {
            return;
        }
        String nextNodes = processDTO.getNextNodesApprover();
        if (StrUtil.isEmpty(nextNodes)) {
            return;
        }
        String[] nodeArr = nextNodes.split(",");
        //下级审批人只有一个时，不处理
        if (nodeArr.length == 1) {
            return;
        }
        List<String> list = new ArrayList<>(nodeArr.length);
        for (String s : nodeArr) {
            if (nodeIds.contains(s)) {
                list.add(s);
            }
        }
        processDTO.setNextNodesApprover(CollectionUtil.join(list, ","));
    }


    @PostMapping(path = "/commitProcess")
    @ApiOperation(value = "发起审批流程", notes = "发起审批流程")
    public ActionResponse<Long> commitProcess(@RequestBody SubmitProcessJxDTO submitProcessDTO) {
        log.info("start commitProcess para:{}", new JSONObject(submitProcessDTO).toString());
        User user = MetaHandler.getUser();
        //TODO 2023-07-05 10:29:15 测试阶段创建临时用户
        if (null == user) {
            user = new User();
            user.setUserId("admin01");
            user.setUserName("管理员1");
        }
        submitProcessDTO.setUser(user);
        Long instanceId = cmpApprovalProcessJxService.commitProcess(submitProcessDTO);
        return ActionResponse.getSuccessResp(instanceId);
    }

    @PostMapping(path = "/submitProcess")
    @ApiOperation(value = "审批", notes = "审批")
    public ActionResponse<Long> submitProcess(@RequestBody SubmitProcessJxDTO submitProcessDTO) {
        log.info("start submitProcess para:{}", new JSONObject(submitProcessDTO).toString());
        User user = MetaHandler.getUser();
        if (null == user) {
            user = new User();
            user.setUserId("admin01");
        }
        submitProcessDTO.setUser(user);
        Long instanceId = cmpApprovalProcessJxService.submitProcess(submitProcessDTO);
        // 检查并处理异常数据
        cmpApprovalProcessJxService.checkAndHandelErrorData(submitProcessDTO,instanceId);
        return ActionResponse.getSuccessResp(instanceId);
    }

    @PostMapping(path = "/batchSubmitProcess")
    @ApiOperation(value = "批量审批", notes = "批量审批")
    public ActionResponse batchSubmitProcess(@RequestBody SubmitProcessJxDTO submitProcessDTO) {
        log.info("start batchSubmitProcess para:{}", new JSONObject(submitProcessDTO).toString());
        User user = MetaHandler.getUser();
        if (null == user) {
            user = new User();
            user.setUserId("admin01");
        }
        submitProcessDTO.setUser(user);
        BatchApproveResult result = cmpApprovalProcessJxService.batchSubmitProcess(submitProcessDTO);
        return ActionResponse.getSuccessResp(result);
    }
    /**
     * 营销列表提交审批，主活动
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "营销列表提交审批", notes = "营销列表提交审批")
    @PostMapping("/subApprove")
    public ActionResponse subApprove(@RequestBody @Valid SubmitProcessJxDTO req) {
        User user = MetaHandler.getUser();
        approveService.submitCampseg(req, user);
        return ActionResponse.getSuccessResp();
    }

    /**
     * EMIS审批稽核调度
     *
     * @return
     */
    @ApiOperation(value = "EMIS审批稽核调度", notes = "EMIS审批稽核调度")
    @PostMapping("/checkEmisTask")
    public ActionResponse checkEmisTask() {
        try {
            checkEmisTask.prcoss();
        } catch (Exception e) {
            log.error("EMIS审批稽核调度异常:", e);
            return ActionResponse.getFaildResp();
        }
        return ActionResponse.getSuccessResp();
    }


    @PostMapping(path = "/isProcessUsed")
    @ApiOperation(value = "审批流程模板是否已使用", notes = "审批流程模板是否已使用,processId:模板流程ID")
    public ActionResponse isProcessUsed(@RequestParam("processId") Long processId) {
        log.info("审批流程模板是否已使用:{}", processId);
        return ActionResponse.getSuccessResp().setData(approveService.isProcessUsed(processId));

    }

    @PostMapping("/addCommunicationTask")
    @ApiOperation(value = "江西转至沟通人添加任务接口", notes = "转至沟通人添加任务接口")
    public ActionResponse addCommunicationTask(@RequestBody ApproveUserTaskBo task, HttpServletRequest request) throws Exception {
        log.info("转至沟通人入参:{} " , task);
        // 2023-07-09 22:15:25 如果是自定义预警或者标签就不添加代办，直接返回
        if (Arrays.asList(CustomLabelFeignClient.CUSTOM_LABEL, CustomAlarmFeignClient.CUSTOM_ALARM).contains(task.getSystemId())) {
            return ActionResponse.getSuccessResp();
        }
        UserSimpleInfo simpleInfo = UserUtil.getUser(request);
        User user = new User();
        user.setUserId(simpleInfo.getUserId());
        user.setUserName(simpleInfo.getUserName());
        user.setDepartmentId(simpleInfo.getDepartmentId());
        user.setCityId(simpleInfo.getCityId());
        cmpApprovalProcessJxService.addCommunicationTask(task,user);
        return ActionResponse.getSuccessResp();
    }

    @PostMapping("/addCommunicationTaskBatch")
    @ApiOperation(value = "江西批量转至沟通人添加任务接口", notes = "批量转至沟通人添加任务接口")
    public ActionResponse addCommunicationTaskBatch(@RequestBody ApproveUserTaskBo task, HttpServletRequest request) throws Exception {
        log.info("转至沟通人入参:{} " , task);
        // 2023-07-09 22:15:25 如果是自定义预警或者标签就不添加代办，直接返回
        if (Arrays.asList(CustomLabelFeignClient.CUSTOM_LABEL, CustomAlarmFeignClient.CUSTOM_ALARM).contains(task.getSystemId())) {
            return ActionResponse.getSuccessResp();
        }
        UserSimpleInfo simpleInfo = UserUtil.getUser(request);
        User user = new User();
        user.setUserId(simpleInfo.getUserId());
        user.setUserName(simpleInfo.getUserName());
        BatchApproveResult result = cmpApprovalProcessJxService.addCommunicationTaskBatch(task, user);
        return ActionResponse.getSuccessResp(result);
    }

    @PostMapping(path = "/queryRecordList")
    @ApiOperation(value = "江西获取用户当前待审批接口", notes = "江西获取用户当前待审批接口")
    public ActionResponse<IPage<CmpApproveProcessRecordJx>> queryRecordList(@RequestBody RecordPageDTO record) {
        log.info("start queryRecordList para:{}", new JSONObject(record).toString());
        User user = MetaHandler.getUser();
        List<String> list = record.getList();
        Page<CmpApproveProcessRecordJx> page = new Page<>(record.getCurrent(), record.getSize());
        IPage<CmpApproveProcessRecordJx> processRecordList = approveRecordService.getRecordByUserNew(page, user.getUserId(), Integer.parseInt(DealStatus.PENDING), list);
        List<CmpApproveProcessRecordJx> records = processRecordList.getRecords();
        List<CmpApproveProcessRecordJx> recordsList = new ArrayList<>();
        if (records.size() > 0) {
            records.forEach(processRecord -> {
                String nodeId = processRecord.getNodeId();
                List<CmpApproveProcessRecord> recordList = recordService.list(Wrappers.<CmpApproveProcessRecord>query().lambda()
                        .eq(CmpApproveProcessRecord::getInstanceId, processRecord.getInstanceId())
                        .eq(CmpApproveProcessRecord::getBusinessId, processRecord.getBusinessId())
                        .eq(CmpApproveProcessRecord::getDealStatus, 1));
                e:
                for (CmpApproveProcessRecord recordOne : recordList) {
                    Long processId = instanceService.getById(recordOne.getInstanceId()).getProcessId();
                    CmpApproveProcessNode processNode = cmpApproveProcessNodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                            .eq(CmpApproveProcessNode::getNodeId, recordOne.getNodeId())
                            .eq(CmpApproveProcessNode::getProcessId, processId)
                            .like(CmpApproveProcessNode::getNextNodeId, nodeId).last("LIMIT 1"));
                    if (null != processNode) {
                        recordsList.add(processRecord);
                        break e;
                    }
                }
                processRecordList.setRecords(recordsList);
            });

        }

//        List<CmpApproveProcessRecord> processRecordList = recordService.list(Wrappers.<CmpApproveProcessRecord>query().lambda()
//                .eq(CmpApproveProcessRecord::getApprover, user.getUserId())
//                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING.getValue())
//        );
        return ActionResponse.getSuccessResp(processRecordList);
    }

    @PostMapping("/queryCommunicationTask")
    @ApiOperation(value = "江西转至沟通人审批信息查询接口", notes = "转至沟通人审批信息查询接口")
    public ActionResponse<List<McdApprovalAdviceModel>> queryCommunicationTask(@RequestBody ApproveUserTaskBo task) throws Exception {

        log.info("沟通人审批信息查询入参:{} " + task);
        LambdaQueryWrapper<McdApprovalAdviceModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdApprovalAdviceModel::getInstanceId, task.getInstanceId())
                .eq(McdApprovalAdviceModel::getNodeId, task.getNodeId())
                .orderByAsc(McdApprovalAdviceModel::getDealTime);
        List<McdApprovalAdviceModel> adviceModelList = mcdApprovalAdviceService.list(queryWrapper);

        return ActionResponse.getSuccessResp(adviceModelList);
    }

    @PostMapping(path = "/queryRecordListNew")
    @ApiOperation(value = "江西获取用户当前待审批接口(包含沟通人)", notes = "江西获取用户当前待审批接口(包含沟通人)")
    public ActionResponse<IPage<McdCampCmpApproveProcessRecordJx>> queryRecordListNew(@RequestBody McdCampApproveJxNewQuery record) {
        log.info("start queryRecordListNew para:{}", new JSONObject(record).toString());
        if(StrUtil.isEmpty(record.getUserId())){
            record.setUserId( UserUtil.getUserId(request));
        }
        final IPage<McdCampCmpApproveProcessRecordJx> processRecordJxIPage = approveRecordService.queryCampAppRecord(record);
        return ActionResponse.getSuccessResp(processRecordJxIPage);
    }

    @PostMapping(path = "/queryApproveNodes")
    @ApiOperation(value = "查询用户所有待审批策略的节点", notes = "查询用户所有待审批策略的节点")
    public ActionResponse<List<ApproveNodeDTO>> queryApproveNodes(@RequestParam(value = "userId", required = false) String userId,
                                                                  @RequestParam(value = "channelId", required = false) String channelId) throws Exception {
        log.info("queryApproveNodes param:{}", userId);
        if (StrUtil.isEmpty(userId)) {
            userId = UserUtil.getUserId(request);
        }
        if (StrUtil.isEmpty(userId)) {
            throw new Exception("用户ID不能为空");
        }
        List<ApproveNodeDTO> approveNodes = approveRecordService.queryApproveNodesByUserId(userId,channelId);
        return ActionResponse.getSuccessResp(approveNodes);
    }

    @PostMapping("/communicationAppTask")
    @ApiOperation(value = "江西沟通人审批接口", notes = "江西沟通人审批接口")
    public ActionResponse communicationAppTask(@RequestBody ApproveUserTaskBo task, HttpServletRequest request) throws Exception {
        log.info("江西沟通人审批接口:{} " + task);
        User user = new User();
        communicationApp(task);

        // 2023-07-09 22:15:25 如果是自定义预警或者标签就不添加代办，直接返回
        if (Arrays.asList(CustomLabelFeignClient.CUSTOM_LABEL, CustomAlarmFeignClient.CUSTOM_ALARM).contains(task.getSystemId())) {
            return ActionResponse.getSuccessResp();
        }

        LambdaQueryWrapper<McdApprovalAdviceModel> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.eq(McdApprovalAdviceModel::getInstanceId, task.getInstanceId())
                .eq(McdApprovalAdviceModel::getNodeId, task.getNodeId())
                .eq(McdApprovalAdviceModel::getDealStatus, 1)
                .orderByAsc(McdApprovalAdviceModel::getDealTime).last("limit 1");
        McdApprovalAdviceModel model = mcdApprovalAdviceService.getOne(modelWrapper);
        if (Objects.isNull(model)) {
            UserSimpleInfo simpleInfo = UserUtil.getUser(request);
            user.setUserId(simpleInfo.getUserId());
            user.setUserName(simpleInfo.getUserName());
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, model.getSubmitter()).last("limit 1");
        user = userService.getOne(queryWrapper);

        SubmitProcessJxDTO submitProcessDTO = new SubmitProcessJxDTO();
        submitProcessDTO.setUser(user);
        submitProcessDTO.setApprovalType(ConstApprove.APPROVE_TYPE_CAMP);

        communicationAppTaskSyncEmis(task, submitProcessDTO);
        return ActionResponse.getSuccessResp();
    }

    @ApiOperation(value = "智推棱镜：我的主题列表提交审批", notes = "智推棱镜：主题列表批量提交审批")
    @PostMapping("/batchSubApprove")
    public ActionResponse batchSubApprove(@RequestBody @Valid SubmitProcessJxDTO req) {
        User user = MetaHandler.getUser();
        approveService.batchSubmitCampseg(req, user);
        return ActionResponse.getSuccessResp();
    }

    //生成转至沟通人记录
    private void communicationApp(ApproveUserTaskBo task) throws Exception {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, task.getApprovalUser());
        User user = userService.getOne(wrapper);

        McdApprovalAdviceModel model = new McdApprovalAdviceModel();
        model.setId(DataUtil.generateId());
        model.setInstanceId(task.getInstanceId());
        model.setNodeId(task.getNodeId());
        model.setSubmitter(task.getSubmitter());
        model.setApprover(task.getApprovalUser());
        model.setApproverName(user.getUserName());
        model.setDealStatus(1);
        model.setDealOpinion(task.getAdvice());

        mcdApprovalAdviceService.saveOrUpdate(model);

        LambdaUpdateWrapper<McdApprovalAdviceModel> modelWrapper = new LambdaUpdateWrapper<>();
        modelWrapper.eq(McdApprovalAdviceModel::getInstanceId, task.getInstanceId())
                .eq(McdApprovalAdviceModel::getNodeId, task.getNodeId())
                .eq(McdApprovalAdviceModel::getDealStatus, 0).set(McdApprovalAdviceModel::getDealStatus, 1);
        mcdApprovalAdviceService.update(modelWrapper);


    }

    private void communicationAppTaskSyncEmis(ApproveUserTaskBo task, SubmitProcessJxDTO submitProcessDTO) {

        CmpApproveProcessInstance processInstance = instanceService.getOne(Wrappers.<CmpApproveProcessInstance>query().lambda()
                .eq(CmpApproveProcessInstance::getBusinessId, task.getServiceType())
                .orderByDesc(CmpApproveProcessInstance::getCreateDate).last("LIMIT 1"));
        CmpApproveProcessRecord processRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                .eq(CmpApproveProcessRecord::getInstanceId, task.getInstanceId())
                .eq(CmpApproveProcessRecord::getNodeId, task.getNodeId())
                .eq(CmpApproveProcessRecord::getDealStatus, DealStatus.PENDING));

        processRecord.setDealStatus(1);
        processRecord.setApprover(task.getApprovalUser());
        //删除emis代办
        approvePostProcessor.rejectProcess(submitProcessDTO, processInstance, processRecord);

        User user = submitProcessDTO.getUser();
        processRecord.setDealStatus(0);
        processRecord.setApprover(user.getUserId());
        //增加emis代办
        approvePostProcessor.approvalPendingProcess(submitProcessDTO, processInstance, processRecord);


    }

}
