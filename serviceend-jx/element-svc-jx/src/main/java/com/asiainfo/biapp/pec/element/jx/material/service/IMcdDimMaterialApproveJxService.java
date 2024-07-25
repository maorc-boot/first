package com.asiainfo.biapp.pec.element.jx.material.service;

import com.asiainfo.biapp.client.pec.approve.model.SubmApproveQuery;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessDTO;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.element.jx.material.request.DimMaterialApproveJxQuery;
import com.asiainfo.biapp.pec.element.query.KeyWordsReq;
import com.asiainfo.biapp.pec.element.query.appr.ApprRecord;
import com.asiainfo.biapp.pec.element.query.appr.MaterialStatusQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * Description : 江西素材审批服务
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-6
 * //@modified By :
 * //@since :
 */

public interface IMcdDimMaterialApproveJxService {
    
    /**
     * 素材提交审批
     *
     * @param req
     * @param user
     */
    void submitMaterialAppr(SubmitProcessQuery req, UserSimpleInfo user);
    
    /**
     *审批流转修改状态
     *
     * @param req
     */
    void modifyMaterialStatusFromAppr(MaterialStatusQuery req);
    
    ///**
    // * 获取审批人列表
    // *
    // * @return
    // */
    //List<ApproveUserQuery> getMaterialApprover();
    
    /**
     * 获取素材提交审批入参
     *
     * @param submApproveQuery
     * @return
     */
    SubmitProcessQuery getCmpApprovalProcess(SubmApproveQuery submApproveQuery);
    
    /**
     * 素材审批列表
     *
     * @param req
     * @return
     */
    IPage<ApprRecord> approveRecord(DimMaterialApproveJxQuery req);
    
    /**
     * 获取审批流程以及下一节点审批人
     *
     * @param submitProcessDTO
     * @return
     */
    SubmitProcessDTO getMaterialApprover(SubmitProcessDTO submitProcessDTO);
}
