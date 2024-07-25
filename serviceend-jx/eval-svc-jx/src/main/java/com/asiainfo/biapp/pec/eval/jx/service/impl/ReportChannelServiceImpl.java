package com.asiainfo.biapp.pec.eval.jx.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.eval.jx.dao.ReportChannelMapper;
import com.asiainfo.biapp.pec.eval.jx.model.ReportChannelExprDt;
import com.asiainfo.biapp.pec.eval.jx.model.ReportProductExprDt;
import com.asiainfo.biapp.pec.eval.jx.req.ReportFormReq;
import com.asiainfo.biapp.pec.eval.jx.service.ReportChannelService;
import com.asiainfo.biapp.pec.eval.jx.utils.TableNameCache;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcraft.jsch.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description: 产品分渠道订购报表service实现
 *
 * @author: lvchaochao
 * @date: 2023/2/8
 */
@Service
@Slf4j
public class ReportChannelServiceImpl implements ReportChannelService {

    @Autowired
    private ReportChannelMapper reportChannelMapper;

    /**
     * 获取产品分渠道订购报表列表数据
     *
     * @param pager pager
     * @param req   req
     * @return {@link IPage}<{@link ReportProductExprDt}>
     */
    @Override
    public IPage<ReportChannelExprDt> getOrderProductDMDataByPage(Page<ReportChannelExprDt> pager, ReportFormReq req) {
        String tableName = "report_channel_expr_{}";
        if (StrUtil.isNotEmpty(req.getOpTime())) {
            String temp = req.getOpTime().replace("-", "").substring(0, 6);
            tableName = StrUtil.format(tableName, temp);
        } else {
            tableName = StrUtil.format(tableName, DateUtil.format(new Date(), "yyyyMM"));
        }
        log.info("获取产品分渠道订购报表列表数据表名：{}", tableName);
        // 判断表是否存在，不存在直接返回
        if (!getExitTable(tableName)) {
            Page<ReportChannelExprDt> reportChannelExprDtPage = new Page<>(req.getCurrent(), req.getSize());
            reportChannelExprDtPage.setRecords(null);
            return reportChannelExprDtPage;
        }
        return reportChannelMapper.getOrderProductDMDataByPage(pager, req.getOpTime(), tableName);
    }

    /**
     * 获取产品分渠道订购报表列表导出数据
     *
     * @param req req
     * @return {@link List}<{@link ReportChannelExprDt}>
     */
    @Override
    public List<ReportChannelExprDt> getExportOrderProductDMDataByPage(ReportFormReq req) {
        String tableName = "report_channel_expr_{}";
        if (StrUtil.isNotEmpty(req.getOpTime())) {
            String temp = req.getOpTime().replace("-", "").substring(0, 6);
            tableName = StrUtil.format(tableName, temp);
        } else {
            tableName = StrUtil.format(tableName, DateUtil.format(new Date(), "yyyyMM"));
        }
        log.info("获取产品分渠道订购报表列表数据表名：{}", tableName);
        // 判断表是否存在，不存在直接返回
        if (!getExitTable(tableName)) {
            return new ArrayList<>();
        }
        return reportChannelMapper.getOrderProductDMDataByPage(req.getOpTime(), tableName);
    }

    /**
     * 判断表是否存在
     *
     * @param table table
     * @return boolean
     */
    private boolean getExitTable(String table) {
        if (TableNameCache.TabbleNameMap.get(table) != null) {
            return TableNameCache.TabbleNameMap.get(table);
        }
        try {
            reportChannelMapper.getExitTable(table);
            TableNameCache.TabbleNameMap.put(table, true);
        } catch (Exception e) {
            log.error("获取产品分渠道订购报表列表数据表名{}不存在: ", table, e);
            return false;
        }
        return true;
    }

}
