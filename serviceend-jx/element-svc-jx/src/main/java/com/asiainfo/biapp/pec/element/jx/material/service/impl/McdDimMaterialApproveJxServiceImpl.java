package com.asiainfo.biapp.pec.element.jx.material.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.client.pec.approve.api.PecApproveFeignClient;
import com.asiainfo.biapp.client.pec.approve.model.*;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.element.Enum.MaterialStatus;
import com.asiainfo.biapp.pec.element.jx.material.dao.McdDimMaterialJxDao;
import com.asiainfo.biapp.pec.element.jx.material.model.McdDimMaterialJxModel;
import com.asiainfo.biapp.pec.element.jx.material.request.DimMaterialApproveJxQuery;
import com.asiainfo.biapp.pec.element.jx.material.service.IMcdDimMaterialApproveJxService;
import com.asiainfo.biapp.pec.element.query.KeyWordsReq;
import com.asiainfo.biapp.pec.element.query.appr.ApprRecord;
import com.asiainfo.biapp.pec.element.query.appr.MaterialStatusQuery;
import com.asiainfo.biapp.pec.element.service.IApproveService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * Description : 素材审批服务实现类
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-6
 * //@modified By :
 * //@since :
 */
@Service
@Slf4j
public class McdDimMaterialApproveJxServiceImpl implements IMcdDimMaterialApproveJxService {

    public static final String APPROVE_TYPE = "MATERIAL";


    @Resource
    private McdDimMaterialJxDao mcdDimMaterialJxDao;
    
    @Autowired
    private PecApproveFeignClient approveFeignClient;
    
    @Autowired
    private IApproveService approveService;
    
    /**
     * 素材提交审批
     *
     * @param req
     * @param user
     */
    @Override
    public void submitMaterialAppr(SubmitProcessQuery req, UserSimpleInfo user) {
        String materialId = req.getBusinessId();
        final McdDimMaterialJxModel mcdDimMaterial = mcdDimMaterialJxDao.selectById(materialId);
        Assert.notNull(mcdDimMaterial, "当前素材不存在");
        req.setApprovalType(APPROVE_TYPE);
        final ActionResponse<Object> submit = approveService.submit(req);
        log.info("提交审批结果->{}", new JSONObject(submit));
        if (ResponseStatus.SUCCESS.equals(submit.getStatus())) {
            log.info("submit approval success, flowId->{}", submit.getData());
            if(StringUtils.isBlank(mcdDimMaterial.getApproveFlowId())
                       ||MaterialStatus.DRAFT.getValue().equals(mcdDimMaterial.getMaterialStatus().toString())){
                final Long id = (Long) submit.getData();
                mcdDimMaterial.setApproveFlowId(Long.toString(id));
                mcdDimMaterial.setMaterialStatus(Integer.parseInt(MaterialStatus.APPROVING.getValue()));
                mcdDimMaterialJxDao.updateById(mcdDimMaterial);
            }
        } else {
            throw new BaseException("素材提交审批失败");
        }
    }
    
    /**
     * 审批流转修改状态
     *
     * @param req
     */
    @Transactional
    @Override
    public void modifyMaterialStatusFromAppr(MaterialStatusQuery req) {
        log.info("<<<<<<<<MaterialStatusQueryJx>>>>>>>>> -【{}】",req);
        if (req.getMaterialStat() == 1){
            mcdDimMaterialJxDao.updateByFlowId(req.getFlowId(),Integer.parseInt(MaterialStatus.APPROVED.getValue()));
        } else if (req.getMaterialStat() == 2){
            mcdDimMaterialJxDao.updateByFlowId(req.getFlowId(),Integer.parseInt(MaterialStatus.DRAFT.getValue()));
        }
    }
    
    //public static final String SYSTEM_ID = "MATERIAL";
    ///**
    // * 获取审批人列表
    // *
    // * @return
    // */
    //@Override
    //public List<ApproveUserQuery> getMaterialApprover() {
    //    final ActionResponse<CmpApprovalProcess> material = approveFeignClient.getApproveConfig(SYSTEM_ID);
    //    log.info("CmpApprovalProces对象 material ====> {}",new JSONObject(material).toString());
    //    SubmitProcessDTO submitProcessDTO = new SubmitProcessDTO();
    //    submitProcessDTO.setProcessId(material.getData().getProcessId());
    //    submitProcessDTO.setBerv(material.getData().getBerv());
    //    submitProcessDTO.setTriggerParm(new JSONObject());
    //    log.info("submitProcessDTO 入参 ====> {}",new JSONObject(submitProcessDTO).toString());
    //    //get node approver and nextnode approver
    //    final ActionResponse<SubmitProcessDTO> submitprocessdto = approveFeignClient.getNodeApprover(submitProcessDTO);
    //    log.info("NextApproverDTO对象,nodeApprover ====> {}",new JSONObject(submitprocessdto).toString());
    //    final List<NodesApprover> nodesApprover = submitprocessdto.getData().getNextNodesApprover();
    //    final List<User> approverUser = nodesApprover.get(0).getApproverUser();
    //    log.info("approverUser ====> {}, nextNodesApprover_size ====> {}",approverUser,nodesApprover.size());
    //    List<ApproveUserQuery> approveUserQueryList =  new ArrayList<>();
    //    for (User user : approverUser) {
    //        ApproveUserQuery approveUserQuery = new ApproveUserQuery();
    //        BeanUtils.copyProperties(user,approveUserQuery);
    //        approveUserQueryList.add(approveUserQuery);
    //    }
    //    return approveUserQueryList;
    //}
    

    @Override
    public SubmitProcessDTO getMaterialApprover(SubmitProcessDTO submitProcessDTO) {
        if (submitProcessDTO.getProcessId() == null){
            final ActionResponse<CmpApprovalProcess> material = approveFeignClient.getApproveConfig(APPROVE_TYPE);
            log.info("CmpApprovalProces对象 material ====> {}",new JSONObject(material).toString());
            submitProcessDTO.setProcessId(material.getData().getProcessId());
            submitProcessDTO.setBerv(material.getData().getBerv());
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
     * 获取素材提交审批入参
     *
     * @param submApproveQuery
     * @return
     */
    @Override
    public SubmitProcessQuery getCmpApprovalProcess(SubmApproveQuery submApproveQuery) {
        final ActionResponse<CmpApprovalProcess> material = approveFeignClient.getApproveConfig("MATERIAL");
        log.info("CmpApprovalProces对象 material ====> {}",new JSONObject(material).toString());
   
        SubmitProcessDTO submitProcessDTO = new SubmitProcessDTO();
        submitProcessDTO.setProcessId(material.getData().getProcessId());
        submitProcessDTO.setBerv(material.getData().getBerv());
        submitProcessDTO.setTriggerParm(new JSONObject());
        log.info("submitProcessDTO 入参 ====> {}",new JSONObject(submitProcessDTO).toString());
    
        //get node approver and nextnode approver
        final ActionResponse<SubmitProcessDTO> nodeApprover = approveFeignClient.getNodeApprover(submitProcessDTO);
        log.info("NextApproverDTO对象,nodeApprover ====> {}",new JSONObject(nodeApprover).toString());

        //get node info and approver info
        SubmitProcessQuery submitProcessQuery = new SubmitProcessQuery();
        
        submitProcessQuery.setBerv(material.getData().getBerv());
        submitProcessQuery.setNodeId(nodeApprover.getData().getNodeId());
        submitProcessQuery.setProcessId(material.getData().getProcessId());
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

        submitProcessQuery.setBusinessId(submApproveQuery.getMaterialId());
        return submitProcessQuery;
    }
    
    
    /**
     * 素材审批列表
     *
     * @param req
     * @return
     */
    @Override
    public IPage<ApprRecord> approveRecord(DimMaterialApproveJxQuery req) {
        RecordPageDTO dto = new RecordPageDTO();
        dto.setCurrent(req.getCurrent());
        dto.setSize(req.getSize());
        List<ApprRecord> mcdMaterials  = mcdDimMaterialJxDao.qryApprRecord(Collections.EMPTY_SET, req);
        if(CollectionUtils.isEmpty(mcdMaterials)){
            return new Page<>();
        }
        Map<String, ApprRecord> materialMap  = mcdMaterials.stream().collect(Collectors.toMap(ApprRecord::getMaterialId, Function.identity()));
        dto.setList(Lists.newArrayList(materialMap.keySet()));
        
        final ActionResponse<Page<CmpApproveProcessRecord>> userRecord = approveFeignClient.getUserRecord(dto);
        log.info("当前用户待审记录->{}", new JSONObject(userRecord));
        Assert.isTrue(ResponseStatus.SUCCESS.equals(userRecord.getStatus()), userRecord.getMessage());
        final IPage<CmpApproveProcessRecord> data = userRecord.getData();
        final Set<String> materialIds = data.getRecords().stream()
                .map(CmpApproveProcessRecord::getBusinessId)
                .collect(Collectors.toSet());
        
        mcdMaterials = mcdDimMaterialJxDao.qryApprRecord(materialIds, req);
        materialMap = mcdMaterials.stream().collect(Collectors.toMap(ApprRecord::getMaterialId, Function.identity()));
        IPage<ApprRecord> apprRecordIPage = new Page<>(req.getCurrent(), req.getSize(), mcdMaterials.size());
        List<ApprRecord> listRecord = new ArrayList<>();
        apprRecordIPage.setRecords(listRecord);
        for (CmpApproveProcessRecord record : data.getRecords()) {
            if (null != materialMap.get(record.getBusinessId())) {
                listRecord.add(convertToAppr(record, materialMap.get(record.getBusinessId())));
            }
        }
        return apprRecordIPage;
    }
    
    /**
     *  信息转换
     *
     * @param record
     * @param material
     * @return
     */
    public ApprRecord convertToAppr(CmpApproveProcessRecord record, ApprRecord material) {
        final ApprRecord apprRecord = new ApprRecord();
        apprRecord.setMaterialId(material.getMaterialId());
        apprRecord.setMaterialName(material.getMaterialName());
        apprRecord.setCreateUserId(material.getCreateUserId());
        apprRecord.setUserName(material.getUserName());
        apprRecord.setCreateTime(material.getCreateTime());
        apprRecord.setType(material.getType());
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
