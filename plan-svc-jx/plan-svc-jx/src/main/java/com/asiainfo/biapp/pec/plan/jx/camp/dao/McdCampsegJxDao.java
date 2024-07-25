package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.model.CampExecInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampBaseInfoJxVO;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampExecReq;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.HisCampInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCampExcuteInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2022/11/2
 */
@Mapper
public interface McdCampsegJxDao {

    /**
     * 获取活动基本信息
     *
     * @param campsegId
     * @return
     */
    CampBaseInfoJxVO getCampsegBaseInfo(@Param("campsegId") String campsegId);

    /**
     * 根据campsegRootId查询所有活动基本信息
     *
     * @param campsegRootId
     * @return
     */
    List<CampBaseInfoJxVO> getCampsegBaseInfoByRootId(@Param("campsegRootId") String campsegRootId);

    /**
     * 根据客户群ID和渠道ID查询历史活动
     * @param custgroupId
     * @param channelIds
     * @return
     */
    IPage<List<HisCampInfo>> queryHisCamp(Page page, @Param("custgroupId") String custgroupId, @Param("channelIds")List<String> channelIds);

    List<String > getChannelIdByCamprootId(@Param("campsegRootId") String campsegRootId);

    List<McdCampExcuteInfo> queryCampExcuteInfo(@Param("list") List<String> channelIds);

    /**
     * 查询IOP_ACTIVITY_INFO表状态是执行中，但是MCD_CAMP_DEF 表是审批中的活动
     */
    List<Map<String, Object>> queryActivityList();

    /**
     * 查询模板
     * @return
     */
    List<Map<String, Object>> queryIopTemplate(@Param("userId")String userId);

    /**
     * 查询渠道执行情况
     * @param param
     * @return
     */
    List<CampExecInfo> queryCampExecInfo(@Param("param")CampExecReq param);

    /**
     * 查询活动场景配置信息
     *
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryCampScene();


}
