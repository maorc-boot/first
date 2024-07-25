package com.asiainfo.biapp.pec.element.jx.service;

import com.asiainfo.biapp.pec.element.jx.entity.McdDimChannelDesc;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 渠道说明表 服务类
 * </p>
 *
 * @since 2022-10-25
 */
public interface McdDimChannelDescService extends IService<McdDimChannelDesc> {

    /**
     * 通过channelId查询
     * @param channelId id
     * @return McdDimChannelDesc
     */
    McdDimChannelDesc queryById(String channelId);
}
