package com.asiainfo.biapp.pec.eval.jx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.eval.jx.constants.SpecialNumberJx;
import com.asiainfo.biapp.pec.eval.jx.dao.TdEvalMapper;
import com.asiainfo.biapp.pec.eval.jx.req.TdEvalReq;
import com.asiainfo.biapp.pec.eval.jx.service.TdEvalService;
import com.asiainfo.biapp.pec.eval.util.CommonUtil;
import com.asiainfo.biapp.pec.eval.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * description: 厅店评估service实现
 *
 * @author: lvchaochao
 * @date: 2023/1/16
 */
@Service
public class TdEvalServiceImpl implements TdEvalService {

    @Autowired
    private TdEvalMapper tdEvalMapper;

    /**
     * 获取厅店评估列表数据
     *
     * @param pager pager
     * @param req   req
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> getTdEvalList(Pager pager, TdEvalReq req) {
        if (StrUtil.isNotEmpty(req.getStartDate()) && StrUtil.isNotEmpty(req.getEndDate())) {
            req.setStartDate(req.getStartDate().replaceAll(StrUtil.DASHED, ""));
            req.setEndDate(req.getEndDate().replaceAll(StrUtil.DASHED, ""));
        }
        int begin = (pager.getPageNum() - SpecialNumberJx.ONE_NUMBER) * pager.getPageSize();
        int end = begin + pager.getPageSize();
        req.setPageStart(begin);
        req.setPageSize(end);
        List<Map<String, Object>> list = tdEvalMapper.getTdEvalList(req);
        List<String> numFileds = Collections.singletonList("CAMP_SUCC_RATE");
        CommonUtil.convertNumFiled(list, numFileds);
        return list;
    }

    /**
     * 获取厅店评估导出列表数据
     *
     * @param req
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> getExportTdEvalList(TdEvalReq req) {
        if (StrUtil.isNotEmpty(req.getStartDate()) && StrUtil.isNotEmpty(req.getEndDate())) {
            req.setStartDate(req.getStartDate().replaceAll(StrUtil.DASHED, ""));
            req.setEndDate(req.getEndDate().replaceAll(StrUtil.DASHED, ""));
        }
        List<Map<String, Object>> list = tdEvalMapper.getExportTdEvalList(req);
        List<String> numFileds = Collections.singletonList("CAMP_SUCC_RATE");
        CommonUtil.convertNumFiled(list, numFileds);
        return list;
    }

    @Override
    public int getTdEvalListCount(TdEvalReq req) {
        return tdEvalMapper.getTdEvalListCount(req);
    }
}
