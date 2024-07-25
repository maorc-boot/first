package com.asiainfo.biapp.pec.approve.jx.dao;

import com.asiainfo.biapp.pec.approve.jx.model.McdCampChannelExt;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 策略渠道运营位扩展属性表 Mapper 接口
 * </p>
 *
 * @author imcd
 * @since 2021-11-11
 */
public interface McdCampChannelExtDao extends BaseMapper<McdCampChannelExt> {

    /**
     * 用父策略id删除渠道扩展属性
     * @param campsegPid
     */
    void deleteByCampsegPid(@Param("campsegPid") String campsegPid);

    /**
     * 用父策略id获取渠道扩展属性
     * @param campsegPid
     */
    List<McdCampChannelExt> qryByCampsegPid(@Param("campsegPid") String campsegPid);

    /**
     * 用根策略id删除渠道扩展属性
     * @param campsegRootId
     */
    void deleteByCampsegRootId(@Param("campsegRootId") String campsegRootId);

    /**
     * 用根策略id获取渠道扩展属性
     * @param campsegRootId
     */
    List<McdCampChannelExt> qryByCampsegRootId(@Param("campsegRootId") String campsegRootId);

    /**
     * 用根策略id获取渠道扩展属性
     * @param channelId
     * @param adivId
     * @param adivUrl
     * @param campsegId
     */
    List<McdCampChannelExt> validMaterial(@Param("channelId") String channelId,@Param("adivId") String adivId
            ,@Param("adivUrl") String adivUrl,@Param("campsegId") String campsegId);

}
