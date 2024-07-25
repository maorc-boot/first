package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.req.SingleUserReq;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.SingleUserCampsegInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author mamp
 * @date 2022/12/14
 */

public interface SingleUserService {

    IPage<SingleUserCampsegInfo> queryCampsegInfo(SingleUserReq req);

    IPage<SingleUserCampsegInfo> queryCampsegInfoByCondition(SingleUserReq req);

    IPage<SingleUserCampsegInfo> queryCampsegInfoByOtherCondition(SingleUserReq req);

}
