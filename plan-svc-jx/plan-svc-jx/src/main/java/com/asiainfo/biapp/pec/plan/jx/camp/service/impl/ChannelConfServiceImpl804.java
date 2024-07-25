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
public class ChannelConfServiceImpl804 implements ChannelConfService {

    /**
     * 获取渠道扩展配置信息
     *
     * @param object
     * @param task
     * @return
     */
    @Override
    public CampChannelExtQuery getChannelExtConf(Object[] object, McdCampImportTask task) throws Exception {
        if (object.length < 20) {
            throw new Exception("配置数据不完整");
        }
        CampChannelExtQuery channelExtConf = new CampChannelExtQuery();
        // 默认全量推送
        channelExtConf.setColumnExt1("1");
        // 客户群规则描述
        String custRuleDesc = (String) object[16];
        channelExtConf.setColumnExt3(custRuleDesc);
        // 营销话术
        String word = (String) object[17];
        channelExtConf.setColumnExt2(word);
        // 活动政策
        String campPolicy = (String) object[18];
        channelExtConf.setColumnExt4(campPolicy);
        // 短信模板
        String smsTpl = (String) object[19];

        channelExtConf.setColumnExt5(smsTpl);
        return channelExtConf;
    }

    @Override
    public String getChannelId() {
        return "804";
    }
}  smsTpl.equals("abc");
