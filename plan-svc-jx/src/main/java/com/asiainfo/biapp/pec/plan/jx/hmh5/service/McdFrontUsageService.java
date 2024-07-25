package com.asiainfo.biapp.pec.plan.jx.hmh5.service;


import com.asiainfo.biapp.pec.plan.jx.hmh5.request.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageDetailInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageOppoHandleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageSummaryInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface McdFrontUsageService {

    public IPage<McdFrontUsageInfo>  queryCityUsageInfo(McdFrontUsageCityQuery req);
    public IPage<McdFrontUsageInfo>  queryChannelUsageInfo(McdFrontUsageChannelQuery req);
    public IPage<McdFrontUsageInfo>  queryGridUsageInfo(McdFrontUsageGridQuery req);
    public IPage<McdFrontUsageInfo>  queryCountyUsageInfo(McdFrontUsageCountyQuery req);
    public IPage<McdFrontUsageSummaryInfo>  queryCampsegHandleSummary(McdFrontUsageQuery req);

    public IPage<McdFrontUsageDetailInfo>  queryCampsegHandleDetail(McdFrontUsageQuery req);
    public IPage<McdFrontUsageOppoHandleInfo>  queryOppoCampCityHandle(McdFrontUsageQuery req);

}
