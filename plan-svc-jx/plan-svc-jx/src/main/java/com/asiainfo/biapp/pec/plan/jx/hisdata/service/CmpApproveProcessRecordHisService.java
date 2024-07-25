package com.asiainfo.biapp.pec.plan.jx.hisdata.service;

import com.asiainfo.biapp.pec.plan.jx.hisdata.model.CmpApproveProcessRecordHis;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 4.0策略审批记录 服务类
 * </p>
 *
 * @author mamp
 * @since 2023-05-23
 */
public interface CmpApproveProcessRecordHisService extends IService<CmpApproveProcessRecordHis> {
        List <CmpApproveProcessRecordHis> listRecord(String campsegRootId);
}
