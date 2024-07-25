package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.req.CampStatReq;
import com.asiainfo.biapp.pec.eval.jx.req.EvalHomeChnExecReq;
import com.asiainfo.biapp.pec.eval.jx.req.EvalHomeReq;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeCampStatVO;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeChnExecVO;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeDataVo;

import java.util.List;

/**
 * @author mamp
 * @date 2023/5/6
 */
public interface EvalHomeService {

    /**
     * 首页-总营销数
     *
     * @param req
     * @return
     */
    List<EvalHomeDataVo> queryTotal(EvalHomeReq req) throws Exception;

    /**
     * 首页-总营成功数
     *
     * @param req
     * @return
     */
    List<EvalHomeDataVo> querySuccess(EvalHomeReq req) throws Exception;

    /**
     * 首页-营销转化率
     *
     * @param req
     * @return
     */
    List<EvalHomeDataVo> querySuccessRate(EvalHomeReq req) throws Exception;


    /**
     * 评估-效果总览-渠道执行情况
     *
     * @param req
     * @return
     * @throws Exception
     */
    List<EvalHomeDataVo> queryChnExec(EvalHomeChnExecReq req) throws Exception;


    /**
     * 评估-效果总览-渠道执行情况-近6个月成功数趋势
     *
     * @param req
     * @return
     */
    List<EvalHomeChnExecVO> queryChnExecSuccess(EvalHomeChnExecReq req) throws Exception;

    /**
     * 评估-效果总览-渠道执行情况-近6个月成功率趋势
     *
     * @param req
     * @return
     */
    List<EvalHomeChnExecVO> queryChnExecSuccessRate(EvalHomeChnExecReq req) throws Exception;


    /**
     * 评估-效果总览-策略统计
     *
     * @param req
     * @return
     */
    List<EvalHomeCampStatVO> queryCampStatistics(CampStatReq req) throws Exception;

    /**
     * 查询策略列表
     *
     * @return
     */
    List queryCampList(String keyword);


}
