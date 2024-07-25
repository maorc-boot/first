package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampImportTask;
import com.asiainfo.biapp.pec.plan.vo.req.CampChannelExtQuery;

/**
 * @author mamp
 * @date 2023/4/11
 */
public interface ChannelConfService {

    /**
     * 获取渠道配置信息
     *
     * @param object
     * @param task
     * @return
     */
    CampChannelExtQuery getChannelExtConf(Object[] object, McdCampImportTask task) throws Exception;

    /**
     * 渠道ID
     *
     * @return
     */
    String getChannelId();


}
