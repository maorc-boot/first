package com.asiainfo.biapp.pec.approve.jx.event;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.approve.Enum.DealStatus;
import com.asiainfo.biapp.pec.approve.common.Event;
import com.asiainfo.biapp.pec.approve.common.EventInterface;
import com.asiainfo.biapp.pec.approve.dto.UpdateCampStatusDTO;
import com.asiainfo.biapp.pec.approve.jx.dao.McdCampDefDao;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.jx.po.MaterialAuditFallBackPO;
import com.asiainfo.biapp.pec.approve.jx.service.Chan968MaterialAuditService;
import com.asiainfo.biapp.pec.approve.jx.service.ICmpApprovalProcessJxService;
import com.asiainfo.biapp.pec.approve.jx.service.IMaterialAuditFallBackService;
import com.asiainfo.biapp.pec.approve.jx.service.feign.ElementMatermialJxService;
import com.asiainfo.biapp.pec.approve.jx.vo.ApprovalProcessCampDef;
import com.asiainfo.biapp.pec.approve.jx.vo.DnaResponseDataVO;
import com.asiainfo.biapp.pec.approve.model.*;
import com.asiainfo.biapp.pec.approve.service.*;
import com.asiainfo.biapp.pec.approve.service.feign.PlanService;
import com.asiainfo.biapp.pec.approve.util.DataUtil;
import com.asiainfo.biapp.pec.approve.util.NetUtils;
import com.asiainfo.biapp.pec.approve.util.RequestUtils;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.RestFullUtil;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 江西策划流程结束事件
 *
 * @author lvcc
 * @date 2023/08/29
 */
@Component("processApprovalJxEvent")
@Event(name = "江西策划流程结束事件", value = "processApprovalJxEvent")
@Slf4j
public class ProcessApprovalJxEvent implements EventInterface {

    @Autowired
    private PlanService planService;

    @Autowired
    private ICmpApproveEventLogService logService;

    @Autowired
    private McdCampDefDao mcdCampDefDao;

    @Autowired
    private Chan968MaterialAuditService chan968MaterialAuditService;

    @Autowired
    IMaterialAuditFallBackService materialAuditFallBackService;

    @Autowired
    ICmpApprovalProcessJxService cmpApprovalProcessJxService;

    @Autowired
    private ElementMatermialJxService elementMatermialJxService;

    @Autowired
    private ICmpApproveProcessInstanceService instanceService;

    @Autowired
    private ICmpApproveProcessNodeService nodeService;

    @Autowired
    private ICmpApproveProcessRecordService recordService;

    @Autowired
    private IMcdCampOperateLogService operateLogService;

    /**
     * 是否需要调广点通接口审核素材 1-不需要 2-需要
     */
    @Value("${chan968.materialAudit.needPost:2}")
    private String needPost;

    @Value("${dna.approve.processUrl:http://10.19.92.21:13086/dna/dna-web/provider/customer/activity-ref/add}")
    private String approveServiceUrl;

    @Override
    @Async
    public void invoke(CmpApproveProcessRecord record) {
        CmpApproveEventLog eventLog = CmpApproveEventLog.builder()
                .id(DataUtil.generateId())
                .businessId(record.getBusinessId())
                .eventId(record.getEventId())
                .nodeId(record.getNodeId())
                .nodeName(record.getNodeName())
                .instanceId(record.getInstanceId())
                .build();
        try {
            Thread.sleep(10 * 1000);
            // 广点通968--活动素材审核 非968渠道会直接返回true 968渠道必须全部(多波次情况下)审核通过才返回true
            Map<String, Object> map = postMaterialAudit(record);
            if ((Boolean) map.get("chkBool")) { // 审核通过
                log.info("开始节点结束事件");
                log.info("CmpApproveProcessRecord:{}", new JSONObject(record).toString());
                UpdateCampStatusDTO dto = new UpdateCampStatusDTO();
                dto.setCampsegRootId(record.getBusinessId());
                dto.setFlowId(record.getInstanceId().toString());
                dto.setCampStat(52);
                log.info("updateCampStatus para:{}", new JSONObject(dto));
                ActionResponse<Boolean> actionResponse = planService.updateCampStatus(dto);
                log.info("updateCampStatus return:{}", new JSONObject(actionResponse));
                if (actionResponse != null) {
                    int code = actionResponse.getStatus().getCode();
                    if (code == ResponseStatus.SUCCESS.getCode()) {
                        eventLog.setEventStatus(0);
                    } else {
                        eventLog.setEventStatus(1);
                    }
                    eventLog.setEventMessage(actionResponse.getMessage());
                } else {
                    eventLog.setEventStatus(1);
                    eventLog.setEventMessage("调用策划状态修改接口无返回");
                }
                // Map<String, Object> sourceByRootId = mcdCampDefDao.queryCustgroupSourceByRootId(record.getBusinessId());
                // log.info("ProcessApprovalJxEvent-->活动={}客群来源={}", sourceByRootId.get("CAMPSEG_ROOT_ID"), sourceByRootId.get("CUSTGROUP_SOURCE"));
                // // 使用DNA客群创建的活动，同步活动信息到DNA侧
                // if (1 == Integer.parseInt(String.valueOf(sourceByRootId.get("CUSTGROUP_SOURCE")))) {
                //     syncIOPCamp2DNA(record);
                // }
            } else {
                CmpApproveProcessInstance processInstance = instanceService.getById(record.getInstanceId());
                // 驳回  需要修改iop的审批流为驳回以及素材状态为审批驳回==>需修改素材信息,再次提交审批
                // 1. 组装审批流驳回入参
                SubmitProcessJxDTO submitProcessJxDTO = buildApproveRejectParam(record, processInstance);
                // 1.1 审批流驳回
                approveFlowReject(processInstance, submitProcessJxDTO, record);
                // 2. 修改iop素材状态为审批驳回
                materialsReject(record, eventLog, map);
            }
        } catch (Exception e) {
            log.error("processApprovalJxEvent error:{}", e.toString());
            eventLog.setEventStatus(1);
            eventLog.setEventMessage(e.getMessage());
        }
        logService.saveEventLog(eventLog);
    }

    /**
     * 审批流驳回
     *
     * @param processInstance processInstance
     * @param submitProcessJxDTO submitProcessJxDTO
     */
    private void approveFlowReject(CmpApproveProcessInstance processInstance, SubmitProcessJxDTO submitProcessJxDTO, CmpApproveProcessRecord record) {
        // 获取当前节点
        CmpApproveProcessNode thisNode = nodeService.getOne(Wrappers.<CmpApproveProcessNode>query().lambda()
                .eq(CmpApproveProcessNode::getProcessId, submitProcessJxDTO.getProcessId())
                .eq(CmpApproveProcessNode::getProcessVersionNum, submitProcessJxDTO.getBerv())
                .eq(CmpApproveProcessNode::getNodeId, submitProcessJxDTO.getNodeId()).last("LIMIT 1")
        );
        CmpApproveProcessRecord thisRecord = recordService.getOne(Wrappers.<CmpApproveProcessRecord>query().lambda()
                // 审批人Ids
                .eq(CmpApproveProcessRecord::getApprover, record.getApprover())
                // 流程实例ID
                .eq(CmpApproveProcessRecord::getInstanceId, submitProcessJxDTO.getInstanceId())
                .eq(CmpApproveProcessRecord::getNodeId, submitProcessJxDTO.getNodeId())
                // 处理状态
                .in(CmpApproveProcessRecord::getDealStatus, DealStatus.PASS)); // 这地方的状态为1原因：审批流审批通过才会调素材审核接口，所以审批流审核通过的时候，这个状态会被改为1；
        // 1.1 审批流驳回
        cmpApprovalProcessJxService.approvalRejected(processInstance, thisNode, thisRecord, null, submitProcessJxDTO);
        //策略操作记录实例
        McdCampOperateLog operateLog = McdCampOperateLog.builder()
                .id(DataUtil.generateId().toString())
                .logDesc("审批驳回")
                .logTime(new Date())
                .campsegId(submitProcessJxDTO.getBusinessId())
                .logType(4)
                .userId(record.getApprover())
                .userName(record.getApproverName())
                .userComment("审批驳回")
                .opResult("成功")
                .build();
        final HttpServletRequest request = RequestUtils.getRequest();
        if (null != request) {
            operateLog.setUserIpAddr(NetUtils.getClientIp(request));
            operateLog.setUserBrower(NetUtils.getClientBrowser(request));
        }
        operateLogService.save(operateLog);
        UpdateCampStatusDTO updateCampStatusDTO = new UpdateCampStatusDTO();
        updateCampStatusDTO.setCampsegRootId(processInstance.getBusinessId());
        updateCampStatusDTO.setFlowId(processInstance.getInstanceId().toString());
        updateCampStatusDTO.setCampStat(41); // 审批退回
        planService.updateCampStatus(updateCampStatusDTO);
    }

    /**
     * 素材驳回
     *
     * @param record record
     * @param eventLog eventLog
     * @param map map
     */
    private void materialsReject(CmpApproveProcessRecord record, CmpApproveEventLog eventLog, Map<String, Object> map) {
        Set materialIds = (Set) map.get("materialIds"); //多波次情况下，素材id为多个
        for (Object materialId : materialIds) {
            MaterialStatusQuery req = new MaterialStatusQuery();
            req.setFlowId((String)materialId); // 素材id
            req.setMaterialStat(4); // 素材驳回状态
            ActionResponse actionResponse = elementMatermialJxService.updateMaterialApproveRejectStatus(req);
            log.info("活动={},素材={}修改审批驳回返回={}", record.getBusinessId(), materialId, JSONUtil.toJsonStr(actionResponse));
            if (actionResponse.getStatus().equals("ERROR")) {
                log.warn("活动={},素材={},修改素材状态异常", record.getBusinessId(), materialId);
                eventLog.setEventStatus(1);
                eventLog.setEventMessage("广点通渠道调用素材状态修改接口无返回");
                break;
            }
        }
        eventLog.setEventStatus(0);
        eventLog.setEventMessage("广点通渠道审批流、素材状态修改成功");
    }

    /**
     * 组装审批流驳回入参
     *
     * @param record 记录
     * @return {@link SubmitProcessJxDTO}
     */
    private SubmitProcessJxDTO buildApproveRejectParam(CmpApproveProcessRecord record, CmpApproveProcessInstance processInstance) throws Exception{
        SubmitProcessJxDTO submitProcessDTO = new SubmitProcessJxDTO();
        submitProcessDTO.setInstanceId(record.getInstanceId());
        submitProcessDTO.setNodeId(record.getNodeId());
        submitProcessDTO.setBusinessId(record.getBusinessId());
        submitProcessDTO.setSubmitStatus(0);
        submitProcessDTO.setDealOpinion(record.getDealOpinion());
        submitProcessDTO.setNextNodesApprover(new ArrayList<>());
        submitProcessDTO.setProcessId(processInstance.getProcessId());
        submitProcessDTO.setBerv(processInstance.getProcessVersionNum());
        submitProcessDTO.setApprovalType("IMCD");
        log.info("buildApproveRejectParam-->活动={}修改审批流驳回入参={}", record.getBusinessId(), JSONUtil.toJsonStr(submitProcessDTO));
        return submitProcessDTO;
    }

    /**
     * 前置校验--素材审核校验
     * 968渠道需要校验  其他渠道跳过直接返回true
     *
     * @param record 记录
     */
    private Map<String, Object> postMaterialAudit(CmpApproveProcessRecord record) throws Exception{
        Map<String, Object> res = new HashMap<>();
        Set<String> materialIds = Collections.synchronizedSet(new HashSet<>()); // 审核失败的素材id集合
        try {
            // 根据策略根id查询相关信息
            List<Map<String, Object>> mapList = mcdCampDefDao.qryChannelIdByCampsegRootId(record.getBusinessId());
            Set<String> channelIds = mapList.stream().map(p -> (String) p.get("CHANNEL_ID")).collect(Collectors.toSet());
            log.info("活动={}的渠道集合={}", record.getBusinessId(), JSONUtil.toJsonStr(channelIds));
            int count = 0; // 968渠道计数
            int succCount = 0; // 审核通过计数
            if (channelIds.contains("968")) { // 广点通968渠道才需走此逻辑
                for (Map<String, Object> map : mapList) {
                    if (!StrUtil.equals("968", (String)map.get("CHANNEL_ID"))) {
                        continue;
                    }
                    count++;
                    // 广点通968--需要调 2.1 【活动素材审核接口】统一互联平台提供
                    // 1. 组装入参
                    try {
                        JSONObject jsonObject = buildParam(map);
                        // 2. 请求素材审核接口==>广点通回调IOP素材审核接口,IOP将素材审核结果保存MATERIAL_AUDIT_FALLBACK表
                        if ("2".equals(needPost)) {
                            chan968MaterialAuditService.postMaterialAudit(jsonObject);
                        }
                        // 3. 根据素材id查询素材审核结果是否已回传入表
                        JSONObject materialAuditFallBackRes = chkMaterialAuditFallBackRes(map, record);
                        if (materialAuditFallBackRes.getBool("status") && "1".equals(materialAuditFallBackRes.get("obj", MaterialAuditFallBackPO.class).getMainAuditStatus())) {
                            materialIds.add((String) map.get("COLUMN_EXT2"));
                            log.error("素材={}审核不通过", map.get("COLUMN_EXT2"));
                            continue;
                        }
                        // 4. 若审核结果入表且审核通过则succCount+1
                        succCount++;
                    } catch (Exception e) {
                        log.error("请求素材审核并获取素材审核反馈数据异常：", e);
                        throw new Exception("请求素材审核并获取素材审核反馈数据异常");
                    }
                }
                log.info("postMaterialAudit-->活动={}是968渠道的有={}个，素材审核校验通过={}个", record.getBusinessId(), count, succCount);
                // 5. 若968渠道全部审核通过，则才算审核结束并通过(968渠道总数=审核通过数量)
                if (count == succCount) {
                    res.put("chkBool", true);
                    res.put("materialIds", materialIds);
                } else {
                    // 6. 若968渠道没有全部审核通过(968渠道总数!=审核通过数量)
                    res.put("chkBool", false);
                    res.put("materialIds", materialIds);
                }
            } else {
                log.info("postMaterialAudit-->活动={}未使用广点通渠道！", record.getBusinessId());
                res.put("chkBool", true);
                res.put("materialIds", materialIds);
            }
        } catch (Exception e) {
            log.error("活动={}调素材审核通知接口异常：", record.getBusinessId(), e);
            throw new Exception("活动=" + record.getBusinessId() + "调素材审核通知接口异常");
        }
        log.info("postMaterialAudit-->活动={}审核返回={}", record.getBusinessId(), JSONUtil.toJsonStr(res));
        return res;
    }

    /**
     * 查询素材审核反馈数据是否已入表
     *
     * @param map             auditFallBackPO
     * @return boolean        true--已入表
     * @throws InterruptedException 中断异常
     */
    private JSONObject chkMaterialAuditFallBackRes(Map<String, Object> map, CmpApproveProcessRecord record) throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        MaterialAuditFallBackPO auditFallBackPO = materialAuditFallBackService.getById((String) map.get("COLUMN_EXT2"));
        while (true) {
            if (ObjectUtil.isNotEmpty(auditFallBackPO)) {
                log.warn("活动={}，素材id={}审核反馈数据已入表！", record.getBusinessId(), map.get("COLUMN_EXT2"));
                jsonObject.putOpt("status", true);
                jsonObject.putOpt("obj", auditFallBackPO);
                return jsonObject;
            }
            log.warn("活动={}，素材id={}审核反馈数据还未入表！", record.getBusinessId(), map.get("COLUMN_EXT2"));
            TimeUnit.MINUTES.sleep(1L); //休眠一分钟再试
            auditFallBackPO = materialAuditFallBackService.getById((String) map.get("COLUMN_EXT2"));
        }
    }

    /**
     * 构建请求入参
     *
     * @param map 根据策略根id查询相关信息
     * @return {@link JSONObject}
     */
    private JSONObject buildParam(Map<String, Object> map) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("ACTIVITY_ID", map.get("CAMPSEG_ID")); // 推荐活动编码
        jsonObject.putOpt("Channel_ID", (String)map.get("CHANNEL_ID")); // 渠道编码
        jsonObject.putOpt("SLOT_ID", (String)map.get("CHANNEL_ADIV_ID")); // 运营位编码
        jsonObject.putOpt("Material_ID", map.get("COLUMN_EXT2")); // 素材编码
        jsonObject.putOpt("Material_Name", map.get("COLUMN_EXT3")); // 素材名称
        jsonObject.putOpt("Material_Type", "1"); // 素材类型 1：图片 2：文本 3：视频
        Date startDate = (Date) map.get("START_DATE");
        jsonObject.putOpt("Begin_Time", StrUtil.split(DateUtil.format(startDate, "yyyy-MM-dd HH:mm:ss"), " ").get(0)); // 生效时间
        Date endDate = (Date) map.get("END_DATE");
        jsonObject.putOpt("End_Time", StrUtil.split(DateUtil.format(endDate, "yyyy-MM-dd HH:mm:ss"), " ").get(0)); // 失效时间
        // 获取文件的base64编码
        String base64 = getBase64FromSftpFile(map);
        jsonObject.putOpt("Source_Content", base64); // 素材文件流
        if (ObjectUtil.isNotEmpty(map.get("COLUMN_EXT5"))) jsonObject.putOpt("Description", map.get("COLUMN_EXT5")); // 描述
        jsonObject.putOpt("LINK_URL", ObjectUtil.isNotEmpty(map.get("COLUMN_EXT6")) ? map.get("COLUMN_EXT6") : ""); // 跳转Url
        return jsonObject;
    }

    /**
     * 登录sftp，并将文件转为base64
     *
     * @param map map
     * @return {@link String}
     * @throws SftpException
     */
    private String getBase64FromSftpFile(Map<String, Object> map) throws Exception {
        String sftpDir = RedisUtils.getDicValue("MCD_MATERIAL_SFTP_PATH"); // 获取配置的sftp地址
        // 获取element服务存放的本地路径 并截取获取文件名
        String localpath = (String) map.get("COLUMN_EXT4");
        String fileName = localpath.substring(localpath.lastIndexOf(StrUtil.SLASH) + 1);
        if (!StrUtil.endWith(sftpDir, StrUtil.SLASH)) {
            sftpDir += StrUtil.SLASH;
        }
        SftpUtils sftpUtils = new SftpUtils();
        String sftpServer = RedisUtils.getDicValue("MCD_MATERIAL_SFTP_IP");
        int sftpPort = Integer.parseInt(RedisUtils.getDicValue("MCD_MATERIAL_SFTP_PORT"));
        String username = RedisUtils.getDicValue("MCD_MATERIAL_SFTP_USERNAME");
        String password = RedisUtils.getDicValue("MCD_MATERIAL_SFTP_PASSWORD");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChannelSftp sftp = null;
        InputStream inputStream = null;
        try {
            sftp = sftpUtils.connect(sftpServer, sftpPort, username, password);
            inputStream = sftp.get(sftpDir + fileName);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("getBase64FromSftpFile-->连接sftp获取图片转base64编码异常：", e);
        } finally {
            if (inputStream != null && sftp != null) {
                try {
                    inputStream.close();
                    sftpUtils.disconnect(sftp);
                } catch (JSchException e) {
                    log.error("关闭sftp连接异常",e);
                }
            }
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }


    /**
     * 江西IOP活动信息同步到标签DNA
     *
     * @param record 审批活动信息
     */
    private void syncIOPCamp2DNA(CmpApproveProcessRecord record) {
        log.info("江西IOP活动使用的DNA客群，信息同步到标签侧开始，入参={}", JSONUtil.toJsonStr(record));
        // 调用DNA活动信息同步接口(1.7)
        // 1. 只查询并同步第一波次的活动信息
        List<ApprovalProcessCampDef> processCampDef = mcdCampDefDao.queryCampInfoByRootId(record.getBusinessId());
        if (CollectionUtils.isEmpty(processCampDef)) return;
        // 2. 组装信息同步数据
        processCampDef.forEach(campDef -> {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("customerId", campDef.getCustgroupId());
                map.put("activityId", campDef.getCampsegId());
                map.put("channelId", campDef.getChannelId());
                map.put("channelAdivId", campDef.getChannelAdivId());
                map.put("planId", campDef.getPlanId());
                map.put("eventId", campDef.getCepEventId());
                map.put("startTime", DateUtil.format(campDef.getStartDate(),"yyyyMMddHHmmss"));
                map.put("endTime", DateUtil.format(campDef.getEndDate(),"yyyyMMddHHmmss"));
                log.info("江西IOP活动信息同步到标签侧发送DNA请求路径url：{},返回类型：String类型,参数：{}", approveServiceUrl, JSONUtil.parseObj(map, false).toString());
                // 3. 发送请求
                String resStr = RestFullUtil.getInstance().sendPost2(approveServiceUrl, JSONUtil.parseObj(map, false).toString());
                log.debug("江西IOP活动信息同步到标签侧DNA响应数据：{}", resStr);
                // 4. 解析响应
                DnaResponseDataVO resData = JSONUtil.toBean(resStr, DnaResponseDataVO.class);
                log.info("江西IOP活动信息同步到标签侧DNA响应封装后数据：{}", resData);
            } catch (JSONException ej) {
                log.error("江西IOP活动信息同步到标签侧JSON转化异常！", ej);
            } catch (RestClientException er) {
                log.error("江西IOP活动信息同步到标签侧请求调用异常！", er);
            } catch (Exception e) {
                log.error("江西IOP活动信息同步到标签侧调用失败", e);
            }
        });
    }
}
