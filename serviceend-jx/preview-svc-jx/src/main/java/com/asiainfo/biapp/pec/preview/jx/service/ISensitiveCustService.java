package com.asiainfo.biapp.pec.preview.jx.service;

/**
 * 渠道敏感客户群service
 *
 * @author mamp
 * @date 2023/4/20
 */
public interface ISensitiveCustService {
    /**
     * 更新bitMap数据
     */

    void refreshSensitiveBitmap(String channelId) throws Exception;

    /**
     * 客户群清单更新后，更新其所在渠道的敏感客户群bitmap缓存
     *
     * @param custgroupId
     */
    void refreshSensitiveBitmapByCustgroupId(String custgroupId);
}
