package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.jx.model.ReportProductExprDt;
import com.asiainfo.biapp.pec.eval.jx.req.ReportFormReq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author: lvchaochao
 * @date: 2023/2/2
 */
public interface ReportFormMapper extends BaseMapper<ReportProductExprDt> {

    /**
     * 获取产品分类订购报表列表数据
     *
     * @param req req
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getPlanOrderReportList(ReportFormReq req);

    /**
     * 获取产品分类订购报表列表数据总数
     *
     * @param req req
     * @return int int
     */
    int getPlanOrderReportListCount(ReportFormReq req);
}
