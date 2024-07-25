package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.model.ReportProductExprDt;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * description: 报表下载service
 *
 * @author: lvchaochao
 * @date: 2023/1/16
 */
public interface ReportFormService extends IService<ReportProductExprDt> {

    /**
     * 获取产品分类订购报表列表数据
     *
     * @param pager pager
     * @param req req
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    // List<Map<String, Object>> getPlanOrderReportList(Pager pager, ReportFormReq req);
    //
    // /**
    //  * 获取产品分类订购报表列表数据总数
    //  *
    //  * @param req req
    //  * @return int int
    //  */
    // int getPlanOrderReportListCount(ReportFormReq req);
}
