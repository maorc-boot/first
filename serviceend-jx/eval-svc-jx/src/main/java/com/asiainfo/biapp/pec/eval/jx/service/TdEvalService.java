package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.req.TdEvalReq;
import com.asiainfo.biapp.pec.eval.util.Pager;

import java.util.List;
import java.util.Map;

/**
 * description: 厅店评估service
 *
 * @author: lvchaochao
 * @date: 2023/1/16
 */
public interface TdEvalService {

    /**
     * 获取厅店评估列表数据
     *
     * @param pager pager
     * @param req   req
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getTdEvalList(Pager pager, TdEvalReq req);

    /**
     * 获取厅店评估导出列表数据
     *
     * @param req
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getExportTdEvalList(TdEvalReq req);

    /**
     * 获取厅店评估列表总数
     *
     * @param req req
     * @return int
     */
    int getTdEvalListCount(TdEvalReq req);
}
