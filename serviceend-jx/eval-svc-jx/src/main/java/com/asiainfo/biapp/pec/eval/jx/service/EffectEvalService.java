package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.req.EffectEvalReq;
import com.asiainfo.biapp.pec.eval.util.Pager;

import java.util.List;
import java.util.Map;

/**
 * description: 营销效果评估service
 *
 * @author: lvchaochao
 * @date: 2023/1/12
 */
public interface EffectEvalService {

    /**
     * 查询营销效果评估汇总数据
     *
     * @param req 营销效果评估入参对象
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryEffectEvaluationAllList(EffectEvalReq req);

    /**
     * 查询营销效果评估周期下钻数据
     *
     * @param pager 分页对象
     * @param req 营销效果评估入参对象
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getDetailByDrillDownType(Pager pager, EffectEvalReq req);

    /**
     * 查询营销效果评估周期下钻数据总数
     *
     * @param req 营销效果评估入参对象
     * @return int
     */
    int getDetailByDrillDownTypeCount(EffectEvalReq req);

}
