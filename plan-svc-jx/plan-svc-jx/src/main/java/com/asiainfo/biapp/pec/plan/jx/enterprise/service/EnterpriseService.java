package com.asiainfo.biapp.pec.plan.jx.enterprise.service;


import com.asiainfo.biapp.pec.plan.jx.enterprise.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.ApproveUserVo;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.OfferVo;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.OrgVo;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupDef;

import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2023/6/25
 */
public interface EnterpriseService {

    List<OfferVo> queryEnterprisePlan(Map<String, Object> map) throws Exception;

    List<ApproveUserVo> queryApprove() throws Exception;

    List<OrgVo> queryCityList();

    List<Map<String, Object>> getCampDetails(String campsegId) throws Exception;

    McdCampDef getCampSegInfo(String campSegId) throws Exception;

    long getTargetIdBySubitemId(String subitemId);

    McdCustgroupDef saveZqCustDefInfo(long targetId, String custgroupId, String createUserId);


}
