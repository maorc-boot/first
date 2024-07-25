package com.asiainfo.biapp.pec.plan.jx.coordination.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdPlanDef;
import com.asiainfo.biapp.pec.plan.jx.coordination.dao.McdCoordinatePlanDao;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCoordinatePlanModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.PlanConfListQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.IMcdCoordinatePlanService;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.McdCoordinatePlanTypeVo;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.McdCoordinatePlanVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: 产品配置查询service实现
 *
 * @author: lvchaochao
 * @date: 2023/7/24
 */
@Service
public class McdCoordinatePlanServiceImpl extends ServiceImpl<McdCoordinatePlanDao, McdCoordinatePlanVo> implements IMcdCoordinatePlanService {

    @Autowired
    private McdCoordinatePlanDao mcdCoordinatePlanDao;

    /**
     * 产品配置列表查询
     *
     * @param query 查询
     * @return {@link List}<{@link McdCoordinatePlanModel}>
     */
    @Override
    public Page<McdCoordinatePlanModel> qryPlanConfList(PlanConfListQuery query) {
        Page<McdCoordinatePlanModel> page = new Page<>(query.getCurrent(), query.getSize());
        // true--查询列表数据
        return mcdCoordinatePlanDao.qryPlanConfList(page, query, true);
    }

    /**
     * 查询该产品系列下所有已配置了优先级的产品（带分页）
     *
     * @param query 查询
     * @return {@link Page}<{@link McdCoordinatePlanModel}>
     */
    @Override
    public Page<McdCoordinatePlanModel> qryPlanConfListByPlanType(PlanConfListQuery query) {
        Page<McdCoordinatePlanModel> page = new Page<>(query.getCurrent(), query.getSize());
        // false--修改操作，查询该产品系列下所有已配置了优先级的产品
        return mcdCoordinatePlanDao.qryPlanConfList(page, query, false);
    }

    /**
     * 查询该产品系列下所有已配置了优先级的产品（不带分页，返回所有数据）
     *
     * @param query 查询
     * @return {@link List}<{@link McdCoordinatePlanModel}>
     */
    @Override
    public List<McdCoordinatePlanModel> qryPlanConfListByPlanTypeOnSave(PlanConfListQuery query) {
        return mcdCoordinatePlanDao.qryPlanConfListByPlanTypeOnSave(query);
    }

    /**
     * 展示未配置产品系列以及产品优先级的产品
     *
     * @param query 查询
     * @return {@link Page}<{@link McdPlanDef}>
     */
    @Override
    public Page<McdPlanDef> qryNotCfgPlanTypeAndPri(PlanConfListQuery query) {
        Page<McdPlanDef> page = new Page<>(query.getCurrent(), query.getSize());
        return mcdCoordinatePlanDao.qryNotCfgPlanTypeAndPri(page, query);
    }

    /**
     * 产品配置删新增
     *
     * @param list 列表
     * @return boolean
     */
    @Override
    public boolean savePlanConf(List<McdCoordinatePlanModel> list) {
        // mcdCoordinatePlanDao.up

        return false;
    }

    /**
     * 查询产品系列类型集合
     *
     * @return {@link List}<{@link McdCoordinatePlanTypeVo}>
     */
    @Override
    public List<McdCoordinatePlanTypeVo> qryPlanTypeList() {
        return mcdCoordinatePlanDao.qryPlanTypeList();
    }
}
