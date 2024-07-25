package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.model.req.ChannelEvaluateReqModel;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * description: 渠道评估mapper
 *
 * @author: lvchaochao
 * @date: 2023/1/11
 */
public interface ChannelEvalJxMapper {

    /**
     * 获取渠道评估数据
     *
     * @param model model
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getChannelEffectList(ChannelEvaluateReqModel model);

    /**
     * 获取评估数据总数
     *
     * @param model model
     * @return {@link Integer}
     */
    Integer getChannelEffectListTotal(ChannelEvaluateReqModel model);

    /**
     * 通过地市id获取地市信息
     *
     * @param cityId 地市id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> getCityById(@Param("cityId") String cityId);

}
