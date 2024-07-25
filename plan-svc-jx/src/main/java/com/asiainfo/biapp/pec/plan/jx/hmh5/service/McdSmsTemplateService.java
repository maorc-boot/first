package com.asiainfo.biapp.pec.plan.jx.hmh5.service;

import com.asiainfo.biapp.client.pec.approve.model.SubmApproveQuery;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCareSmsTemplate;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.*;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupAttrList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * description: 江西客户通关怀短信模板service
 *
 * @author: lvchaochao
 * @date: 2023/3/14
 */
public interface McdSmsTemplateService extends IService<McdCareSmsTemplate> {

    /**
     * 查询关怀短信模板列表数据
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage}<{@link McdCareSmsTemplate}>
     */
    IPage<McdCareSmsTemplateResp> getCareSmsTemplate(Page<McdCareSmsTemplateResp> page, McdCareSmsTemplateQuery query);

    /**
     * 获取营销用语变量替换信息
     *
     * @param queryVO 短信模板-获取营销用语变量替换信息入参对象
     * @return {@link List}<{@link McdCustgroupAttrList}>
     */
    List<McdCustgroupAttrList> getCustGroupVars(SmsTemplateCustVarsQueryVO queryVO);

    /**
     * 根据模板编码查询关怀短信模板详情
     *
     * @param templateCode 模板代码
     * @return {@link McdCareSmsTemplateResp}
     */
    McdCareSmsTemplateResp getCareSmsTemplateDetail(String templateCode, HttpServletRequest request);

    /**
     * 客户通关怀短信模板提交审批
     *
     * @param req  要求事情
     * @param user 用户
     */
    void subCareSmsTemplateApprove(SubmitProcessQuery req, UserSimpleInfo user);

    /**
     * 获取客户通关怀短信模板审批流程实例节点下级审批人
     *
     * @param submitProcessDTO submitProcessDTO
     * @return SubmitProcessDTO
     */
    ActionResponse<SubmitProcessJxDTO> getCareSmsTemplateApprover(SubmitProcessJxDTO submitProcessDTO);

    /**
     * 获取客户通关怀短信模板提交审批对象信息
     *
     * @param submApproveQuery submApproveQuery
     * @return SubmitProcessQuery
     */
    SubmitProcessQuery getCmpApprovalProcess(SubmApproveQuery submApproveQuery);

    /**
     * 客户通关怀短信模板审批列表
     *
     * @param req req
     * @return IPage<ApprRecord>
     */
    IPage<SmsTemplateApprRecord> approveRecord(CareSmsTemplateApproveJxQuery req);

    /**
     * 保存或修改短信模板与标签关系
     *
     * @param saveOrUpdateRelaParam 保存或修改短信模板与标签关系入参
     * @return {@link ActionResponse}
     */
    ActionResponse saveOrUpdateRela(SaveOrUpdateRelaParam saveOrUpdateRelaParam);
}
