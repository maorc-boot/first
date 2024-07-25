package com.asiainfo.biapp.pec.preview.jx.service;

import com.asiainfo.biapp.pec.client.jx.preview.model.McdCampPreview;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustConf;
import com.asiainfo.biapp.pec.preview.jx.entity.McdPreviewInfo;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampPreviewExecResult;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampPreviewResult;
import com.asiainfo.biapp.pec.preview.jx.model.PreveiwResultReq;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 营销活动预演 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-09-22
 */
public interface McdCampPreviewService extends IService<McdCampPreview> {

    /**
     * 查询待执行预演任务列表
     *
     * @return
     */
    List<McdPreviewInfo> queryCampPreviewInfoList();

    /**
     * 开启预演任务
     *
     * @return
     */
    boolean startPreview();

    /**
     * 更新活动义表状态
     *
     * @param previewInfo   活动
     * @param campsegStatId 目标状态
     * @param curStatId 当前状态
     * @return
     */
    int updateCampsegStatus(McdPreviewInfo previewInfo, int campsegStatId, int curStatId);

    /**
     * 更新channel_list表状态
     *
     * @param previewInfo   活动ID
     * @param campsegStatId 状态
     * @return
     */
    int updateCampChannelListStatus(McdPreviewInfo previewInfo, int campsegStatId, int curStatId);


    /**
     * 更新预演表状态
     *
     * @param mcdPreviewInfo
     * @param status
     * @return
     */
    boolean updateCampPreviewStatus(McdPreviewInfo mcdPreviewInfo, int status);


    /**
     * 是否活动的所有渠道预演完成
     *
     * @param campsegRootId 活动RootId
     * @return
     */
    boolean isAllChannelPreviewFinish(String campsegRootId);

    /**
     * 获取需要频次过滤的渠道
     *
     * @return
     */
    String getFqcFilterChnIds();

    /**
     * 获取需要已推荐过滤的需求ID
     *
     * @return
     */
    String getRecommendFilterChnIds();

    /**
     * 查询执行前预演结果
     *
     * @param campsegRootId 根活动ID
     * @return
     */
    List<McdCampPreviewResult> queryPreviewLog(String campsegRootId, String campsegId);

    /**
     * 查询执行中预演结果
     *
     * @param param
     * @return
     */
    List<McdCampPreviewExecResult> queryPreviewExecLog(PreveiwResultReq param);

    /**
     * 通过渠道ID查询 敏感客户群列表
     *
     * @param channelId 渠道ID
     */
    List<McdChannelSensitiveCustConf> getSensitiveCustsByChannleId(String channelId);


}
