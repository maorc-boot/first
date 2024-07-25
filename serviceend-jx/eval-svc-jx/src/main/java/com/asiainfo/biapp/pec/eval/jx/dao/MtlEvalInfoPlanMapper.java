package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.jx.model.MtlEvalInfoPlan;
import com.asiainfo.biapp.pec.eval.jx.req.PlanEvalPageQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 产品效果评估 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-12-18
 */
@Mapper
public interface MtlEvalInfoPlanMapper extends BaseMapper<MtlEvalInfoPlan> {

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    IPage<MtlEvalInfoPlan> queryEvalInfoPlanPage(IPage<MtlEvalInfoPlan> page, PlanEvalPageQuery query);
}
