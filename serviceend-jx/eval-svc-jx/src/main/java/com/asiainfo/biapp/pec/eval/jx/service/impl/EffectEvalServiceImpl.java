package com.asiainfo.biapp.pec.eval.jx.service.impl;

import com.asiainfo.biapp.pec.eval.jx.constants.SpecialNumberJx;
import com.asiainfo.biapp.pec.eval.jx.dao.EffectEvalMapper;
import com.asiainfo.biapp.pec.eval.jx.req.EffectEvalReq;
import com.asiainfo.biapp.pec.eval.jx.service.EffectEvalService;
import com.asiainfo.biapp.pec.eval.util.CommonUtil;
import com.asiainfo.biapp.pec.eval.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * description: 营销效果评估service实现
 *
 * @author: lvchaochao
 * @date: 2023/1/12
 */
@Service
public class EffectEvalServiceImpl implements EffectEvalService {

    @Autowired
    private EffectEvalMapper effectEvalMapper;

    /**
     * 查询营销效果评估汇总数据
     *
     * @param req 营销效果评估入参对象
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> queryEffectEvaluationAllList(EffectEvalReq req) {
        List<Map<String, Object>> list = effectEvalMapper.queryEffectEvaluationAllList(req);
        List<String> numFileds = Arrays.asList("SUCC_RATE", "CAMP_USER_RATE");
        CommonUtil.convertNumFiled(list, numFileds);
        return list;
    }

    /**
     * 查询营销效果评估周期下钻数据
     *
     * @param pager 分页对象
     * @param req   营销效果评估入参对象
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> getDetailByDrillDownType(Pager pager, EffectEvalReq req) {
        int begin = (pager.getPageNum() - SpecialNumberJx.ONE_NUMBER) * pager.getPageSize();
        int end = begin + pager.getPageSize();
        req.setPageStart(begin);
        req.setPageSize(end);
        List<Map<String, Object>> list = effectEvalMapper.getDetailByDrillDownType(req);
        List<String> numFileds = Arrays.asList("SUCC_RATE", "CAMP_USER_RATE");
        CommonUtil.convertNumFiled(list, numFileds);
        return list;
    }

    /**
     * 查询营销效果评估周期下钻数据总数
     *
     * @param req 营销效果评估入参对象
     * @return int
     */
    @Override
    public int getDetailByDrillDownTypeCount(EffectEvalReq req) {
        return effectEvalMapper.getDetailByDrillDownTypeCount(req);
    }
}
