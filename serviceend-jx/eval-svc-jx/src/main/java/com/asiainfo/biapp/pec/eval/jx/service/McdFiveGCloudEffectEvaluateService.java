package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.model.McdFiveGCloudEffectEvaluate;
import com.asiainfo.biapp.pec.eval.jx.req.G5CloudEvalPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 5G云卡效果评估service
 *
 * @author lvcc
 * @date 2023/10/12
 */
public interface McdFiveGCloudEffectEvaluateService extends IService<McdFiveGCloudEffectEvaluate> {

    /**
     * 分页查询
     *
     * @param query 入参对象
     * @return IPage<McdFiveGCloudEffectEvaluate>
     */
    IPage<McdFiveGCloudEffectEvaluate> queryPage(G5CloudEvalPageQuery query);

    /**
     * 导出
     *
     * @param query 入参对象
     * @return List<Map<String, Object>>
     */
    List<List<String>> getExport5gCloudEvalList(G5CloudEvalPageQuery query);
}
