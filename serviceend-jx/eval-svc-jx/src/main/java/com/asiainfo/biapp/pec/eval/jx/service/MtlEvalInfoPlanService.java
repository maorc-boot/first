package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.model.MtlEvalInfoPlan;
import com.asiainfo.biapp.pec.eval.jx.req.PlanEvalPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品效果评估 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-12-18
 */
public interface MtlEvalInfoPlanService extends IService<MtlEvalInfoPlan> {
    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    IPage<MtlEvalInfoPlan> queryPage(PlanEvalPageQuery query);

    /**
     * 导出
     *
     * @param query
     * @return
     */
    List<List<String>> export(PlanEvalPageQuery query);
}
