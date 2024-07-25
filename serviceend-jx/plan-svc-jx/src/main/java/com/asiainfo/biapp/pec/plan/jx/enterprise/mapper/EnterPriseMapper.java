package com.asiainfo.biapp.pec.plan.jx.enterprise.mapper;


import com.asiainfo.biapp.pec.plan.jx.enterprise.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.ApproveUserVo;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.OfferVo;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.OrgVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface EnterPriseMapper  {

    List<OfferVo> queryEnterprisePlan(Map<String, Object> params) throws Exception;


    List<ApproveUserVo> queryApprove(String approve) throws Exception;

    List<OrgVo> queryCityList();

    @MapKey("campsegId")
    List<Map<String, Object>> getCampDetails(@Param("campsegId") String campsegId);

    McdCampDef queryCampSegInfo(@Param("campSId") String campSId) throws Exception;

    Long queryTargetIdBySubitemId(@Param("subitemId")String subitemId);
}
