package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.req.CampEvalReq;
import com.asiainfo.biapp.pec.eval.jx.req.ChannelRadarMapReq;
import com.asiainfo.biapp.pec.eval.jx.vo.CampEvalVo;
import com.asiainfo.biapp.pec.eval.jx.vo.UseEffectVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2023/1/9
 */
public interface CampEvalService {


    /**
     * 活动评估
     *
     * @param req
     * @return
     */
    IPage<CampEvalVo> campEval(CampEvalReq req) throws ParseException, Exception;

    /**
     * 活动评估导出
     * @param req
     * @return
     */
    List<List<String>>  campEvalExport(CampEvalReq req) throws ParseException, Exception;

    /**
     * 查询日使用效果
     *
     * @param dataDate
     * @return
     */
    List<UseEffectVo> queryDayUseEffect(String dataDate);


    /**
     * 查询月使用效果
     *
     * @param dataDate
     * @return
     */
    List<UseEffectVo> queryMonthUseEffect(String dataDate);

    /**
     * 查询近6个月使用效果
     *
     * @param dataDate
     * @return
     */
    List<UseEffectVo> queryMonth6UseEffect(String dataDate);

    /**
     * 渠道雷达图数据查询
     *
     * @param req req
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryChannelRadarMap(ChannelRadarMapReq req);

}
