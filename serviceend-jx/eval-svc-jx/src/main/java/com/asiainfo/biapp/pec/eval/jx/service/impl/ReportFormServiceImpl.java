package com.asiainfo.biapp.pec.eval.jx.service.impl;

import com.asiainfo.biapp.pec.eval.jx.dao.ReportFormMapper;
import com.asiainfo.biapp.pec.eval.jx.model.ReportProductExprDt;
import com.asiainfo.biapp.pec.eval.jx.service.ReportFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: 报表下载service实现
 *
 * @author: lvchaochao
 * @date: 2023/1/16
 */
@Service
public class ReportFormServiceImpl extends ServiceImpl<ReportFormMapper, ReportProductExprDt> implements ReportFormService {

    @Autowired
    private ReportFormMapper reportFormMapper;

    // /**
    //  * 获取产品分类订购报表列表数据
    //  *
    //  * @param pager pager
    //  * @param req   req
    //  * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
    //  */
    // @Override
    // public List<Map<String, Object>> getPlanOrderReportList(Pager pager, ReportFormReq req) {
    //     int begin = (pager.getPageNum() - SpecialNumberJx.ONE_NUMBER) * pager.getPageSize();
    //     int end = begin + pager.getPageSize();
    //     req.setPageStart(begin);
    //     req.setPageSize(end);
    //     return reportFormMapper.getPlanOrderReportList(req);
    // }
    //
    // /**
    //  * 获取产品分类订购报表列表数据总数
    //  *
    //  * @param req req
    //  * @return int int
    //  */
    // @Override
    // public int getPlanOrderReportListCount(ReportFormReq req) {
    //     return reportFormMapper.getPlanOrderReportListCount(req);
    // }
}
