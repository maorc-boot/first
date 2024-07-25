package com.asiainfo.biapp.pec.approve.jx.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.plan.model.McdPlanDef;
import com.asiainfo.biapp.pec.approve.Enum.DealStatus;
import com.asiainfo.biapp.pec.approve.dto.ApproverProcessDTO;
import com.asiainfo.biapp.pec.approve.jx.Enum.ConstApprove;
import com.asiainfo.biapp.pec.approve.jx.dto.*;
import com.asiainfo.biapp.pec.approve.jx.model.*;
import com.asiainfo.biapp.pec.approve.jx.service.*;
import com.asiainfo.biapp.pec.approve.jx.service.feign.CustomAlarmFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.CustomLabelFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.HisApproveRecordFeignClient;
import com.asiainfo.biapp.pec.approve.jx.service.feign.McdPecPlanFeignClient;
import com.asiainfo.biapp.pec.approve.jx.utils.EmisUtils;
import com.asiainfo.biapp.pec.approve.jx.utils.Pager;
import com.asiainfo.biapp.pec.approve.jx.vo.ApproveInfo;
import com.asiainfo.biapp.pec.approve.jx.vo.ApproveUserTaskH5Bo;
import com.asiainfo.biapp.pec.approve.jx.vo.McdCampChannelDetailVo;
import com.asiainfo.biapp.pec.approve.jx.vo.McdCampChannelExtVo;
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
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 移动办公H5接入API接口
 * <p>
 * ranpf
 * 2023-05-22
 */
@RestController
@RequestMapping("/api/mobile/")
@Api(tags = "江西H5审批相关接口")
@Slf4j
public class MobileH5ApproveController {


    @Autowired
    private ApproveRecordService approveRecordService;

    @Resource
    private IMcdSysUserRoleRelationService mcdSysUserRoleRelationService;

    @Autowired
    private ApproveService approveService;

    @Resource
    private IUserService userService;

    @Autowired
    private ICmpApprovalProcessJxService cmpApprovalProcessJxService;

    @Autowired
    private ICmpApproveProcessInstanceService cmpApproveProcessInstanceService;
    @Resource
    private ICmpApproveProcessNodeService cmpApproveProcessNodeService;

    @Resource
    private McdPecPlanFeignClient mcdPecPlanFeignClient;
    @Resource
    private HisApproveRecordFeignClient hisApproveRecordFeignClient;

    @Resource
    private McdCampWarnEmisTaskService mcdCampWarnEmisTaskService;

    @Resource
    private McdApprovalAdviceService mcdApprovalAdviceService;
    @Autowired
    private IApprovePostProcessor approvePostProcessor;
    @Autowired
    private ICmpApproveProcessInstanceService instanceService;
    @Autowired
    private ICmpApproveProcessRecordService recordService;
    @Autowired
    private IMcdCampDefService campDefService;

    @Resource
    private McdDimPlanTypeService mcdDimPlanTypeService;

    @Resource
    private IMcdCampChannelListService mcdCampChannelListService;


    @Resource
    private IMcdCampChannelExtAttrService mcdCampChannelExtAttrService;

    @Resource
    private IMcdCampChannelExtService mcdCampChannelExtService;

    @Resource
    private McdEmisReadTaskService mcdEmisReadTaskService;

    /**
     * 待审批策略信息查询
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("待审批查询接口")
    @PostMapping(value = "queryApproveInfo")
    public ActionResponse<Pager> queryApproveInfo(@RequestBody McdCampApproveH5Query recordH5) throws Exception {
        ActionResponse<Pager> actionResponse = new ActionResponse<>();
        Pager pager = new Pager();
        log.info("H5查询待审批活动接口入参: {}" + recordH5);
        try {

            McdCampApproveJxNewQuery record = new McdCampApproveJxNewQuery();
            record.setCampsegName(recordH5.getKeyword());
            record.setSize(Integer.parseInt(recordH5.getPageSize()));
            record.setCurrent(Integer.parseInt(recordH5.getPageNum()));
            record.setUserId(recordH5.getUserId());
            record.setChannelId(recordH5.getChannelId());

            List<ApproveUserTaskH5Bo> listResult = new ArrayList<>();
            IPage<McdCampCmpApproveProcessRecordJx> processRecordJxIPage = approveRecordService.queryCampAppRecord(record);

            boolean isLeader = false;
            List<Long> userRoles = mcdSysUserRoleRelationService.findRoleIDByUserID(recordH5.getUserId());
            String leaderRole = RedisUtils.getDicValue("leader_role_id");
            String scoreChannel = RedisUtils.getDicValue("score_channel");
            if (StringUtils.isNotEmpty(leaderRole) && userRoles.contains(leaderRole)) {
                isLeader = true;
            }
            for (McdCampCmpApproveProcessRecordJx temp : processRecordJxIPage.getRecords()) {
                ApproveUserTaskH5Bo h5Bo = new ApproveUserTaskH5Bo();

                h5Bo.setServiceType(temp.getBusinessId());
                h5Bo.setServiceName(temp.getCampsegName());
                h5Bo.setInstanceId(temp.getInstanceId() + "");
                h5Bo.setSystemId("IMCD");
                h5Bo.setSubmitter(temp.getCreateBy());
                h5Bo.setApprovalUser(temp.getApprover());
                h5Bo.setApprovalUserName(temp.getApproverName());
                h5Bo.setCampsegId(temp.getCampsegId());
                h5Bo.setNodeId(temp.getNodeId());
                h5Bo.setCepEventId(temp.getEventId());

                h5Bo.setScoreChannelId(scoreChannel);
                h5Bo.setLeader(String.valueOf(isLeader));

                listResult.add(h5Bo);
            }
            pager.setResult(listResult);
            actionResponse.setData(pager);
            actionResponse.setStatus(ResponseStatus.SUCCESS);

            return actionResponse;

        } catch (Exception e) {
            log.error("查询审批信息列表失败", e);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("查询审批信息列表失败");

        }
        return actionResponse;
    }

    /**
     * 获得下一批节点的流程审批人。每个节点的流程审批人可能有多个
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获得下一批节点的流程审批人接口")
    @PostMapping(value = "getNextApprovers")
    public ActionResponse<Map<String, Object>> getNextApprovers(@RequestBody MobileActionForm form) throws Exception {
        ActionResponse<Map<String, Object>> actionResponse = new ActionResponse<>();
        Map<String, Object> res = new HashMap<>();
        log.info("getNextApprovers 入参：{}", form);
        //当前登录用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, form.getUserId()).last("LIMIT 1");

        User user = userService.getOne(queryWrapper);
        long instanceId = form.getInstanceId() == null ? 0L : Long.parseLong(form.getInstanceId());
        String systemId = form.getSystemId() == null ? "IMCD":form.getSystemId();

        if (user == null) {
            res.put("status", ResponseStatus.ERROR);
            res.put("result", "无法获取用户失败");
            res.put("data", new JSONArray());
            actionResponse.setData(res);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("获取用户失败");
            return actionResponse;
        }

        SubmitProcessJxDTO submitProcessDTO = new SubmitProcessJxDTO();
        submitProcessDTO.setInstanceId(instanceId);
        submitProcessDTO.setApprovalType(systemId);
        submitProcessDTO.setUser(user);
        submitProcessDTO.setBusinessId(form.getCampsegId());
        submitProcessDTO.setNodeId(form.getNodeId());

        try {
            log.info("getNextApprovers submitProcessDTO：{}", submitProcessDTO);
            SubmitProcessJxDTO h5NodeApprover = approveService.getH5NodeApprover(submitProcessDTO);
            res.put("status", "200");
            res.put("data", getApproveUser(h5NodeApprover));
            actionResponse.setData(res);
            actionResponse.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("获取下级审批人列表失败", e);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("获取下级审批人列表失败");
        }

        return actionResponse;
    }


    /**
     * H5策略审批
     *
     * @throws Exception
     */
    @ApiOperation("策略审批接口")
    @PostMapping(value = "processApproveInfo")
    public ActionResponse<Map<String, Object>> processApproveInfo(@RequestBody MobileActionForm form) throws Exception {
        ActionResponse<Map<String, Object>> actionResponse = new ActionResponse<>();
        log.info("  H5processApproveInfo入参: {}", form);

        Map<String, Object> returnMap = new HashMap<>();
        try {

            ApproveInfo approveInfo = form.getApproveInfo();
            String campsegPId = form.getCampsegPId() == null ? form.getCampsegPid() : form.getCampsegPId();
            if(StrUtil.isEmpty(campsegPId)){
                campsegPId = form.getServiceType() ;
            }
            String userId = form.getUserId() == null ? approveInfo.getApprovalUserId() : form.getUserId();

            //当前登录用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserId, userId).last("LIMIT 1");

            User user = userService.getOne(queryWrapper);
            List<NodesApproverJx> nextNodesApprover = new ArrayList<>();
            List<Map<String, Object>> assignNodesApprover = form.getAssignNodesApprover();
            //流程实例
            Long instanceId = Long.parseLong(approveInfo.getInstanceId());
            CmpApproveProcessInstance processInstance = cmpApproveProcessInstanceService.getById(instanceId);
            if (Objects.isNull(processInstance)) {
                log.error(" H5审批流程实例不存在,instanceId : " + instanceId);
                return actionResponse;
            }
            long processId = processInstance.getProcessId();
            int berv = processInstance.getProcessVersionNum();

            for (Map<String, Object> nextMap : assignNodesApprover) {
                NodesApproverJx nodesApprover = new NodesApproverJx();
                nodesApprover.setNode(getNodeInfo(processId, berv, (String) nextMap.get("nodeId")));
                nodesApprover.setApproverUser(getNextApproveUser((String) nextMap.get("approveId")));
                nodesApprover.setProcessVersionNum(berv);
                nextNodesApprover.add(nodesApprover);
            }

            int appResult = Integer.parseInt(approveInfo.getResult());
            SubmitProcessJxDTO submitProcessDTO = new SubmitProcessJxDTO();
            submitProcessDTO.setUser(user);
            submitProcessDTO.setInstanceId(instanceId);
            submitProcessDTO.setNodeId(approveInfo.getNodeId());
            submitProcessDTO.setBusinessId(campsegPId);
            submitProcessDTO.setSubmitStatus(appResult);
            submitProcessDTO.setDealOpinion(approveInfo.getAdvice());
            submitProcessDTO.setBerv(berv);
            submitProcessDTO.setProcessId(processId);
            submitProcessDTO.setNextNodesApprover(nextNodesApprover);

            log.info("H5审批参数: submitProcessDTO "+ com.alibaba.fastjson.JSONObject.toJSONString(submitProcessDTO));
            Long submitted = cmpApprovalProcessJxService.submitProcess(submitProcessDTO);

            log.info("H5审批返回 instanceId "+ submitted);
            if (appResult == 1) {
                returnMap.put("status", "200");
                returnMap.put("result", "审批成功");
                returnMap.put("data", instanceId);
                actionResponse.setData(returnMap);
                actionResponse.setMessage("审批成功");
                actionResponse.setStatus(ResponseStatus.SUCCESS);
            } else if (appResult == 0) {
                returnMap.put("status", "200");
                returnMap.put("result", "驳回成功");
                returnMap.put("data", instanceId);
                actionResponse.setData(returnMap);
                actionResponse.setMessage("驳回成功");
                actionResponse.setStatus(ResponseStatus.SUCCESS);
            }
        } catch (Exception e) {
            returnMap.put("status", "500");
            returnMap.put("result", "审批失败");
            returnMap.put("data", "");
            actionResponse.setMessage("审批失败");
            actionResponse.setStatus(ResponseStatus.ERROR);
            log.error("H5提交审批异常", e);
        }
        return actionResponse;
    }


    /**
     * @param @param  mapping
     * @param @param  form
     * @param @param  request
     * @param @param  response
     * @param @return
     * @param @throws Exception
     * @return ActionForward
     * @throws
     * @Title: getLogRecordNew
     * @Description: 审批日志记录
     */
    @ApiOperation("H5查询审批日志记录")
    @PostMapping(value = "/getLogRecord")
    public ActionResponse<Map<String, Object>> getLogRecord(@RequestBody MobileActionForm form) {
        log.info("getLogRecord param: {}", JSONUtil.toJsonStr(form));
        ActionResponse<Map<String, Object>> actionResponse = new ActionResponse<>();
        Map<String, Object> returnMap = new HashMap<>();
        String campsegId = form.getCampsegId();
        ApproverProcessDTO approverProcessDTO = new ApproverProcessDTO();
        approverProcessDTO.setBusinessId(campsegId);
        log.info("start getApproverProcessNew param:{}", new JSONObject(approverProcessDTO).toString());
        String businessId = approverProcessDTO.getBusinessId();
        McdCampDef campDef = null;
        try {
            // 4.0的活动ID是16位
            if (businessId.length() <= 19) {
                ActionResponse response = hisApproveRecordFeignClient.listRecord(businessId);
                return ActionResponse.getSuccessResp(response.getData());
            }
            campDef = campDefService.getOne(Wrappers.<McdCampDef>lambdaQuery()
                    .eq(McdCampDef::getCampsegId, approverProcessDTO.getBusinessId()).last(" limit 1 "));
            long instanceId = campDef.getApproveFlowId() == null ? 0 : Long.parseLong(campDef.getApproveFlowId());
            approverProcessDTO.setInstanceId(instanceId);
            List<ApproverProcessNewDTO> approverProcessDTOList = approveRecordService.getApproverProcessNew(approverProcessDTO);
            List<Map<String, Object>> mapList = mapRestructure(approverProcessDTOList);

            returnMap.put("status", "200");
            returnMap.put("data", mapList);
            actionResponse.setData(returnMap);
            actionResponse.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("H5审批日志记录异常", e);
            List<Map<String, String>> mapList = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            map.put("node_name", "错误");
            map.put("node_no", "1");
            map.put("approve_result", "发生错误，审批系统未返回相关日志");
            map.put("approve_view", "");
            if (null != campDef) {
                map.put("approvaler_name", campDef.getCreateUserId());
                map.put("approve_date", DateFormatUtils.format(campDef.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            map.put("if_current_node", "");
            mapList.add(map);
            JSONArray jsonArray = JSONUtil.parseArray(mapList);
            returnMap.put("status", "500");
            returnMap.put("result", "无法获取外部审批ID,该策略没有提交审批或提交审批错误");
            returnMap.put("data", jsonArray);

            actionResponse.setData(returnMap);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("审批日志记录异常");
            return actionResponse;
        }
        return actionResponse;
    }

    /**
     * viewPolicyDetail H5查看策略详情
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("查看策略详情")
    @PostMapping(value = "/viewPolicyDetail")
    public ActionResponse<Map<String, Object>> viewPolicyDetail(@RequestBody MobileActionForm form) throws Exception {
        log.info("H5查看策略详情viewPolicyDetail入参: " + form.getCampsegId());
        String campsegId = form.getCampsegId() == null ? form.getCampsegPId() : form.getCampsegId();
        McdIdQuery idQuery = new McdIdQuery();
        idQuery.setId(campsegId);
        return mcdPecPlanFeignClient.viewPolicyDetail(idQuery);

    }


    /**
     * 查询沟通人列表
     *
     * @param form MobileActionForm
     * @return
     */
    @ApiOperation("H5查询沟通人列表")
    @PostMapping(value = "/queryCommunicationUsers")
    public ActionResponse queryCommunicationUsers(@RequestBody MobileActionForm form) {
        ActionResponse actionResponse = new ActionResponse<>();
        String pageNum = form.getPageNum() != null ? form.getPageNum() : "1";
        String pageSize = form.getPageSize() == null ? "8" : form.getPageSize();
        McdCommunicationQuery req = new McdCommunicationQuery();
        req.setCurrent(Integer.parseInt(pageNum));
        req.setSize(Integer.parseInt(pageSize));
        req.setKeyWords(form.getKeyWord() == null ? "" : form.getKeyWord());

        log.info("start H5queryCommunicationUsers para:{}", req);

        try {
            actionResponse = mcdPecPlanFeignClient.queryCommunicationUsers(req);
            actionResponse.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("H5查询沟通人列表异常", e);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("H5查询沟通人列表异常");
        }
        return actionResponse;
    }

    /**
     * 查询沟通人流程明细
     *
     * @param form MobileActionForm
     * @return
     */
    @ApiOperation("H5查询沟通人流程明细")
    @PostMapping(value = "/queryCommunicationTask")
    public ActionResponse<List<ApproveUserTaskH5Bo>> queryCommunicationTask(@RequestBody MobileActionForm form) {
        ActionResponse<List<ApproveUserTaskH5Bo>> actionResponse = new ActionResponse<>();
        List<ApproveUserTaskH5Bo> taskList = new ArrayList<>();
        ApproveUserTaskH5Bo task = form.getTask();
        try {
            log.info("H5沟通人审批信息查询入参:{} " + task);
            LambdaQueryWrapper<McdApprovalAdviceModel> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(McdApprovalAdviceModel::getInstanceId, task.getInstanceId())
                    .eq(McdApprovalAdviceModel::getNodeId, task.getNodeId())
                    .orderByAsc(McdApprovalAdviceModel::getDealTime);
            List<McdApprovalAdviceModel> adviceModelList = mcdApprovalAdviceService.list(queryWrapper);

            for (McdApprovalAdviceModel adviceModel : adviceModelList) {
                ApproveUserTaskH5Bo taskH5Bo = new ApproveUserTaskH5Bo();
                taskH5Bo.setInstanceId(adviceModel.getInstanceId());
                taskH5Bo.setNodeId(adviceModel.getNodeId());
                taskH5Bo.setApprovalUser(adviceModel.getApprover());
                taskH5Bo.setApprovalUserName(adviceModel.getApproverName());
                taskH5Bo.setApprovalTime(adviceModel.getDealTime());
                taskH5Bo.setCommunicationState(adviceModel.getDealStatus());
                taskH5Bo.setAdvice(adviceModel.getDealOpinion());
                taskList.add(taskH5Bo);
            }
            actionResponse.setData(taskList);
            actionResponse.setStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {
            log.error("查询沟通人流程明细异常", e);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("查询沟通人流程明细异常");
        }
        return actionResponse;
    }


    /**
     * 沟通人 "审批"
     *
     * @param form form
     * @return actionResponse
     */
    @ApiOperation("H5沟通人审批")
    @PostMapping(value = "/commitCommunicationTask")
    public ActionResponse<Map<String, Object>> commitCommunicationTask(@RequestBody MobileActionForm form) {
        ActionResponse<Map<String, Object>> actionResponse = new ActionResponse<>();
        Map<String, Object> returnMap = new HashMap<>();
        try {
            ApproveUserTaskH5Bo task = form.getTask();
            if (null != task) {

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

                String userId = model.getSubmitter() == null ? task.getApprovalUser() : model.getSubmitter();
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getUserId, userId).last("limit 1");
                User user = userService.getOne(queryWrapper);

                SubmitProcessJxDTO submitProcessDTO = new SubmitProcessJxDTO();
                submitProcessDTO.setUser(user);
                submitProcessDTO.setApprovalType(ConstApprove.APPROVE_TYPE_CAMP);

                communicationAppTaskSyncEmis(task, submitProcessDTO);


                returnMap.put("status", "200");
                returnMap.put("result", "沟通人审批成功");
                returnMap.put("data", "1");
                actionResponse.setData(returnMap);
                actionResponse.setMessage("沟通人审批成功");
                actionResponse.setStatus(ResponseStatus.SUCCESS);
            } else {
                returnMap.put("status", "200");
                returnMap.put("result", "参数为空");
                returnMap.put("data", "0");
                actionResponse.setData(returnMap);
                actionResponse.setMessage("参数为空");
                actionResponse.setStatus(ResponseStatus.SUCCESS);
            }
        } catch (Exception e) {
            log.error("沟通人审批失败", e);
            returnMap.put("status", "500");
            returnMap.put("result", "沟通人审批失败");
            returnMap.put("data", "");
            actionResponse.setData(returnMap);
            actionResponse.setMessage("沟通人审批失败");
            actionResponse.setStatus(ResponseStatus.ERROR);
        }
        return actionResponse;
    }


    /**
     * 查询预警待办明细
     *
     * @param campWarnMobile CampWarnMobile
     * @return ActionResponse<CampWarnEmisTaskModel>
     */
    @ApiOperation("H5查询预警待办明细")
    @PostMapping(value = "/getCampWarnEmisTask")
    public ActionResponse<Map<String, Object>> getCampWarnEmisTask(@RequestBody MobileActionForm campWarnMobile) {
        ActionResponse<Map<String, Object>> actionResponse = new ActionResponse<>();
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isEmpty(campWarnMobile.getCampsegId()) || StringUtils.isEmpty(campWarnMobile.getUniqueIdentifierId())) {
            log.error("H5查询预警待办明细getCampWarnEmisTask入参为空!  ");
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("查询活动预警明细入参为空!");
            return actionResponse;
        }

        try {
            String campsegId = campWarnMobile.getCampsegId();
            String uniqueIdentifierId = campWarnMobile.getUniqueIdentifierId();
            LambdaQueryWrapper<McdCampWarnEmisTask> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(McdCampWarnEmisTask::getCampsegId, campsegId)
                    .eq(McdCampWarnEmisTask::getUniqueIdentifierId, uniqueIdentifierId).last(" limit 1 ");
            McdCampWarnEmisTask campWarnEmisTask = mcdCampWarnEmisTaskService.getOne(queryWrapper);

            returnMap.put("status", "200");
            returnMap.put("data", campWarnEmisTask);
            actionResponse.setData(returnMap);
            actionResponse.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("查询活动预警明细数据异常", e);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("查询活动预警明细数据异常");
        }
        return actionResponse;
    }


    /**
     * H5预警处理接口
     *
     * @param campWarnMobile CampWarnMobile
     * @return
     */
    @ApiOperation("H5预警处理接口")
    @PostMapping(value = "/updateCampWarnTodoProcess")
    public ActionResponse updateCampWarnTodoProcess(@RequestBody CampWarnMobile campWarnMobile) {
        ActionResponse actionResponse = new ActionResponse<>();
        String uniqueIdentifierId = campWarnMobile.getUniqueIdentifierId();
        //预警处理状态，0:待处理；1：处理待办成功；2：已发送emis待办；3：发送emis待办失败或异常'
        int status = 1;
        try {
            net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
            jsonObject.put("UNIQUE_IDENTIFIER_ID", campWarnMobile.getUniqueIdentifierId());
            jsonObject.put("CAMPSEG_ID", campWarnMobile.getCampsegId());
            jsonObject.put("CREATER", campWarnMobile.getCreater());

            //预警待办转已办
            boolean resResult = EmisUtils.updateWarnMessage(jsonObject, campWarnMobile.getUserId());

            if (!resResult) {
                status = 3;
                log.error("H5updateCampWarnTodoProcess 待办转已办失败: {}", campWarnMobile.toString());
                actionResponse.setStatus(ResponseStatus.ERROR);
                actionResponse.setMessage("预警待办处理失败");
            } else {
                actionResponse.setStatus(ResponseStatus.SUCCESS);
                actionResponse.setData(1);
                actionResponse.setMessage("预警待办处理成功");
            }

            //修改状态
            LambdaUpdateWrapper<McdCampWarnEmisTask> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(McdCampWarnEmisTask::getStatus, status)
                    .set(McdCampWarnEmisTask::getWarnUpdateTime, new Date())
                    .eq(McdCampWarnEmisTask::getUniqueIdentifierId, uniqueIdentifierId);
            mcdCampWarnEmisTaskService.update(updateWrapper);

        } catch (Exception e) {
            log.error("预警待办处理异常：" + e.getMessage());
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("预警待办处理异常");
        }

        return actionResponse;
    }

    private List<Map<String, Object>> mapRestructure(List<ApproverProcessNewDTO> resultApproveProcessLsit) {

        List<Map<String, Object>> nodeInstanceList = new ArrayList<>();
        for (ApproverProcessNewDTO approverProcessDTO : resultApproveProcessLsit) {

            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Object> mapResult = new HashMap<String, Object>();
            mapResult.put("0", "待审批");
            mapResult.put("1", "通过");
            mapResult.put("2", "驳回");

            map.put("node_name", approverProcessDTO.getNodeName());
            map.put("node_no", approverProcessDTO.getNodeId());
            map.put("approve_result", mapResult.get(approverProcessDTO.getSubmitStatus() + ""));
            map.put("approve_view", approverProcessDTO.getDealOpinion());
            map.put("approvaler_name", approverProcessDTO.getApproverName());
            map.put("approve_date", approverProcessDTO.getCreateDate());
            map.put("if_current_node", "");
            map.put("approvalTimes", "1");
            map.put("communicationList", "");
            nodeInstanceList.add(map);

        }
        return nodeInstanceList;
    }


    //生成转至沟通人记录
    private void communicationApp(ApproveUserTaskH5Bo task) throws Exception {
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

    private void communicationAppTaskSyncEmis(ApproveUserTaskH5Bo task, SubmitProcessJxDTO submitProcessDTO) {

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


    private List<NodesApproverH5> getApproveUser(SubmitProcessJxDTO h5NodeApprover) {

        List<NodesApproverJx> nextNodesApprover = h5NodeApprover.getNextNodesApprover();
        if (nextNodesApprover.isEmpty()) {
            return null;
        }
        List<NodesApproverH5> nodesApproverH5List = new ArrayList<>();
        List<H5ApproveUser> userList = new ArrayList<>();
        for (NodesApproverJx nodesApproverJx : nextNodesApprover) {
            NodesApproverH5 nodesApproverH5 = new NodesApproverH5();
            List<SysUser> approverUser = nodesApproverJx.getApproverUser();
            for (SysUser sysUser : approverUser) {
                H5ApproveUser h5ApproveUser = new H5ApproveUser();
                h5ApproveUser.setId(sysUser.getUserId());
                h5ApproveUser.setName(sysUser.getUserName());
                h5ApproveUser.setCityId(sysUser.getCityId());
                h5ApproveUser.setStatus(sysUser.getStatus());
                h5ApproveUser.setDepartmentId(sysUser.getDepartmentId());
                h5ApproveUser.setMobilePhone(sysUser.getMobilePhone());
                h5ApproveUser.setCounty(sysUser.getCountyId());
                h5ApproveUser.setGridId(sysUser.getGridId());

                userList.add(h5ApproveUser);
            }

            CmpApproveProcessNode approverJxNode = nodesApproverJx.getNode();
            nodesApproverH5.setApprovers(userList);
            nodesApproverH5.setNodeId(approverJxNode.getNodeId());
            nodesApproverH5.setNodeName(approverJxNode.getNodeName());
            nodesApproverH5List.add(nodesApproverH5);
        }

        return nodesApproverH5List;
    }


    //查询审批人信息
    private List<SysUser> getNextApproveUser(String approveId) {

        List<SysUser> approveUser = new ArrayList<>();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, approveId).last("LIMIT 1");

        User user = userService.getOne(queryWrapper);
        // 用户对象信息转换
        SysUser sysUser = userConvert(user);
        approveUser.add(sysUser);
        return approveUser;
    }


    //获取节点信息
    private CmpApproveProcessNode getNodeInfo(long processId, int berv, String nodeId) {

        CmpApproveProcessNode processNode = cmpApproveProcessNodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                .eq(CmpApproveProcessNode::getProcessId, processId)
                .eq(CmpApproveProcessNode::getProcessVersionNum, berv)
                .eq(CmpApproveProcessNode::getNodeId, nodeId).last("LIMIT 1")
        );

        return processNode;
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


    @ApiOperation("H5查询产品明细")
    @PostMapping(value = "/getMtlStcPlan")
    public ActionResponse<Map<String, Object>> getMtlStcPlan(HttpServletRequest request, @RequestBody MobileActionForm form) throws Exception {
        ActionResponse<Map<String, Object>> actionResponse = new ActionResponse<>();
        Map<String, Object> returnMap = new HashMap<String, Object>();

        // “匹配的政策”
        try {
            // 子活动ID
            String campsegId = form.getCampsegId();

            JSONObject stcPlanJSON = new JSONObject();
            McdPlanDef stcPlan = new McdPlanDef();
            List<McdPlanDef> mcdPlanDefs = campDefService.queryPlanByCampsegId(campsegId);

            if (CollectionUtil.isNotEmpty(mcdPlanDefs)) {
                stcPlan = mcdPlanDefs.get(0);
                 McdDimPlanTypeModel dimPlanType = mcdDimPlanTypeService.getById(stcPlan.getPlanType());
                 if (Objects.isNull(dimPlanType)){
                     // 业务类型
                     stcPlanJSON.put("campDrvName",   "");
                 } else {
                     // 业务类型
                     stcPlanJSON.put("campDrvName", dimPlanType.getTypeName() != null ? dimPlanType.getTypeName() : "");
                 }

                //“政策名称”,政策即产品
                stcPlanJSON.put("stcPlanName", stcPlan.getPlanName() != null ? stcPlan.getPlanName() : "");
                //“编号”
                stcPlanJSON.put("stcPlanId", stcPlan.getPlanId() != null ? stcPlan.getPlanId() : "");
                //“有效期”开始时间
                stcPlanJSON.put("stcPlanStartdate",
                        stcPlan.getPlanStartDate() != null ? DateFormatUtils.format(stcPlan.getPlanStartDate(), "yyyy/MM/dd") : "");
                //“有效期”结束时间
                stcPlanJSON.put("stcPlanEnddate",
                        stcPlan.getPlanEndDate() != null ? DateFormatUtils.format(stcPlan.getPlanEndDate(), "yyyy/MM/dd") : "");
                //“描述”
                stcPlanJSON.put("stcPlanDesc", stcPlan.getPlanDesc() != null ? stcPlan.getPlanDesc() : "");
                // 同系列产品
                // String evaLuationStrs = mcdEvaluationPlanListService.getEvaluationPlans(campsegId, planId);
                stcPlanJSON.put("stcPlanEvaluationPlanList", "--");
                // 融合产品
                // String fixStrs = mcdFixPlanListService.getFixPlans(campsegId,planId);
                stcPlanJSON.put("stcPlanFixPlanList", "--");
            }

            request.setAttribute("stcPlanJSONStr", stcPlanJSON.toString());
            request.setAttribute("stcPlanJSON", stcPlanJSON);
            returnMap.put("status", "200");
            returnMap.put("data", stcPlanJSON);

            actionResponse.setData(returnMap);
            actionResponse.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("获取匹配的政策异常", e);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("获取匹配的政策异常");
        }
        return actionResponse;
    }


    @ApiOperation("H5查询客户群明细")
    @PostMapping(value = "/getTargetCustomerbase")
    public ActionResponse<Map<String, Object>> getTargetCustomerbase(@RequestBody MobileActionForm form) throws Exception {
        ActionResponse<Map<String, Object>> actionResponse = new ActionResponse<>();
        Map<String, Object> returnMap = new HashMap<>();
        //投放的渠道
        try {
            List<McdCustgroupDef> mcdCustgroupDefs = campDefService.queryCustByCampsegId(form.getCampsegId());
            JSONObject customDataJSON = new JSONObject();
            if (CollectionUtil.isNotEmpty(mcdCustgroupDefs)) {
                McdCustgroupDef def = mcdCustgroupDefs.get(0);
                // 客户群编码
                customDataJSON.put("custom_group_id", StrUtil.nullToEmpty(def.getCustomGroupId()));
                // 客户群名称
                customDataJSON.put("custom_group_name", StrUtil.nullToEmpty(def.getCustomGroupName()));
                // 客户群描述
                customDataJSON.put("custom_group_desc", StrUtil.nullToEmpty(def.getCustomGroupDesc()));
                // 客户群周期
                customDataJSON.put("update_cycle", StrUtil.nullToEmpty(def.getUpdateCycle() == null ? "" : def.getUpdateCycle().toString()));
                // 创建时间
                customDataJSON.put("create_time", def.getCreateTime() == null ? "" : DateUtil.format(def.getCreateTime(), "yyyyMMdd HH:mm:ss"));
                // 修建人
                customDataJSON.put("create_user_id", StrUtil.nullToEmpty(def.getCreateUserId()));
                // 客户群数量
                customDataJSON.put("custom_num", def.getCustomNum());
                // 客户群来源
                customDataJSON.put("custom_source_id", def.getCustomSourceId());
                // 客户群规则
                customDataJSON.put("rule_desc", def.getRuleDesc());
                // 最新数据日期
                customDataJSON.put("max_data_time", def.getDataDate());
                // 客户群实际数量
                customDataJSON.put("sum_custom_num", def.getActualCustomNum());
                customDataJSON.put("show_sql", "");
                customDataJSON.put("custgroup_number", def.getCustomNum());
                List<Map<String, Object>> mtlChannelDefs = campDefService.getMtlChannelDefs(form.getCampsegId());

                List<Map<String, Object>> mtltlChannelDefList = new ArrayList<Map<String, Object>>();

                for (int i = 0; i < mtlChannelDefs.size(); i++) {
                    Map<String, Object> map = (Map<String, Object>) mtlChannelDefs.get(i);
                    if(map.get("EXEC_STATUS") == null){
                        map.put("EXEC_STATUS", "");
                    }
                    if(map.get("PAUSE_COMMENT") == null){
                        map.put("PAUSE_COMMENT", "");
                    }
                    map.put("custgroup_number", def.getCustomNum() == null ? "" : def.getCustomNum().toString());
                    map.put("CHANNEL_NAME", map.get("channel_name"));
                    map.put("TARGER_USER_NUMS", def.getCustomNum());
                    mtltlChannelDefList.add(map);
                }

                JSONArray jsonArray = JSONUtil.parseArray(mtltlChannelDefList);
                customDataJSON.put("mtlChannelDefs", jsonArray);
            }



            returnMap.put("status", "200");
            returnMap.put("data", customDataJSON);
            actionResponse.setData(returnMap);
            actionResponse.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("获取目标客户群异常", e);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("获取目标客户群异常");
        }
        return actionResponse;
    }

    @PostMapping("/addCommunicationTask")
    @ApiOperation(value = "江西H5转至沟通人添加任务接口", notes = "H5转至沟通人添加任务接口")
    public ActionResponse addCommunicationTask(@RequestBody JSONObject json, HttpServletRequest request) throws Exception {
        String jsonStr =  json.get("task").toString();
        ApproveUserTaskBo task = com.alibaba.fastjson.JSONObject.parseObject(jsonStr,ApproveUserTaskBo.class);
        log.info("H5转至沟通人入参:{} " , task);
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

    @PostMapping("/getDeliveryChannel")
    @ApiOperation(value = "江西H5策略详情渠道信息查询接口", notes = "江西H5策略详情渠道信息查询接口")
    public ActionResponse<Map<String, Object>> getDeliveryChannel(HttpServletRequest request, @RequestBody MobileActionForm form) throws Exception {
        ActionResponse actionResponse = ActionResponse.getSuccessResp();

        //投放的渠道
        try {
            List<McdCampChannelDetailVo> deliveryChannelList = mcdCampChannelListService.getDeliveryChannel(form.getCampsegId());

            for (McdCampChannelDetailVo detailVo : deliveryChannelList) {
                List<McdCampChannelExtVo> mcdChannelDefExtList = new ArrayList<>();
                LambdaQueryWrapper<McdCampChannelExt> extQuWrap = new LambdaQueryWrapper();
                extQuWrap.eq(McdCampChannelExt::getCampsegId,detailVo.getCampsegId());
                Map<String, Object> channelExtMap = mcdCampChannelExtService.getMap(extQuWrap);
                if(CollectionUtil.isEmpty(channelExtMap)){
                    continue;
                }
                LambdaQueryWrapper<McdCampChannelExtAttr> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(McdCampChannelExtAttr::getChannelId,detailVo.getChannelId());
                queryWrapper.eq(StringUtils.isNotEmpty(detailVo.getAdivId()),McdCampChannelExtAttr::getChannelAdivId,detailVo.getAdivId());
                List<McdCampChannelExtAttr> extAttrList = mcdCampChannelExtAttrService.list(queryWrapper);
                for (McdCampChannelExtAttr channelExtAttr : extAttrList) {
                    McdCampChannelExtVo extVo = new McdCampChannelExtVo();
                    String value = channelExtMap.get(channelExtAttr.getAttrName()) == null ? "":channelExtMap.get(channelExtAttr.getAttrName()).toString();
                    extVo.setAttrDisplayValue(value);
                    extVo.setAttrDisplayName(channelExtAttr.getAttrDisplayName());
                    mcdChannelDefExtList.add(extVo);
                }
                detailVo.setMcdChannelDefExtList(mcdChannelDefExtList);
            }
            actionResponse.setData(deliveryChannelList);

        } catch (Exception e) {
            log.error("获取投放渠道异常", e);
            actionResponse.setStatus(ActionResponse.getFaildResp().getStatus());
            actionResponse.setMessage("获取投放渠道信息异常");
        }

        return actionResponse;
    }

    /**
     * 查询预警待办明细
     *
     * @param campWarnMobile CampWarnMobile
     * @return ActionResponse<CampWarnEmisTaskModel>
     */
    @ApiOperation("H5查询阅知待办明细")
    @PostMapping(value = "/getReadEmisTask")
    public ActionResponse<Map<String, Object>> getReadEmisTask(@RequestBody MobileActionForm campWarnMobile) {
        ActionResponse<Map<String, Object>> actionResponse = new ActionResponse<>();
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isEmpty(campWarnMobile.getCampsegId()) || StringUtils.isEmpty(campWarnMobile.getUniqueIdentifierId())) {
            log.error("H5查询预警待办明细getCampWarnEmisTask入参为空!  ");
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("查询活动预警明细入参为空!");
            return actionResponse;
        }
        try {
            String campsegPId = campWarnMobile.getCampsegPId();
            String uniqueIdentifierId = campWarnMobile.getUniqueIdentifierId();
            LambdaQueryWrapper<McdEmisReadTask> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(McdEmisReadTask::getId, uniqueIdentifierId).last(" limit 1 ");
            McdEmisReadTask campWarnEmisTask = mcdEmisReadTaskService.getOne(queryWrapper);
            returnMap.put("status", "200");
            returnMap.put("data", campWarnEmisTask);
            actionResponse.setData(returnMap);
            actionResponse.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("查询活动预警明细数据异常", e);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("查询活动预警明细数据异常");
        }
        return actionResponse;
    }

    /**
     * H5预警处理接口
     *
     * @param campWarnMobile CampWarnMobile
     * @return
     */
    @ApiOperation("H5阅知处理接口")
    @PostMapping(value = "/updateReadTodoProcess")
    public ActionResponse updateReadTodoProcess(@RequestBody CampWarnMobile campWarnMobile) {
        ActionResponse actionResponse = new ActionResponse<>();
        String uniqueIdentifierId = campWarnMobile.getUniqueIdentifierId();
        //处理状态，0:待处理；1：处理待办成功；2：已发送emis待办；3：发送emis待办失败或异常'
        int status = 1;
        try {
            // 1.查询需要被消除阅知的活动或预警信息
            LambdaQueryWrapper<McdEmisReadTask> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(McdEmisReadTask::getStatus, 0)
                    .eq(McdEmisReadTask::getId,uniqueIdentifierId);
            McdEmisReadTask one = mcdEmisReadTaskService.getOne(queryWrapper);
            if (ObjectUtil.isEmpty(one)) {
                log.error("updateRejectEmisReadTask-->没有需要更新阅知为已阅的待办");
                actionResponse.setStatus(ResponseStatus.ERROR);
                actionResponse.setMessage("阅知待办处理异常");
                return actionResponse;
            }
            // 2.组装消除阅知emis接口调用入参
            net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
            jsonObject.put("unid", one.getId());
            // 需要阅知的人(创建人)
            jsonObject.put("approvalUserId", one.getReadUser());
            jsonObject.put("nodeId", one.getNodeId());
            // 增加阅知的人(审批节点驳回人)
            jsonObject.put("createUserid", one.getSubmitUser());
            // 3.消除emis侧阅知待办 待办转已阅
            boolean updateReadMessage = EmisUtils.updateReadMessage(jsonObject, one.getReadUser(), one.getBusinessId());
            log.info("updateRejectEmisReadTask-->阅知待办转已阅返回updateReadMessage={}", updateReadMessage);
            if (!updateReadMessage) {
                status = 3;
                log.error("H5updateCampWarnTodoProcess 待办转已办失败: {}", campWarnMobile.toString());
                actionResponse.setStatus(ResponseStatus.ERROR);
                actionResponse.setMessage("阅知待办处理失败");
            } else {
                actionResponse.setStatus(ResponseStatus.SUCCESS);
                actionResponse.setData(1);
                actionResponse.setMessage("阅知待办处理成功");
            }

            //修改状态
            LambdaUpdateWrapper<McdEmisReadTask> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(McdEmisReadTask::getStatus, status)
                    .set(McdEmisReadTask::getDealTime, new Date())
                    .eq(McdEmisReadTask::getId, uniqueIdentifierId);
            mcdEmisReadTaskService.update(updateWrapper);

        } catch (Exception e) {
            log.error("阅知待办处理异常：" , e);
            actionResponse.setStatus(ResponseStatus.ERROR);
            actionResponse.setMessage("阅知待办处理异常");
        }

        return actionResponse;
    }

}
