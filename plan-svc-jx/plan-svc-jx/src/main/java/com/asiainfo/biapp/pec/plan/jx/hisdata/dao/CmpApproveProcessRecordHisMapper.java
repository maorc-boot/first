package com.asiainfo.biapp.pec.plan.jx.hisdata.dao;

import com.asiainfo.biapp.pec.plan.jx.hisdata.model.CmpApproveProcessRecordHis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 4.0策略审批记录 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2023-05-23
 */
public interface CmpApproveProcessRecordHisMapper extends BaseMapper<CmpApproveProcessRecordHis> {

    CmpApproveProcessRecordHis selectCreateRecord(@Param("campsegRootId") String campsegRootId);

}
