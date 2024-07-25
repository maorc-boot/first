package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.util.StrUtil;
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
public class ChannelConfServiceImpl906 implements ChannelConfService {


    @Override
    public String getChannelId() {
        return "906";
    }

    /**
     * 获取渠道扩展配置信息
     *
     * @param object
     * @param task
     * @return
     */
    @Override
    public CampChannelExtQuery getChannelExtConf(Object[] object, McdCampImportTask task) throws Exception {
        if (object.length < 17) {
            throw new Exception("配置数据不完整");
        }
        CampChannelExtQuery channelExtConf = new CampChannelExtQuery();
        // 默认全量推送
        channelExtConf.setColumnExt1("1");
        // 场景ID
        String sceneId = (String) object[16];
        channelExtConf.setColumnExt6(sceneId);
        if (object.length < 18) {
            return channelExtConf;
        }
        // 标签属性ID
        if (StrUtil.isNotEmpty(String.valueOf(object[17]))) {
            channelExtConf.setColumnExt7(String.valueOf(object[17]));
        }
        return channelExtConf;
    }
}
