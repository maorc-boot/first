package com.asiainfo.biapp.pec.approve.jx.service.impl;

import com.asiainfo.biapp.pec.approve.jx.dao.McdCampWarnEmisTaskMapper;
import com.asiainfo.biapp.pec.approve.jx.dto.WarningDetailReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTask;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTaskExt;
import com.asiainfo.biapp.pec.approve.jx.service.McdCampWarnEmisTaskService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 活动预警任务表 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-12-06
 */
@Service
public class McdCampWarnEmisTaskServiceImpl extends ServiceImpl<McdCampWarnEmisTaskMapper, McdCampWarnEmisTask> implements McdCampWarnEmisTaskService {

    @Resource
    private McdCampWarnEmisTaskMapper mapper;

    /**
     * 查询预警详情
     *
     * @param page
     * @param req
     * @return
     */
    @Override
    public IPage<McdCampWarnEmisTaskExt> queryWarnDetailList(Page page, WarningDetailReq req) {
        return mapper.queryWarnDetailList(page,req);
    }

    /**
     * 查询预警汇总
     *
     * @param page
     * @param req
     * @return
     */
    @Override
    public IPage<McdCampWarnEmisTaskExt> queryWarnSumList(Page page, WarningDetailReq req) {
        return mapper.queryWarnSumList(page,req);
    }
}
