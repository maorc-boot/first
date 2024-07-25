package com.asiainfo.biapp.pec.approve.jx.dao;

import com.asiainfo.biapp.pec.approve.jx.dto.WarningDetailReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTask;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTaskExt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 活动预警任务表 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-12-06
 */
public interface McdCampWarnEmisTaskMapper extends BaseMapper<McdCampWarnEmisTask> {

    /**
     * 查询预警详情
     *
     * @param req
     * @return
     */
    IPage<McdCampWarnEmisTaskExt> queryWarnDetailList(Page page, @Param("param") WarningDetailReq req);

    /**
     * 查询预警汇总
     *
     * @param req
     * @return
     */
    IPage<McdCampWarnEmisTaskExt> queryWarnSumList(Page page, @Param("param") WarningDetailReq req);

}
