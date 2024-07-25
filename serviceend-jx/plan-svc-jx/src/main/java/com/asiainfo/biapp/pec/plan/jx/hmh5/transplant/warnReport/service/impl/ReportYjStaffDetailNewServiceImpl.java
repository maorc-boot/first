package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils.ExportUtil;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.impl.resultInfo.ReportYjStaffResultInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.mapper.ReportYjStaffDetailNewMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model.ReportYjStaffDetailNew;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.service.ReportYjStaffDetailNewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户通预警报表（个人） 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Service
public class ReportYjStaffDetailNewServiceImpl extends ServiceImpl<ReportYjStaffDetailNewMapper, ReportYjStaffDetailNew> implements ReportYjStaffDetailNewService {

    @Override
    public Page<ReportYjStaffResultInfo> selectStaffWarnReport(Integer pageNum, Integer pageSize, String statDate) {
        return baseMapper.selectStaffWarnReport(new Page(pageNum, pageSize), statDate);
    }

    @Override
    public void exportStaffWarnReport(String statDate) {
        List<Map<String, Object>> staffWarnReports = baseMapper.exportStaffWarnReport(statDate);
        if (staffWarnReports.size() == 0)
            throw new BaseException("没有符合日期：" + String.format("%tF", statDate) + "的预警数据，无法导出！");
        ExportUtil.mapToXlsx("客户通个人预警报表", true, staffWarnReports);
    }
}
