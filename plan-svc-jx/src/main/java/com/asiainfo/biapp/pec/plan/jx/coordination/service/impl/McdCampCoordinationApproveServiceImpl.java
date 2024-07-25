package com.asiainfo.biapp.pec.plan.jx.coordination.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.approve.api.PecApproveFeignClient;
import com.asiainfo.biapp.client.pec.approve.model.*;
import com.asiainfo.biapp.pec.common.jx.enums.CampCoordinateStatus;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.coordination.dao.McdCampCoordinationTaskDao;
import com.asiainfo.biapp.pec.plan.jx.coordination.dao.McdStrategicCoordinationDao;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCampCoordinationTaskModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.CampCoordinationApproveQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.CoordinationSubmitProcessQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.SubmTaskApproveQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.response.ApprTaskRecord;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.IMcdCampCoordinationApproveService;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.feign.McdCoordinationApprFeignClient;
import com.asiainfo.biapp.pec.plan.service.IApproveService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * Description : 统筹任务审批服务实现类
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-6-16
 */
@Service
@Slf4j
public class McdCampCoordinationApproveServiceImpl extends ServiceImpl<McdCampCoordinationTaskDao, McdCampCoordinationTaskModel> implements IMcdCampCoordinationApproveService {

    public static final String APPROVE_TYPE = "TASK";


    @Resource
    private McdCampCoordinationTaskDao mcdCampCoordinationTaskDao;

    @Resource
    private McdStrategicCoordinationDao mcdStrategicCoordinationDao;

    @Autowired
    private PecApproveFeignClient approveFeignClient;
    
    @Resource
    private IApproveService approveService;

    @Autowired
    private McdCoordinationApprFeignClient mcdCoordinationApprFeignClient;
    
    /**
     * 统筹任务提交审批
     *
     * @param req
     * @param user
     */
    @Override
    public void submitTaskAppr(CoordinationSubmitProcessQuery req, UserSimpleInfo user) {
        // 父任务id
        String taskId = req.getBusinessId();
        McdCampCoordinationTaskModel mcdDimTask = mcdCampCoordinationTaskDao.selectById(taskId);
        Assert.notNull(mcdDimTask, "当前统筹任务不存在");
        req.setApprovalType(APPROVE_TYPE);
        // final ActionResponse<Object> submit = approveService.submit(req);
        // 提交审批并且发送代办
        final ActionResponse<Object> submit = mcdCoordinationApprFeignClient.commitCoordCommitAppr2Emis(req);
        log.info("统筹任务->{}提交审批结果->{}", mcdDimTask.getTaskId(), new JSONObject(submit));
        if (ResponseStatus.SUCCESS.equals(submit.getStatus())) {
            log.info("submitTaskAppr success, flowId->{}", submit.getData());
            if(StringUtils.isBlank(mcdDimTask.getApproveFlowId())
                       || CampCoordinateStatus.APPROVAL_PENDIING.getId() == mcdDimTask.getExecStatus()
                       || CampCoordinateStatus.APPROVING.getId() == mcdDimTask.getExecStatus()){ // 任务多次分开提交时  前面提交的已将主任务状态改为40审批中
                log.info("准备更改任务状态");
                final Long id = (Long) submit.getData();
                mcdDimTask.setApproveFlowId(Long.toString(id));
                mcdDimTask.setExecStatus(Integer.parseInt(CampCoordinateStatus.APPROVING.getCode()));
                // 更新主、子任务状态数据
                updateDataInDb(req, mcdDimTask);
            }
        } else {
            throw new BaseException("统筹任务=" + mcdDimTask.getTaskId() + "提交审批失败");
        }
    }

    /**
     * 更新主、子任务状态数据
     *
     * @param req        req
     * @param mcdDimTask mcdDimTask
     */
    private void updateDataInDb(CoordinationSubmitProcessQuery req, McdCampCoordinationTaskModel mcdDimTask) {
        // 更新主任务状态
        int updateById = mcdCampCoordinationTaskDao.updateById(mcdDimTask);
        log.info("updateDataInDb-->更新主任务={}，结果={}", JSONUtil.toJsonStr(mcdDimTask), updateById);
        List<McdCampCoordinationTaskModel> childTasks = mcdCampCoordinationTaskDao.selectList(Wrappers.<McdCampCoordinationTaskModel>lambdaQuery()
                .eq(McdCampCoordinationTaskModel::getTaskPId, req.getBusinessId())
                .in(McdCampCoordinationTaskModel::getTaskId, Arrays.asList(req.getChildBusinessId().split(StrUtil.COMMA))));
        childTasks.forEach(childTask -> {
            childTask.setExecStatus(Integer.parseInt(CampCoordinateStatus.APPROVING.getCode()));
        });
        // 批量更新子任务
        boolean updateBatchById = this.updateBatchById(childTasks);
        log.info("updateDataInDb-->更新子任务={}，结果={}", JSONUtil.toJsonStr(childTasks), updateBatchById);
    }


    @Override
    public SubmitProcessDTO getTaskApprover(SubmitProcessDTO submitProcessDTO) {
        if (submitProcessDTO.getProcessId() == null){
            final ActionResponse<CmpApprovalProcess> Task = approveFeignClient.getApproveConfig(APPROVE_TYPE);
            log.info("CmpApprovalProces对象 Task ====> {}",new JSONObject(Task).toString());
            submitProcessDTO.setProcessId(Task.getData().getProcessId());
            submitProcessDTO.setBerv(Task.getData().getBerv());
            submitProcessDTO.setTriggerParm(new JSONObject());
        }
        submitProcessDTO.setApprovalType(APPROVE_TYPE);
        log.info("submitProcessDTO 入参 ====> {}",new JSONObject(submitProcessDTO).toString());
        final ActionResponse<SubmitProcessDTO> submitprocessdto = approveFeignClient.getNodeApprover(submitProcessDTO);
        SubmitProcessDTO processDTO = new SubmitProcessDTO();
        BeanUtils.copyProperties(submitprocessdto.getData(),processDTO);
        log.info("SubmitProcessDTO -【{}】",processDTO);
        return processDTO;
    }
    
    /**
     * 获取统筹任务提交审批入参
     *
     * @param submApproveQuery
     * @return
     */
    @Override
    public SubmitProcessQuery getCmpApprovalProcess(SubmTaskApproveQuery submApproveQuery) {
        final ActionResponse<CmpApprovalProcess> Task = approveFeignClient.getApproveConfig(APPROVE_TYPE);
        log.info("CmpApprovalProces对象 Task ====> {}",new JSONObject(Task).toString());
   
        SubmitProcessDTO submitProcessDTO = new SubmitProcessDTO();
        submitProcessDTO.setProcessId(Task.getData().getProcessId());
        submitProcessDTO.setBerv(Task.getData().getBerv());
        submitProcessDTO.setTriggerParm(new JSONObject());
        log.info("submitProcessDTO 入参 ====> {}",new JSONObject(submitProcessDTO).toString());
    
        //get node approver and nextnode approver
        final ActionResponse<SubmitProcessDTO> nodeApprover = approveFeignClient.getNodeApprover(submitProcessDTO);
        log.info("NextApproverDTO对象,nodeApprover ====> {}",new JSONObject(nodeApprover).toString());

        //get node info and approver info
        SubmitProcessQuery submitProcessQuery = new SubmitProcessQuery();
        
        submitProcessQuery.setBerv(Task.getData().getBerv());
        submitProcessQuery.setNodeId(nodeApprover.getData().getNodeId());
        submitProcessQuery.setProcessId(Task.getData().getProcessId());
        submitProcessQuery.setSubmitStatus(1);

        List<NodesApproverQuery> nextNodesApprover = new ArrayList<>();

        final List<NodesApprover> nodesApprover = nodeApprover.getData().getNextNodesApprover();

        for (NodesApprover approver : nodesApprover) {
            NodesApproverQuery nodesApproverQuery = new NodesApproverQuery();
            final CmpApproveProcessNode node = approver.getNode();
            CmpApproveProcessNodeQuery nodeQuery= new CmpApproveProcessNodeQuery();
            BeanUtils.copyProperties(node,nodeQuery);
            nodesApproverQuery.setNode(nodeQuery);
            nodesApproverQuery.setProcessVersionNum(approver.getProcessVersionNum());
            nodesApproverQuery.setApproverUser(submApproveQuery.getApprover());
            nextNodesApprover.add(nodesApproverQuery);
        }
        submitProcessQuery.setNextNodesApprover(nextNodesApprover);

        submitProcessQuery.setBusinessId(submApproveQuery.getTaskId());
        return submitProcessQuery;
    }


    /**
     * 统筹任务审批列表
     *
     * @param req
     * @return
     */
    @Override
    public IPage<ApprTaskRecord> approveTaskRecord(CampCoordinationApproveQuery req) {
        RecordPageDTO dto = new RecordPageDTO();
        dto.setCurrent(req.getCurrent());
        dto.setSize(req.getSize());
        List<ApprTaskRecord> mcdTasks  = mcdStrategicCoordinationDao.qryApprTaskRecord(Collections.EMPTY_SET, req);
        if(CollectionUtils.isEmpty(mcdTasks)){
            return new Page<>();
        }
        Map<String, ApprTaskRecord> TaskMap  = mcdTasks.stream().collect(Collectors.toMap(ApprTaskRecord::getTaskId, Function.identity()));
        dto.setList(Lists.newArrayList(TaskMap.keySet()));
        
        final ActionResponse<Page<CmpApproveProcessRecord>> userRecord = approveFeignClient.getUserRecord(dto);
        log.info("当前用户统筹任务待审记录->{}", new JSONObject(userRecord));
        Assert.isTrue(ResponseStatus.SUCCESS.equals(userRecord.getStatus()), userRecord.getMessage());
        final IPage<CmpApproveProcessRecord> data = userRecord.getData();
        final Set<String> TaskIds = data.getRecords().stream()
                .map(CmpApproveProcessRecord::getBusinessId)
                .collect(Collectors.toSet());
        
        mcdTasks = mcdStrategicCoordinationDao.qryApprTaskRecord(TaskIds, req);
        TaskMap = mcdTasks.stream().collect(Collectors.toMap(ApprTaskRecord::getTaskId, Function.identity()));
        IPage<ApprTaskRecord> apprRecordIPage = new Page<>(req.getCurrent(), req.getSize(), mcdTasks.size());
        List<ApprTaskRecord> listRecord = new ArrayList<>();
        apprRecordIPage.setRecords(listRecord);
        for (CmpApproveProcessRecord record : data.getRecords()) {
            if (null != TaskMap.get(record.getBusinessId())) {
                listRecord.add(convertToAppr(record, TaskMap.get(record.getBusinessId())));
            }
        }
        return apprRecordIPage;
    }

    /**
     * 统筹子任务待审批列表查询
     *
     * @param taskIds      父任务id
     * @param qryApproved qry批准
     * @return {@link List}<{@link ApprTaskRecord}>
     */
    @Override
    public List<ApprTaskRecord> approveChildTaskRecord(List<String> taskIds, boolean qryApproved, String status) {
        return mcdStrategicCoordinationDao.approveChildTaskRecord(taskIds, qryApproved, status);
    }

    /**
     *  信息转换
     *
     * @param record
     * @param Task
     * @return
     */
    public ApprTaskRecord convertToAppr(CmpApproveProcessRecord record, ApprTaskRecord Task) {
        final ApprTaskRecord apprRecord = new ApprTaskRecord();
        apprRecord.setTaskId(Task.getTaskId());
        apprRecord.setTaskName(Task.getTaskName());
        apprRecord.setCreateUserId(Task.getCreateUserId());
        apprRecord.setCreateUserName(Task.getCreateUserName());
        apprRecord.setCreateTime(Task.getCreateTime());
        apprRecord.setExecStatus(Task.getExecStatus());
        apprRecord.setCustomTotalNum(Task.getCustomTotalNum());
        apprRecord.setPlanTotalNum(Task.getPlanTotalNum());
        apprRecord.setId(record.getId());
        apprRecord.setBusinessId(record.getBusinessId());
        apprRecord.setInstanceId(record.getInstanceId());
        apprRecord.setNodeId(record.getNodeId());
        apprRecord.setNodeName(record.getNodeName());
        apprRecord.setNodeType(record.getNodeType());
        apprRecord.setNodeBusinessName(record.getNodeBusinessName());
        apprRecord.setApprover(record.getApprover());
        apprRecord.setApproverName(record.getApproverName());
        apprRecord.setDealOpinion(record.getDealOpinion());
        apprRecord.setDealStatus(record.getDealStatus());
        apprRecord.setDealTime(record.getDealTime());
        apprRecord.setPreRecordId(record.getPreRecordId());
        apprRecord.setEventId(record.getEventId());
        apprRecord.setCreateDate(record.getCreateDate());
        apprRecord.setCreateBy(record.getCreateBy());
        apprRecord.setModifyDate(record.getModifyDate());
        apprRecord.setModifyBy(record.getModifyBy());
        return apprRecord;
    }
    
}
