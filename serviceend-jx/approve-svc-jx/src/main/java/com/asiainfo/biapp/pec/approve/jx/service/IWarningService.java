package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.dto.WarningDetailReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTask;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTaskExt;
import com.asiainfo.biapp.pec.approve.jx.model.McdEmisReadTask;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author mamp
 * @date 2022/12/6
 */
public interface IWarningService {

    /**
     * 查询预警待办列表
     *
     * @param req
     * @return
     */
    IPage<McdCampWarnEmisTask> queryWarningList(WarningDetailReq req);

    /**
     * 查询客户通渠道/自定义预警审批驳回阅知记录
     *
     * @param req 请求入参
     * @return {@link IPage}<{@link McdEmisReadTask}>
     */
    IPage<McdEmisReadTask> queryRejectList(McdPageQuery req);

    /**
     * 更新自定义预警&客户通渠道活动驳回Emis阅知待办状态
     *
     * @param query 自定义预警id&客户通渠道活动id
     * @return boolean
     */
    boolean updateRejectEmisReadTask(McdEmisReadTask query);

    /**
     * 处理待办
     *
     * @param task
     * @return
     */
    Boolean dealWarning(McdCampWarnEmisTask task);

    /**
     * 查询预警详情
     *
     * @param req
     * @return
     */
    IPage<McdCampWarnEmisTaskExt> queryWarnDetailList(WarningDetailReq req);

    /**
     * 查询预警汇总数据
     *
     * @param req
     * @return
     */
    IPage<McdCampWarnEmisTaskExt> queryWarnSumList(WarningDetailReq req);

}
