package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.sms.api.PecSmsFeignClient;
import com.asiainfo.biapp.pec.approve.Enum.DealStatus;
import com.asiainfo.biapp.pec.approve.jx.Enum.ConstApprove;
import com.asiainfo.biapp.pec.approve.jx.dao.McdCampDefDao;
import com.asiainfo.biapp.pec.approve.jx.dao.SysUserDao;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.approve.jx.model.*;
import com.asiainfo.biapp.pec.approve.jx.service.IApprovePostProcessor;
import com.asiainfo.biapp.pec.approve.jx.service.McdEmisReadTaskService;
import com.asiainfo.biapp.pec.approve.jx.service.McdEmisTaskService;
import com.asiainfo.biapp.pec.approve.jx.service.feign.CustomAlarmFeignClient;
import com.asiainfo.biapp.pec.approve.jx.utils.EmisUtils;
import com.asiainfo.biapp.pec.approve.jx.vo.McdChannelRoleListVo;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessInstance;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessNode;
import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import com.asiainfo.biapp.pec.approve.model.User;
import com.asiainfo.biapp.pec.approve.service.ICmpApproveProcessNodeService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.McdSmsKafka;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2022/9/19
 */
@Slf4j
@Service
public class DefaultApprovePostProcessor implements IApprovePostProcessor {


    private static final String CITY_MANAGER_ID = "133";
    @Autowired
    private McdEmisTaskService mcdEmisTaskService;
    @Resource
    private McdEmisReadTaskService mcdEmisReadTaskService;

    @Autowired
    private EmisUtils emisUtils;

    @Autowired
    private PecSmsFeignClient pecSmsFeignClient;

    @Resource
    private McdCampDefDao campDefDao;

    @Resource
    private SysUserDao sysUserDao;
    @Autowired
    private ICmpApproveProcessNodeService cmpApproveProcessNodeService;

    @Autowired
    private CustomAlarmFeignClient customAlarmFeignClient;

    @Value("${spring.profiles.active}")
    private String profiles;

    /**
     * 审批通过后处理
     *
     * @param submitProcessDTO
     * @param thisRecord
     * @param nextRecordList
     */
    @Override
    public void approvedProcess(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance processInstance, CmpApproveProcessNode thisNode, CmpApproveProcessRecord thisRecord, List<CmpApproveProcessNode> nextNodeList, LinkedList<CmpApproveProcessRecord> nextRecordList) {
        if (!checkApproveType(submitProcessDTO)) {
            return;
        }
        // 1. 更新当前节点的待办状态为已办

        McdEmisTask task = buildTask(processInstance, thisRecord);
        UpdateWrapper<McdEmisTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("instance_id", task.getInstanceId());
        updateWrapper.eq("node_id", task.getNodeId());
        updateWrapper.eq("approval_times", task.getApprovalTimes());
        updateWrapper.eq("result", 2);

        ApprovalResult approvalResult = buildApprovalResult(processInstance, thisRecord);
        // 代办转已办
        boolean res = EmisUtils.updateMessage(approvalResult);
        if (res) {
            // Emis代办转已办接口调用成功，更新状态
            mcdEmisTaskService.saveOrUpdate(task, updateWrapper);
        }

        // 2. 下级审批人添加待办
        nextRecordList.stream().forEach(record -> {
            approvalPendingProcess(submitProcessDTO, processInstance, record);
        });

        log.info("江西根据配置条件添加阅知代办开始");
        LambdaQueryWrapper<CmpApproveProcessNode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CmpApproveProcessNode::getProcessId,processInstance.getProcessId())
                .eq(CmpApproveProcessNode::getNodeId,thisRecord.getNodeId())
                .eq(CmpApproveProcessNode::getProcessVersionNum,processInstance.getProcessVersionNum())
                .last(" limit 1 ");
        CmpApproveProcessNode cmpApproveProcessNode = cmpApproveProcessNodeService.getOne(queryWrapper);
        boolean currentNodeIsExistFlag = false;
        String finalCampsegName ="";
        if (cmpApproveProcessNode != null && cmpApproveProcessNode.getApproverType() ==2 ){
            //  调用 Emis 添加阅知待办接口
            List<McdChannelRoleListVo> channelRoleListVos = sysUserDao.queryChannelRoleByCampsegId(submitProcessDTO.getBusinessId());

            for (McdChannelRoleListVo channelRoleListVo : channelRoleListVos) {
                if (cmpApproveProcessNode.getApprover().equals(channelRoleListVo.getRoleId()+"")){
                    currentNodeIsExistFlag = true;
                    finalCampsegName = channelRoleListVo.getCampsegName();
                    approvalResult.setCampsegId(channelRoleListVo.getCampsegId());
                    break;
                }
            }
        }

        //  阅知待办
        if (currentNodeIsExistFlag) {
            log.info("江西添加阅知代办开始,"+ finalCampsegName);
            //获取配置的角色ID
            String roleId = RedisUtils.getDicValue("EMIS_SEND_READ_SMS_ROLE") == null ? CITY_MANAGER_ID:RedisUtils.getDicValue("EMIS_SEND_READ_SMS_ROLE");
            List<SysUser> userList = sysUserDao.queryUserByRoleId(roleId);

            String msg = String.format("您好：IOP系统有您的一条待阅知任务【%s】，请及时阅知，谢谢！", finalCampsegName);
            for (SysUser u : userList) {
                User user = getUserInfo(u);
                // 发送阅知待办
                boolean readFlag = emisUtils.addReadMessage(approvalResult, finalCampsegName, user);

                if (readFlag){
                    String unid = approvalResult.getInstanceId() +"_"+ user.getUserId()+"_"+approvalResult.getServiceType()+"_"+approvalResult.getNodeId();
                    //记录阅知信息
                    McdEmisReadTask emisReadTask = getMcdEmisReadTask(processInstance, thisRecord,getUserInfo(u),finalCampsegName, null,unid);
                    // Emis阅知代办转
                    mcdEmisReadTaskService.save(emisReadTask);
                }

                if (StrUtil.isEmpty(u.getMobilePhone())) {
                    log.warn("阅知代办用户号码为空,userId:{}", u.getUserId());
                    continue;
                }

                McdSmsKafka smsKafka = new McdSmsKafka();
                smsKafka.setMessage(msg);
                smsKafka.setProductNo(u.getMobilePhone());
                // 发送提醒短信
                pecSmsFeignClient.sendWarnMessage(JSONUtil.toJsonStr(smsKafka));

            }
        }

    }

    /**
     * 驳回后处理增加发送emis阅知待办
     *
     * @param submitProcessDTO 提交流程信息
     * @param processInstance  流程实例信息
     */
    @Override
    public void rejectProcessAndSendEmis(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance processInstance, CmpApproveProcessRecord thisRecord) throws Exception{
        log.info("江西驳回后处理增加发送emis阅知待办开始,businessId={}", submitProcessDTO.getBusinessId());
        ApprovalResult approvalResult = buildApprovalResult(processInstance, thisRecord);
        // 1.获取活动或预警的创建人
        // String type;
        int businessType;
        Map<String, Object> resMap = null;
        User user = new User();
        if (CustomAlarmFeignClient.CUSTOM_ALARM.equals(submitProcessDTO.getApprovalType())) {
            // type = "alarm";
            businessType = 2; // 预警
            // 2.根据预警id查询预警名称以及创建人信息
            McdIdQuery query = new McdIdQuery();
            query.setId(submitProcessDTO.getBusinessId());
            ActionResponse<Map<String, Object>> mapActionResponse = customAlarmFeignClient.queryAlarmNameAndCreator(query);
            if (mapActionResponse.getStatus().getCode() == ResponseStatus.SUCCESS.getCode()){
                resMap = mapActionResponse.getData();
                // 3.组装创建人信息
                user.setUserId(resMap.get("USERID").toString());
                user.setUserName(resMap.get("USERNAME").toString());
                user.setDepartmentId(resMap.get("DEPARTMENTID").toString());
                user.setCityId(resMap.get("CITYID").toString());
            }
        } else {
            // type = "khtchannel";
            businessType = 3; // 客户通渠道
            // 2.根据活动根id查询活动名称以及创建人信息
            resMap = sysUserDao.getUserAndCampInfoCampsegId(submitProcessDTO.getBusinessId());
            // 3.组装创建人信息
            user.setUserId(resMap.get("USER_ID").toString());
            user.setUserName(resMap.get("USER_NAME").toString());
            user.setDepartmentId(resMap.get("DEPARTMENT_ID").toString());
            user.setCityId(resMap.get("CITY_ID").toString());
        }
        // Map<String, Object> resMap = sysUserDao.getUserAndCampInfoCampsegId(submitProcessDTO.getBusinessId(), type);
        log.info("江西驳回操作添加阅知代办resMap={}", JSONUtil.toJsonStr(resMap));
        // 4.给创建人发送阅知待办
        boolean readFlag = emisUtils.addRejectReadMessage(approvalResult, resMap.get("NAME").toString(), user);
        log.info("江西驳回后处理增加发送emis阅知待办返回readFlag={}", readFlag);
        if (readFlag) {
            String unid = approvalResult.getInstanceId() +"_"+ user.getUserId()+"_"+approvalResult.getServiceType()+"_"+approvalResult.getNodeId();
            // 5.记录阅知信息
            McdEmisReadTask emisReadTask = getMcdEmisReadTask(processInstance, thisRecord, user, resMap.get("NAME").toString(), businessType,unid);
            mcdEmisReadTaskService.save(emisReadTask);
            log.info("江西驳回后记录阅知信息保存成功emisReadTask={}", JSONUtil.toJsonStr(emisReadTask));
        }
    }

    private User getUserInfo(SysUser u){

        User user = new User();
        user.setId(u.getId());
        user.setUserId(u.getUserId());
        user.setUserName(u.getUserName());
        user.setCityId(u.getCityId());
        user.setDepartmentId(u.getDepartmentId());
        return user;
    }


    /**
     * 驳回后处理
     *
     * @param submitProcessDTO
     * @param processInstance
     * @param record
     */
    @Override
    public void rejectProcess(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance processInstance, CmpApproveProcessRecord record) {
        if (!checkApproveType(submitProcessDTO)) {
            return;
        }
        boolean resResult;
        ApprovalResult approvalResult = null;
        if (StrUtil.equals(profiles, "dev")) {
            resResult = true;
        } else {
            approvalResult = buildApprovalResult(processInstance, record);
            resResult = EmisUtils.deleteMessage(approvalResult);
        }
        if (resResult) {
            McdEmisTask task = buildTask(processInstance, record);
            UpdateWrapper<McdEmisTask> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("instance_id", task.getInstanceId());
            updateWrapper.eq("node_id", task.getNodeId());
            updateWrapper.eq("approval_times", task.getApprovalTimes());
            // Emis已办转待办接口调用成功，更新状态
            mcdEmisTaskService.saveOrUpdate(task, updateWrapper);
        }else {
            log.error("删除emis待办失败:{}", approvalResult);
        }
    }

    /**
     * 待审批后处理
     *
     * @param processInstance
     * @param record
     */
    @Override
    public void approvalPendingProcess(SubmitProcessJxDTO submitProcessDTO, CmpApproveProcessInstance processInstance, CmpApproveProcessRecord record) {
        if (!checkApproveType(submitProcessDTO)) {
            return;
        }

        if (!record.getDealStatus().equals(Integer.valueOf(DealStatus.PENDING))) {
            log.warn("不是待审批状态不处理");
            return;
        }
        McdEmisTask emisTask = buildTask(processInstance, record);

        ApprovalResult result = buildApprovalResult(processInstance, record);

        final LambdaQueryWrapper<McdCampDef> qry = Wrappers.lambdaQuery();
        qry.eq(McdCampDef::getCampsegRootId, record.getBusinessId());
        List<McdCampDef> campDefList = campDefDao.selectList(qry);
        String campsegName = record.getBusinessId();
        if (CollectionUtils.isNotEmpty(campDefList)){
            campsegName = campDefList.get(0).getCampsegName();
            result.setCampsegId(campDefList.get(0).getCampsegId());
        }
        boolean emisResult;
        if (StrUtil.equals(profiles, "dev")) {
            emisResult = true;
        } else {
            emisResult = emisUtils.addMessage(result, campsegName, submitProcessDTO.getUser().getDepartmentId(), submitProcessDTO.getUser().getCityId());
        }
        if (emisResult) {
            // 调用Emis添加待办接口成功后，保存数据到 McdEmisTask表
            log.info(" mcdemistTask "+JSONUtil.parseObj(emisTask).toString());
            mcdEmisTaskService.saveOrUpdate(emisTask);
        }
    }

    /**
     * 构建 McdEmisTask
     *
     * @param processInstance
     * @param record
     * @return
     */
    private McdEmisTask buildTask(CmpApproveProcessInstance processInstance, CmpApproveProcessRecord record) {

        McdEmisTask task = new McdEmisTask();
        task.setInstanceId(record.getInstanceId().toString());
        task.setNodeId(record.getNodeId());
        // 审批次数,审批版本号
        task.setApprovalTimes(processInstance.getProcessVersionNum());
        task.setCampsegId(record.getBusinessId());
        task.setNodeName(record.getNodeName());
        task.setApprovalUser(record.getApprover());
        task.setSubmitUser(record.getCreateBy());
        // 审批
        task.setTaskType(0);
        task.setAdvice(record.getDealOpinion());
        task.setResult(convertStatus(record.getDealStatus()));
        return task;
    }


    /**
     * 生成阅知信息
     *
     * @param processInstance 流程实例信息
     * @param record 审批节点信息
     * @param user 用户信息
     * @param name 策略名称
     * @param businessType 1-策略 2-自定义预警 3-客户通渠道活动
     * @return {@link McdEmisReadTask}
     */
    private McdEmisReadTask getMcdEmisReadTask(CmpApproveProcessInstance processInstance, CmpApproveProcessRecord record, User user,String name, Integer businessType,String unid) {
        McdEmisReadTask mcdEmisTask = new McdEmisReadTask();
        // mcdEmisTask.setId(processInstance.getInstanceId()+"_"+processInstance.getBusinessId());
        mcdEmisTask.setId(unid);
        mcdEmisTask.setName(name);
        mcdEmisTask.setNodeId(record.getNodeId());
        mcdEmisTask.setReadUser(user.getUserId()); // 客户通渠道/自定义预警驳回  此字段存储的是创建人信息
        mcdEmisTask.setSubmitUser(record.getCreateBy());
        //待阅知
        mcdEmisTask.setStatus(0);
        mcdEmisTask.setAdvice("阅知");
        mcdEmisTask.setBusinessId(processInstance.getBusinessId());
        if (null == businessType) {
            mcdEmisTask.setBusinessType(1);
        } else {
            mcdEmisTask.setBusinessType(businessType);
            mcdEmisTask.setSubmitUser(record.getApprover()); // 客户通渠道/自定义预警驳回 此字段存储的是当前节点审批人
            if (2 == businessType) {
                mcdEmisTask.setAdvice("自定义预警阅知");
            } else {
                mcdEmisTask.setAdvice("客户通渠道活动阅知");
            }
        }
        mcdEmisTask.setCreateTime(new Date());

        return mcdEmisTask;

    }

    /**
     * 构建 ApprovalResult
     *
     * @param processInstance
     * @param record
     * @return
     */
    private ApprovalResult buildApprovalResult(CmpApproveProcessInstance processInstance, CmpApproveProcessRecord record) {
        ApprovalResult result = new ApprovalResult();
        result.setInstanceId(record.getInstanceId().toString());
        result.setInstanceName(record.getNodeBusinessName());
        // todo审批状态需要转换
        result.setResult(convertStatus(record.getDealStatus()));
        result.setNodeId(record.getNodeId());
        result.setNodeName(record.getNodeName());
        result.setApprovalUserId(record.getApprover());
        result.setCreateUserId(record.getCreateBy());
        result.setServiceType(record.getBusinessId());
        result.setAdvice(record.getDealOpinion());
        result.setApprovalTimes(processInstance.getProcessVersionNum());
        result.setApprovalUserName(record.getApproverName());
        return result;
    }

    /**
     * 校验是否为营销活动审批
     *
     * @param submitProcessDTO
     * @return
     */
    private boolean checkApproveType(SubmitProcessJxDTO submitProcessDTO) {
        if (StringUtils.isEmpty(submitProcessDTO.getApprovalType())) {
            log.warn("审批类型(系统)为空,设为默认值IMCD(营销策略)");
            submitProcessDTO.setApprovalType(ConstApprove.APPROVE_TYPE_CAMP);
        }
        if (!ConstApprove.APPROVE_TYPE_CAMP.equalsIgnoreCase(submitProcessDTO.getApprovalType()) && !ConstApprove.TASK.equalsIgnoreCase(submitProcessDTO.getApprovalType())) {
            log.info("非活动、策略统筹任务审批,不需要对接Emis");
            return false;
        }
        return true;
    }

    /**
     * 7.0和4.0之间审批状态转换
     * 4.0: 0-待审批, 1-审批通过, 2-驳回
     * 7.0： 0- 驳回, 1-审批通过, 2-待审批
     *
     * @return
     */
    private int convertStatus(int status) {
        if (status == 0) {
            return 2;
        }
        if (status == 2) {
            return 0;
        }
        return status;
    }

}
