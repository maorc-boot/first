package com.asiainfo.biapp.pec.preview.jx.service;

import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustConf;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustInfo;
import com.asiainfo.biapp.pec.preview.jx.query.SensitiveCustQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 渠道敏感客户群配置表 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-10-12
 */
public interface McdChannelSensitiveCustConfService extends IService<McdChannelSensitiveCustConf> {


    /**
     * 保存敏感客户群配置
     *
     * @param confList 配置列表
     * @return
     */
    boolean saveChannelSensitiveCustConf(List<McdChannelSensitiveCustConf> confList);

    /**
     * 查询渠道敏感客户群信息
     *
     * @param query 渠道ID
     * @return
     */
    IPage<McdChannelSensitiveCustInfo> querySensitiveCustInfo(SensitiveCustQuery query);

}
