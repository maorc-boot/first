package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.request.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageDetailInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageOppoHandleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontUsageSummaryInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface McdFrontUsageDao {

     IPage<McdFrontUsageInfo> queryCityUsageInfo(Page page, @Param("req") McdFrontUsageCityQuery req);
     IPage<McdFrontUsageInfo>  queryChannelUsageInfo(Page page, @Param("req") McdFrontUsageChannelQuery req);
     IPage<McdFrontUsageInfo>  queryGridUsageInfo(Page page, @Param("req") McdFrontUsageGridQuery req);
     IPage<McdFrontUsageInfo>  queryCountyUsageInfo(Page page, @Param("req") McdFrontUsageCountyQuery req);
     //使用情况明细
     IPage<McdFrontUsageSummaryInfo>  queryCampsegHandleSummary(Page page, @Param("req") McdFrontUsageQuery req);
     IPage<McdFrontUsageDetailInfo>  queryCampsegHandleDetail(Page page, @Param("req") McdFrontUsageQuery req);
     IPage<McdFrontUsageOppoHandleInfo>  queryOppoCampCityHandle(Page page, @Param("req") McdFrontUsageQuery req);

}
