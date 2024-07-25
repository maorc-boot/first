package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.req.McdChanAanPlanMarketingHistoryQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdCustContactMarketingHistoryQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdPlanMarketingHistoryQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdChanPlanSuccessMarketingHistoryInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCustContactMarketingHistoryInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCustSuccessMarketingHistoryInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdPlanMarketingHistoryInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface McdMarketingHistoryDao {

    List<McdPlanMarketingHistoryInfo> queryPlanMarketingHistory(@Param("req") McdPlanMarketingHistoryQuery req);
    List<McdChanPlanSuccessMarketingHistoryInfo> queryChanAndPlanMarketingHistory(@Param("req") McdChanAanPlanMarketingHistoryQuery req);

}
