package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.req.PlanExtInfo;
import com.asiainfo.biapp.pec.plan.model.McdCampDef;

import java.util.List;

/**
 * @author mamp
 * @date 2022/10/20
 */
public interface IPlanExtInfoService {
    /**
     * 保存产品扩展信息：融合产品，同系列产品，互斥产品
     *
     * @param planExtInfoList 产品扩展信息
     * @param campDef         根活动信息
     */
    void savePlanExtInfo(List<PlanExtInfo> planExtInfoList, McdCampDef campDef);

}
