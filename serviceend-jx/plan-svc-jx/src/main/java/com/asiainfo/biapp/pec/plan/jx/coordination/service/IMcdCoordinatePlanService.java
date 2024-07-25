package com.asiainfo.biapp.pec.plan.jx.coordination.service;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdPlanDef;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCoordinatePlanModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.PlanConfListQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.McdCoordinatePlanTypeVo;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.McdCoordinatePlanVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * description: 产品配置查询service
 *
 * @author: lvchaochao
 * @date: 2023/7/24
 */
public interface IMcdCoordinatePlanService extends IService<McdCoordinatePlanVo> {

    /**
     * 产品配置列表查询
     *
     * @param query 查询
     * @return {@link Page}<{@link McdCoordinatePlanModel}>
     */
    Page<McdCoordinatePlanModel> qryPlanConfList(PlanConfListQuery query);

    /**
     * 查询该产品系列下所有已配置了优先级的产品（带分页）
     *
     * @param query 查询
     * @return {@link Page}<{@link McdCoordinatePlanModel}>
     */
    Page<McdCoordinatePlanModel> qryPlanConfListByPlanType(PlanConfListQuery query);

    /**
     * 查询该产品系列下所有已配置了优先级的产品（不带分页，返回所有数据）
     *
     * @param query 查询
     * @return {@link List}<{@link McdCoordinatePlanModel}>
     */
    List<McdCoordinatePlanModel> qryPlanConfListByPlanTypeOnSave(PlanConfListQuery query);

    /**
     * 展示未配置产品系列以及产品优先级的产品
     *
     * @param query 查询
     * @return {@link Page}<{@link McdPlanDef}>
     */
    Page<McdPlanDef> qryNotCfgPlanTypeAndPri(PlanConfListQuery query);

    /**
     * 产品配置新增
     *
     * @param list 列表
     * @return boolean
     */
    boolean savePlanConf(List<McdCoordinatePlanModel> list);

    /**
     * 查询产品系列类型集合
     *
     * @return {@link List}<{@link McdCoordinatePlanTypeVo}>
     */
    List<McdCoordinatePlanTypeVo> qryPlanTypeList();
}
