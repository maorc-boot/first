package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.req.SmartGridEvalReq;
import com.asiainfo.biapp.pec.eval.jx.vo.SmartGridEvalVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 智慧网格service
 */
public interface SmartGridEvalService {
    /**
     * 智慧网格效果评估数据---查询
     *
     * @param req
     * @return
     */
    IPage<SmartGridEvalVo> smartGridEval(SmartGridEvalReq req) throws Exception;

    /**
     * 智慧网格效果评估数据---导出
     * @param req
     * @return
     */
    List<List<String>>  smartGridEvalExport(SmartGridEvalReq req) throws Exception;


}
