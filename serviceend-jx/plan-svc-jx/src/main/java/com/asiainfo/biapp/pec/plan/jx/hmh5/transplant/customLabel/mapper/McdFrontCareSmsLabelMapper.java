package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.mapper;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.SelectCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.model.McdFrontCareSmsLabel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.impl.resultInfo.SelectCareSmsLabelResultInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
public interface McdFrontCareSmsLabelMapper extends BaseMapper<McdFrontCareSmsLabel> {
    Page<SelectCareSmsLabelResultInfo> selectCareSmsLabels(Page page, @Param("smsLabelParam") SelectCareSmsLabelParam smsLabelParam);

    /**
     * 查询最大的ID
     *
     * @return int
     */
    int selectMaxSerialNum();
}
