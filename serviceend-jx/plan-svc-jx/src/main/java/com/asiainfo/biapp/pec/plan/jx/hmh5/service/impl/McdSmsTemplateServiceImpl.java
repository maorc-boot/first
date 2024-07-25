package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.approve.model.*;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient;
import com.asiainfo.biapp.pec.plan.jx.api.dto.NodesApproverJx;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.camp.enums.CampStatusJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CmpApproveProcessRecordJx;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.McdSmsTemplateDao;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCareSmsTemplate;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdSmsTemplateService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.model.McdFrontCareSmsLabelRela;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.McdFrontCareSmsLabelRelaService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.*;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupAttrList;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * description: 江西客户通关怀短信模板service实现
 *
 * @author: lvchaochao
 * @date: 2023/3/14
 */
@Service
@Slf4j
public class McdSmsTemplateServiceImpl extends ServiceImpl<McdSmsTemplateDao, McdCareSmsTemplate> implements McdSmsTemplateService {

    @Autowired
    private McdSmsTemplateDao mcdSmsTemplateDao;

    @Autowired
    private IApproveService approveService;

    // @Autowired
    // private PecApproveFeignClient approveFeignClient;

    @Autowired
    private PecApproveFeignClient approveFeignClient;

    @Autowired
    private McdFrontCareSmsLabelRelaService mcdFrontCareSmsLabelRelaService;

    /**
     * 关怀短信--MESSAGE_TEMPLATE 作战地图--BATTLE_MAP
     */
    public static final String APPROVE_TYPE = "MESSAGE_TEMPLATE";

    /**
     * 查询关怀短信模板列表数据
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage}<{@link McdCareSmsTemplate}>
     */
    @Override
    public IPage<McdCareSmsTemplateResp> getCareSmsTemplate(Page<McdCareSmsTemplateResp> page, McdCareSmsTemplateQuery query) {
       return mcdSmsTemplateDao.getCareSmsTemplate(page, query);
    }

    /**
     * 获取营销用语变量替换信息
     * 根据消息类型=代维   直接查询代维
     * 消息类型=普通时判断模板类型  1. 模板类型！=作战地图
     *                           2. 模板类型==作战地图
     *
     * @param queryVO 短信模板-获取营销用语变量替换信息入参对象
     * @return {@link List}<{@link McdCustgroupAttrList}>
     */
    @Override
    public List<McdCustgroupAttrList> getCustGroupVars(SmsTemplateCustVarsQueryVO queryVO) {
        String pCode;
        if ("daiwei".equals(queryVO.getSmsType())) {
            pCode = "CARE_SMS_TEMPLATE_VAR_DAIWEI";
        } else {
            if ("作战地图".equals(queryVO.getTemplateType())) {
                pCode = "CARE_SMS_TEMPLATE_VAR_WGT";
            } else {
                pCode = "CARE_SMS_TEMPLATE_VAR_HMH5";
            }
        }
        return mcdSmsTemplateDao.getCustGroupVars(pCode);
    }

    /**
     * 根据模板编码查询关怀短信模板详情
     *
     * @param templateCode 模板代码
     * @return {@link McdCareSmsTemplateResp}
     */
    @Override
    public McdCareSmsTemplateResp getCareSmsTemplateDetail(String templateCode, HttpServletRequest request) {
        // UserSimpleInfo user = UserUtil.getUser(request);
        // 1.查询模板信息
        return mcdSmsTemplateDao.getCareSmsTemplateDetail(templateCode);
        // try {
        //     // 2.查询审批信息
        //     // Map<String, Object> approveInfo = mcdSmsTemplateDao.getApproveInfo(templateCode);
        //     ActionResponse<CmpApproveProcessRecordJx> careSmsTemplateApproveInfo = approveFeignClient.getCareSmsTemplateApproveInfo(templateCode);
        //     log.info("getCareSmsTemplateDetail->根据模板编码查询关怀短信模板审批详情={}", JSONUtil.toJsonStr(careSmsTemplateApproveInfo));
        //     if (ResponseStatus.SUCCESS.equals(careSmsTemplateApproveInfo.getStatus())) {
        //         CmpApproveProcessRecordJx record = careSmsTemplateApproveInfo.getData();
        //         careSmsTemplateDetail.setBusinessId(record.getBusinessId());
        //         careSmsTemplateDetail.setInstanceId(record.getInstanceId());
        //         careSmsTemplateDetail.setNodeId(record.getNodeId());
        //         careSmsTemplateDetail.setNodeName(record.getNodeName());
        //         careSmsTemplateDetail.setNodeType(record.getNodeType());
        //         careSmsTemplateDetail.setNodeBusinessName(record.getNodeBusinessName());
        //         careSmsTemplateDetail.setApprover(record.getApprover());
        //         careSmsTemplateDetail.setApproverName(record.getApproverName());
        //         careSmsTemplateDetail.setDealOpinion(record.getDealOpinion());
        //         careSmsTemplateDetail.setDealStatus(record.getDealStatus());
        //         careSmsTemplateDetail.setDealTime(record.getDealTime());
        //         careSmsTemplateDetail.setPreRecordId(record.getPreRecordId());
        //         careSmsTemplateDetail.setEventId(record.getEventId());
        //         careSmsTemplateDetail.setCreateDate(record.getCreateDate());
        //         careSmsTemplateDetail.setCreateBy(record.getCreateBy());
        //         careSmsTemplateDetail.setModifyDate(record.getModifyDate());
        //         careSmsTemplateDetail.setModifyBy(record.getModifyBy());
        //         // careSmsTemplateDetail.setNodeName(Convert.toStr(approveInfo.get("NODE_NAME")));
        //         // careSmsTemplateDetail.setApproveDate(Convert.toStr(approveInfo.get("CREATE_DATE")));
        //         // careSmsTemplateDetail.setApprovalerName(Convert.toStr(approveInfo.get("APPROVER_NAME")));
        //         // careSmsTemplateDetail.setApproveResult(Convert.toStr(approveInfo.get("DEAL_STATUS")));
        //     } else {
        //         careSmsTemplateDetail.setNodeName("草稿");
        //         careSmsTemplateDetail.setDealTime(DateUtil.parse(DateUtil.format(careSmsTemplateDetail.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
        //         careSmsTemplateDetail.setApproverName(user.getUserName());
        //         careSmsTemplateDetail.setApproveResult(user.getUserName() + "创建了模板");
        //     }
        // } catch (Exception e) {
        //     log.error("根据模板编码查询关怀短信模板详情异常：",e);
        //     careSmsTemplateDetail.setNodeName("异常");
        //     careSmsTemplateDetail.setDealTime(DateUtil.parse(DateUtil.format(careSmsTemplateDetail.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
        //     careSmsTemplateDetail.setApproverName(user.getUserName());
        //     careSmsTemplateDetail.setApproveResult("审批异常，请查询相关日志！");
        // }
        // return careSmsTemplateDetail;
    }

    /**
     * 客户通关怀短信模板提交审批
     *
     * @param req  要求事情
     * @param user 用户
     */
    @Override
    public void subCareSmsTemplateApprove(SubmitProcessQuery req, UserSimpleInfo user) {
        String templateCode = req.getBusinessId();
        McdCareSmsTemplate mcdCareSmsTemplate = mcdSmsTemplateDao.selectById(templateCode);
        Assert.notNull(mcdCareSmsTemplate, "当前关怀短信模板不存在");
        // req.setApprovalType(req.getApprovalType());
        final ActionResponse<Object> submit = approveFeignClient.submit(req);
        log.info("关怀短信模板提交审批结果->{}", JSONUtil.toJsonStr(submit));
        if (ResponseStatus.SUCCESS.equals(submit.getStatus())) {
            log.info("【客户通关怀短信模板】submit approval success, flowId->{}", submit.getData());
            if (StringUtils.isBlank(mcdCareSmsTemplate.getApproveFlowId()) || CampStatusJx.DRAFT.getCode().equals(mcdCareSmsTemplate.getApprovalStatus().toString())) {
                final Long id = (Long) submit.getData();
                mcdCareSmsTemplate.setApproveFlowId(Long.toString(id));
                mcdCareSmsTemplate.setApprovalStatus(CampStatusJx.APPROVING.getId());
                mcdSmsTemplateDao.updateById(mcdCareSmsTemplate);
            }
        } else {
            throw new BaseException("【客户通关怀短信模板】提交审批失败");
        }
    }

    /**
     * 根据模板id从oracle库查询关怀短信模板信息
     *
     * @param templateCode 模板id
     * @return {@link McdCareSmsTemplate}
     */
    @DataSource("khtmanageusedb")
    private McdCareSmsTemplate qryCareSmsTemplateById (String templateCode) {
        return mcdSmsTemplateDao.selectById(templateCode);
    }

    /**
     * 提交审批后，修改oracle库关怀短信模板审批状态为审批中
     *
     * @param mcdCareSmsTemplate 关怀短信模板对象
     * @param id 模板id
     */
    @DataSource("khtmanageusedb")
    private void updateCareSmsTemplateById (McdCareSmsTemplate mcdCareSmsTemplate, Long id) {
        mcdCareSmsTemplate.setApproveFlowId(Long.toString(id));
        mcdCareSmsTemplate.setApprovalStatus(CampStatusJx.APPROVING.getId());
        mcdSmsTemplateDao.updateById(mcdCareSmsTemplate);
    }

    /**
     * 获取客户通关怀短信模板审批流程实例节点下级审批人
     *
     * @param submitProcessDTO submitProcessDTO
     * @return SubmitProcessDTO
     */
    @Override
    public ActionResponse<SubmitProcessJxDTO> getCareSmsTemplateApprover(SubmitProcessJxDTO submitProcessDTO) {
        if (submitProcessDTO.getProcessId() == null){
            final ActionResponse<CmpApprovalProcess> careSmsTemplate = approveFeignClient.getApproveConfig(submitProcessDTO.getApprovalType());
            log.info("【客户通关怀短信模板】getApproveConfig返回 ====> {}", JSONUtil.toJsonStr(careSmsTemplate));
            // feign调用失败的情况
            if (ResponseStatus.SUCCESS.getCode() != careSmsTemplate.getStatus().getCode())
                throw new BaseException(careSmsTemplate.getMessage());
            // 首次获取审批流程节点的必要参数
            JSONObject triggerParm = new JSONObject();
            triggerParm.putOpt("channelId", "");  //不设置此值无法查询到结果
            submitProcessDTO.setProcessId(careSmsTemplate.getData().getProcessId());
            submitProcessDTO.setBerv(careSmsTemplate.getData().getBerv());
            submitProcessDTO.setTriggerParm(triggerParm);
        }
        // submitProcessDTO.setApprovalType(submitProcessDTO.getApprovalType());
        log.info("【客户通关怀短信模板】getNodeApprover入参 ====> {}", JSONUtil.toJsonStr(submitProcessDTO));
        final ActionResponse<SubmitProcessJxDTO> submitprocessdto = approveFeignClient.getNodeApprover(submitProcessDTO);
        log.info("【客户通关怀短信模板】SubmitProcessDTO -【{}】", submitprocessdto);
        // SubmitProcessJxDTO processDTO = new SubmitProcessJxDTO();
        // BeanUtils.copyProperties(submitprocessdto.getData(),processDTO);
        return submitprocessdto;
    }

    /**
     * 获取客户通关怀短信模板提交审批对象信息
     *
     * @param submApproveQuery submApproveQuery
     * @return SubmitProcessQuery
     */
    @Override
    public SubmitProcessQuery getCmpApprovalProcess(SubmApproveQuery submApproveQuery) {
        final ActionResponse<CmpApprovalProcess> careSmsTemplate = approveFeignClient.getApproveConfig(APPROVE_TYPE);
        log.info("CmpApprovalProces对象 careSmsTemplate ====> {}", JSONUtil.toJsonStr(careSmsTemplate));
        SubmitProcessJxDTO submitProcessDTO = new SubmitProcessJxDTO();
        submitProcessDTO.setProcessId(careSmsTemplate.getData().getProcessId());
        submitProcessDTO.setBerv(careSmsTemplate.getData().getBerv());
        submitProcessDTO.setTriggerParm(new JSONObject());
        log.info("submitProcessDTO 入参 ====> {}",JSONUtil.toJsonStr(submitProcessDTO));
        //get node approver and nextnode approver
        final ActionResponse<SubmitProcessJxDTO> nodeApprover = approveFeignClient.getNodeApprover(submitProcessDTO);
        log.info("NextApproverDTO对象,nodeApprover ====> {}",JSONUtil.toJsonStr(nodeApprover));
        //get node info and approver info
        SubmitProcessQuery submitProcessQuery = new SubmitProcessQuery();
        submitProcessQuery.setBerv(careSmsTemplate.getData().getBerv());
        submitProcessQuery.setNodeId(nodeApprover.getData().getNodeId());
        submitProcessQuery.setProcessId(careSmsTemplate.getData().getProcessId());
        submitProcessQuery.setSubmitStatus(1);
        List<NodesApproverQuery> nextNodesApprover = new ArrayList<>();
        final List<NodesApproverJx> nodesApprover = nodeApprover.getData().getNextNodesApprover();
        for (NodesApproverJx approver : nodesApprover) {
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
     * 客户通关怀短信模板审批列表
     *
     * @param req req
     * @return IPage<ApprRecord>
     */
    @Override
    public IPage<SmsTemplateApprRecord> approveRecord(CareSmsTemplateApproveJxQuery req) {
        RecordPageDTO dto = new RecordPageDTO();
        dto.setCurrent(req.getCurrent());
        dto.setSize(req.getSize());
        List<SmsTemplateApprRecord> smsTemplateApprRecordList  = mcdSmsTemplateDao.qryApprRecord(Collections.EMPTY_SET, req);
        if(CollectionUtils.isEmpty(smsTemplateApprRecordList)){
            log.info("客户通关怀短信模板审批列表为空！");
            return new Page<>();
        }
        Map<String, SmsTemplateApprRecord> smsTemplateMap = smsTemplateApprRecordList.stream().collect(Collectors.toMap(SmsTemplateApprRecord::getTemplateCode, Function.identity()));
        dto.setList(Lists.newArrayList(smsTemplateMap.keySet()));
        final ActionResponse<Page<CmpApproveProcessRecordJx>> userRecord = approveFeignClient.getUserRecordNew(dto);
        log.info("【客户通关怀短信模板】当前用户待审记录->{}", JSONUtil.toJsonStr(userRecord));
        Assert.isTrue(ResponseStatus.SUCCESS.equals(userRecord.getStatus()), userRecord.getMessage());
        final IPage<CmpApproveProcessRecordJx> data = userRecord.getData();
        final Set<String> templateCodes = data.getRecords().stream().map(CmpApproveProcessRecordJx::getBusinessId).collect(Collectors.toSet());
        smsTemplateApprRecordList = mcdSmsTemplateDao.qryApprRecord(templateCodes, req);
        smsTemplateMap = smsTemplateApprRecordList.stream().collect(Collectors.toMap(SmsTemplateApprRecord::getTemplateCode, Function.identity()));
        IPage<SmsTemplateApprRecord> apprRecordIPage = new Page<>(req.getCurrent(), req.getSize(), smsTemplateApprRecordList.size());
        List<SmsTemplateApprRecord> listRecord = new ArrayList<>();
        apprRecordIPage.setRecords(listRecord);
        for (CmpApproveProcessRecordJx record : data.getRecords()) {
            if (null != smsTemplateMap.get(record.getBusinessId())) {
                // SmsTemplateApprRecord vo = new SmsTemplateApprRecord();
                // BeanUtils.copyProperties(record, vo);
                // listRecord.add(vo);
                // listRecord.add(smsTemplateMap.get(record.getBusinessId()));
                listRecord.add(convertToAppr(record, smsTemplateMap.get(record.getBusinessId())));
            }
        }
        return apprRecordIPage;
    }

    /**
     * 保存或修改短信模板与标签关系
     *
     * @param saveOrUpdateRelaParam 保存或修改短信模板与标签关系入参
     * @return {@link ActionResponse}
     */
    @Override
    public ActionResponse saveOrUpdateRela(SaveOrUpdateRelaParam saveOrUpdateRelaParam) {
        // List<Object> oldLabels = mcdFrontCareSmsLabelRelaService.listObjs(Wrappers.<McdFrontCareSmsLabelRela>query().lambda().eq(McdFrontCareSmsLabelRela::getDataState, 1)
        //                 .eq(McdFrontCareSmsLabelRela::getSmsTemplateCode, saveOrUpdateRelaParam.getSmsTemplateCode()));
        try {
            // 查询旧的标签
            List<String> oldLabels = mcdSmsTemplateDao.selectCareSmsLabelCodeList(saveOrUpdateRelaParam.getSmsTemplateCode());
            if (CollectionUtil.isNotEmpty(oldLabels)) {
                // 将旧关系里存在新的标签要去除
                List<String> beRemoveLabels = new ArrayList<>(oldLabels);
                beRemoveLabels.removeAll(Arrays.asList(saveOrUpdateRelaParam.getLabelCodes().split(StrUtil.COMMA)));
                if (CollectionUtil.isNotEmpty(beRemoveLabels)) {
                    // mcdSmsTemplateDao.deleteCareSmsLabelRela(saveOrUpdateRelaParam.getSmsTemplateCode(), beRemoveLabels);
                    boolean remove = mcdFrontCareSmsLabelRelaService.remove(Wrappers.<McdFrontCareSmsLabelRela>update().lambda().eq(McdFrontCareSmsLabelRela::getSmsTemplateCode, saveOrUpdateRelaParam.getSmsTemplateCode())
                            .in(McdFrontCareSmsLabelRela::getLabelCode, beRemoveLabels));
                    if (remove) {
                        return ActionResponse.getSuccessResp("保存或修改短信模板与标签关系成功");
                    } else {
                        return ActionResponse.getFaildResp("保存或修改短信模板与标签关系失败");
                    }
                }
            }
            // 新增逻辑
            List<String> beAddList = new ArrayList<>(Arrays.asList(saveOrUpdateRelaParam.getLabelCodes().split(StrUtil.COMMA)));
            beAddList.removeAll(oldLabels);
            if (CollectionUtil.isNotEmpty(beAddList)) {
                List<McdFrontCareSmsLabelRela> list = new ArrayList<>();
                int i = 0;
                for (String beAddLabelCode : beAddList) {
                    McdFrontCareSmsLabelRela bean = new McdFrontCareSmsLabelRela();
                    bean.setSerialNum(DateUtil.format(new Date(), "yyyyMMddHHmmss") + Math.round(100) + (i++));
                    bean.setLabelCode(beAddLabelCode);
                    bean.setDataState(1);
                    bean.setSmsTemplateCode(saveOrUpdateRelaParam.getSmsTemplateCode());
                    bean.setUpdateTime(new Date());
                    list.add(bean);
                }
                boolean saveBatch = mcdFrontCareSmsLabelRelaService.saveBatch(list);
                if (saveBatch) {
                    return ActionResponse.getSuccessResp("保存或修改短信模板与标签关系成功");
                } else {
                    return ActionResponse.getFaildResp("保存或修改短信模板与标签关系失败");
                }
            }
        } catch (Exception e) {
            log.error("保存或修改短信模板与标签关系异常：", e);
            return ActionResponse.getFaildResp("保存或修改短信模板与标签关系失败");
        }
        return ActionResponse.getSuccessResp("保存或修改短信模板与标签关系成功");
    }

    /**
     * 信息转换
     *
     * @param record 审批信息
     * @param smsTemplateApprRecord 关怀短信模板对象信息
     * @return {@link SmsTemplateApprRecord}
     */
    public SmsTemplateApprRecord convertToAppr(CmpApproveProcessRecordJx record, SmsTemplateApprRecord smsTemplateApprRecord) {
        final SmsTemplateApprRecord apprRecord = new SmsTemplateApprRecord();
        // 模板相关信息
        apprRecord.setTemplateCode(smsTemplateApprRecord.getTemplateCode());
        apprRecord.setUserName(smsTemplateApprRecord.getUserName());
        apprRecord.setCreateTime(smsTemplateApprRecord.getCreateTime());
        apprRecord.setCreateUserId(smsTemplateApprRecord.getCreateUserId());
        apprRecord.setType(smsTemplateApprRecord.getType());
        // 审批对象相关需要的信息
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

    /**
     * 查询待审批关怀短信模板集合
     *
     * @param templateCodes 模板id集合
     * @param req req
     * @return {@link List}<{@link SmsTemplateApprRecord}>
     */
    @DataSource("khtmanageusedb")
    private List<SmsTemplateApprRecord> getSmsTemplateApprRecordList (Set<String> templateCodes, CareSmsTemplateApproveJxQuery req) {
        return mcdSmsTemplateDao.qryApprRecord(templateCodes, req);
    }

}
