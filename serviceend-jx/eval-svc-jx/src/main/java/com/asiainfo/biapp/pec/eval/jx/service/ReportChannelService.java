package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.model.ReportChannelExprDt;
import com.asiainfo.biapp.pec.eval.jx.model.ReportProductExprDt;
import com.asiainfo.biapp.pec.eval.jx.req.ReportFormReq;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * description: 产品分渠道订购报表service
 *
 * @author: lvchaochao
 * @date: 2023/2/8
 */
public interface ReportChannelService {

    /**
     * 获取产品分渠道订购报表列表数据
     *
     * @param pager pager
     * @param req req
     * @return {@link IPage}<{@link ReportProductExprDt}>
     */
    IPage<ReportChannelExprDt> getOrderProductDMDataByPage(Page<ReportChannelExprDt> pager, ReportFormReq req);

    /**
     * 获取产品分渠道订购报表列表导出数据
     *
     * @param req req
     * @return {@link List}<{@link ReportChannelExprDt}>
     */
    List<ReportChannelExprDt> getExportOrderProductDMDataByPage(ReportFormReq req);

}
