package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils.ExportUtil;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjGridResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.mapper.ReportYjGridDetailNewMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjGridDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.ReportYjGridDetailNewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通预警报表（网格） 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Service
public class ReportYjGridDetailNewServiceImpl extends ServiceImpl<ReportYjGridDetailNewMapper, ReportYjGridDetailNew> implements ReportYjGridDetailNewService {

    @Override
    public Page<ReportYjGridResultInfo> selectGridWarnReport(Integer pageNum, Integer pageSize, String statDate) {
        return baseMapper.selectGridWarnReport(new Page(pageNum, pageSize), statDate);
    }

    @Override
    public void exportGridWarnReport(String statDate) {
        List<Map<String, Object>> gridWarnReports = baseMapper.exportGridWarnReport(statDate);
        if (gridWarnReports.size() == 0)
            throw new BaseException("没有符合日期：" + String.format("%tF", statDate) + "的预警数据，无法导出！");
        ExportUtil.mapToXlsx("客户通网格预警报表", true, gridWarnReports);
    }
}
