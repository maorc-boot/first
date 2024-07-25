package com.asiainfo.biapp.pec.plan.jx.coordination.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdPlanDef;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCoordinatePlanModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.PlanConfListQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.McdCoordinatePlanTypeVo;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.McdCoordinatePlanVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * description: 产品配置查询dao
 *
 * @author: lvchaochao
 * @date: 2023/7/24
 */
public interface McdCoordinatePlanDao extends BaseMapper<McdCoordinatePlanVo> {

    /**
     * 产品配置列表查询
     *
     * @param query    查询
     * @param page     分页对象
     * @param isUpdate true--查询列表数据 false--修改操作，查询该产品系列下所有已配置了优先级的产品
     * @return {@link Page}<{@link McdCoordinatePlanModel}>
     */
    Page<McdCoordinatePlanModel> qryPlanConfList(@Param("page") Page<McdCoordinatePlanModel> page, @Param("query") PlanConfListQuery query, @Param("isUpdate") boolean isUpdate);

    /**
     * 查询该产品系列下所有已配置了优先级的产品（不带分页，返回所有数据）
     *
     * @param query 查询
     * @return {@link List}<{@link McdCoordinatePlanModel}>
     */
    List<McdCoordinatePlanModel> qryPlanConfListByPlanTypeOnSave(@Param("query") PlanConfListQuery query);

    /**
     * 展示未配置产品系列以及产品优先级的产品
     *
     * @param page  页面
     * @param query 查询
     * @return {@link Page}<{@link McdPlanDef}>
     */
    Page<McdPlanDef> qryNotCfgPlanTypeAndPri(@Param("page") Page<McdPlanDef> page, @Param("query") PlanConfListQuery query);

    /**
     * 查询该产品系列下所有已配置了优先级的产品
     *
     * @param query 查询
     * @return {@link Page}<{@link McdCoordinatePlanModel}>
     */
    // Page<McdCoordinatePlanModel> qryPlanConfListByPlanType(@Param("page") Page<McdCoordinatePlanModel> page, @Param("query") PlanConfListQuery query);

    /**
     * 查询产品系列类型集合
     *
     * @return {@link List}<{@link McdCoordinatePlanTypeVo}>
     */
    List<McdCoordinatePlanTypeVo> qryPlanTypeList();

}
