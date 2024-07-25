package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.req.McdChanAanPlanMarketingHistoryQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdPlanMarketingHistoryQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdChanPlanSuccessMarketingHistoryInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdPlanMarketingHistoryInfo;

import java.util.List;

public interface McdMarketingHistoryService {

    List<McdPlanMarketingHistoryInfo> queryPlanMarketingHistory(McdPlanMarketingHistoryQuery  req);

    List<McdChanPlanSuccessMarketingHistoryInfo> queryChanAndPlanMarketingHistory(McdChanAanPlanMarketingHistoryQuery req);


}
