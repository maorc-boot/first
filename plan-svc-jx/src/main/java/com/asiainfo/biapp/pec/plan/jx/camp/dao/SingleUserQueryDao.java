package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.common.jx.model.CampsegSyncInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SingleUserDetailQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.MarketResultDetail;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.SingleUserMarketResultDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SingleUserQueryDao {

    List<SingleUserMarketResultDetail> queryChannelInfo(@Param("req") SingleUserDetailQuery req) ;

    List<MarketResultDetail> queryMarketingResultDetail(@Param("req") SingleUserDetailQuery req) ;

    List<CampsegSyncInfo> queryChannelExeLogInfo(@Param("req") SingleUserDetailQuery req) ;

}
