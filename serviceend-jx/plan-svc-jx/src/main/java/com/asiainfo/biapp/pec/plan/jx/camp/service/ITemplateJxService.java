package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.req.QueryActivityJxQuery;
import com.asiainfo.biapp.pec.plan.vo.ActivityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface ITemplateJxService {

    /**
     * 获取模板信息
     * @param form
     * @return
     */
    IPage<ActivityVO> queryActivityInfo(QueryActivityJxQuery form);

    /**
     * 活动状态修改
     *
     * @param activityTemplateId
     * @param activityId
     */
    void updateActivityStatus(String activityTemplateId, String activityId, String status) throws Exception;
}
