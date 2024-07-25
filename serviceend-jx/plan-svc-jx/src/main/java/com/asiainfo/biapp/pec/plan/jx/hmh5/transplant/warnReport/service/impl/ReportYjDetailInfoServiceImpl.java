package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils.ExportUtil;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.controller.reqParam.SelectWarnDetailParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjDetailResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.mapper.ReportYjDetailInfoMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjDetailInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.ReportYjDetailInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通预警报表（明细） 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Service
public class ReportYjDetailInfoServiceImpl extends ServiceImpl<ReportYjDetailInfoMapper, ReportYjDetailInfo> implements ReportYjDetailInfoService {

    @Override
    public Page<ReportYjDetailResultInfo> selectDetailWarnReport(SelectWarnDetailParam param) {
        return baseMapper.selectDetailWarnReport(new Page<>(param.getPageNum(), param.getPageSize()),param);
    }

    @Override
    public void exportDetailWarnReport(SelectWarnDetailParam param) {
        List<Map<String, Object>> detailWarnReports = baseMapper.exportDetailWarnReport(param);
        if (detailWarnReports.size() == 0)
            throw new BaseException("没有符合条件的预警数据，无法导出！");
        ExportUtil.mapToXlsx("客户通明细预警报表", true, detailWarnReports);
    }
}
