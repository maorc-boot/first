package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampImportTask;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ChannelConfService;
import com.asiainfo.biapp.pec.plan.vo.req.CampChannelExtQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author mamp
 * @date 2023/4/11
 */
@Service
@Slf4j
public class ChannelConfServiceImpl922 implements ChannelConfService {


    @Override
    public String getChannelId() {
        return "922";
    }
    /**
     * 获取渠道扩展配置信息
     *
     * @param object
     * @param task
     * @return
     */
    @Override
    public CampChannelExtQuery getChannelExtConf(Object[] object, McdCampImportTask task) {
        CampChannelExtQuery channelExtConf = new CampChannelExtQuery();
        // 默认全量推送
        channelExtConf.setColumnExt1("1");
        return channelExtConf;
    }
}
