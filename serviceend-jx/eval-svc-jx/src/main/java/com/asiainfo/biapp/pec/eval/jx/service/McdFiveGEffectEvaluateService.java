package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.model.McdFiveGEffectEvaluate;
import com.asiainfo.biapp.pec.eval.jx.req.G5EvalPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 5G效果评估表 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-12-15
 */
public interface McdFiveGEffectEvaluateService extends IService<McdFiveGEffectEvaluate> {
    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    IPage<McdFiveGEffectEvaluate> queryPage(G5EvalPageQuery query);

    /**
     * 导出
     *
     * @param query
     * @return
     */
    List<List<String>> export(G5EvalPageQuery query);
}
