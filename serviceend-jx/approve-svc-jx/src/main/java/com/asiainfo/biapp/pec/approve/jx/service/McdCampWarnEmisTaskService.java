package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.dto.WarningDetailReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTask;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTaskExt;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 活动预警任务表 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-12-06
 */
public interface McdCampWarnEmisTaskService extends IService<McdCampWarnEmisTask> {

    /**
     * 查询预警详情
     *
     * @param req
     * @return
     */
    IPage<McdCampWarnEmisTaskExt> queryWarnDetailList(Page page, WarningDetailReq req);

    /**
     * 查询预警汇总
     *
     * @param req
     * @return
     */
    IPage<McdCampWarnEmisTaskExt> queryWarnSumList(Page page,  WarningDetailReq req);
}
