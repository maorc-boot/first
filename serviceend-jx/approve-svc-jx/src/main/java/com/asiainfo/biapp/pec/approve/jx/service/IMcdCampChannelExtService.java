package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.model.McdCampChannelExt;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 策略渠道运营位扩展属性表 服务类
 * </p>
 *
 * @author imcd
 * @since 2021-11-11
 */
public interface IMcdCampChannelExtService extends IService<McdCampChannelExt> {

    /**
     * 用父策略id删除渠道扩展属性
     *
     * @param campsegPid
     */
    void deleteByCampsegPid(String campsegPid);

    /**
     * 获取渠道扩展信息
     * @param campsegPid
     * @return
     */
    List<McdCampChannelExt> qryByCampsegPid(String campsegPid);

    /**
     *
     * @Title: validMaterial
     * @Description: 查询未使用过的素材信息
     * @author feify
     * @date 2021年1月27日 下午10:03:35
     * @param channelId
     * @param adivId
     * @param adivUrl
     * @param campsegId
     * @return 查询未使用过的素材信息
     */
    Boolean validMaterial(String channelId, String adivId, String adivUrl, String campsegId);


    /**
     * 用父策略id删除渠道扩展属性
     *
     * @param campsegRootId
     */
    void deleteByCampsegRootId(String campsegRootId);

    /**
     * 获取渠道扩展信息
     * @param campsegRootId
     * @return
     */
    List<McdCampChannelExt> qryByCampsegRootId(String campsegRootId);


}
