package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.model.CampExecInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampExecReq;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsBaseInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;

import java.util.List;

/**
 * @author mamp
 * @date 2022/11/11
 */
public interface CampDetailService {

    /**
     * 查询活动详情
     *
     * @param id 活动RootID
     * @return
     */
    TacticsInfoJx getCampsegInfo(String id);

    /**
     * 查询活动详情
     *
     * @param id 活动RootID
     * @return
     */
    TacticsBaseInfoJx getCampsegBaseInfo(String id);

    /**
     * 获取子活动详情
     * @param campsegId
     * @return
     */
    TacticsInfoJx getChildCamp(String campsegId);

    /**
     * 查询活动执行信息
     *
     * @param param
     * @return
     */
    List<CampExecInfo> queryPreviewExecLog(CampExecReq param);
}
