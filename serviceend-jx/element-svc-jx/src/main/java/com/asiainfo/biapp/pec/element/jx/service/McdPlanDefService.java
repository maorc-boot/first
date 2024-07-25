package com.asiainfo.biapp.pec.element.jx.service;

import com.asiainfo.biapp.pec.element.jx.model.McdPlanDef;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanDefInfo;
import com.asiainfo.biapp.pec.element.jx.query.PlanExcluQuery;
import com.asiainfo.biapp.pec.element.jx.query.PlanManageJxQuery;
import com.asiainfo.biapp.pec.element.jx.query.RequestPlanQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-11-08
 */
public interface McdPlanDefService extends IService<McdPlanDef> {
    /**
     * 产品分页列表查询
     *
     * @param planQuery
     * @return
     */
    IPage<McdPlanDef> queryPlanDefPageList(PlanManageJxQuery planQuery);

    /**
     * 查询产品详情
     *
     * @param planId
     * @return
     */
    McdPlanDef getPlanDetailById(String planId);


    /**
     * 查询产品列表信息(互斥)
     * @param planQuery
     * @return
     */
    IPage<McdPlanDefInfo> queryPlanDefList(RequestPlanQuery planQuery);


}
