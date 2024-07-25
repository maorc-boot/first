package com.asiainfo.biapp.pec.preview.jx.mapper;

import com.asiainfo.biapp.pec.client.jx.preview.model.McdCampPreview;
import com.asiainfo.biapp.pec.preview.jx.entity.McdPreviewInfo;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampPreviewExecResult;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampPreviewResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 营销活动预演 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-09-22
 */
public interface McdCampPreviewMapper extends BaseMapper<McdCampPreview> {

    /**
     * 查询待执行预演任务列表
     *
     * @return
     */
    List<McdPreviewInfo> queryCampPreviewInfoList();


    /**
     * 更新活动定义表状态
     *
     * @param campsegPid    父活动ID
     * @param campsegRootId 根活动ID
     * @param campsegStatId 活动状态
     * @return
     */
    int updateCampsegStatus(@Param("campsegPid") String campsegPid, @Param("campsegRootId") String campsegRootId, @Param("campsegStatId") int campsegStatId, @Param("curStatId")int curStatId);


    /**
     * 更新channelList表状态
     *
     * @param campsegId   活动预演信息
     * @param campsegStatId 目标状态
     * @param curStatId 当前状态
     * @return
     */
    int updateCampChannelListStatus(@Param("campsegId") String campsegId, @Param("campsegStatId") int campsegStatId, @Param("curStatId")int curStatId);

    /**
     * 查询执行前预演结果
     *
     * @param campsegRootId 根活动ID
     * @return
     */
    List<McdCampPreviewResult> queryPreviewLog(@Param("campsegRootId") String campsegRootId, @Param("campsegId") String campsegId);

    /**
     * 查询执行中预演结果
     *
     * @param campsegRootId
     * @return
     */
    List<McdCampPreviewExecResult> queryPreviewExecLog(@Param("campsegRootId") String campsegRootId,@Param("campsegId") String campsegId);


}