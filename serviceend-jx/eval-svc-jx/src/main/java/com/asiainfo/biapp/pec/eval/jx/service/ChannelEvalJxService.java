package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.model.req.ChannelEvaluateReqModel;
import com.asiainfo.biapp.pec.eval.util.Pager;

import java.util.List;
import java.util.Map;

/**
 * description: 江西渠道评估service
 *
 * @author: lvchaochao
 * @date: 2023/1/11
 */
public interface ChannelEvalJxService {

    /**
     * 获取渠道评估数据
     *
     * @param model model
     * @param pager pager
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getChannelEffectList(ChannelEvaluateReqModel model, Pager pager);

    /**
     * 通过地市id获取地市信息
     *
     * @param cityId 地市id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String,Object> getCityById(String cityId);
}
