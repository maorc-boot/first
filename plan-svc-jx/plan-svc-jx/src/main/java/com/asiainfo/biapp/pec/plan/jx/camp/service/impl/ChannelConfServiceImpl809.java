package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampImportTask;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ChannelConfService;
import com.asiainfo.biapp.pec.plan.vo.req.CampChannelExtQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author mamp
 * @date 2023/4/11
 */
@Service
@Slf4j
public class ChannelConfServiceImpl809 implements ChannelConfService {


    @Override
    public String getChannelId() {
        return "809";
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
        if (object.length < 20) {
            throw new Exception("配置数据不完整");
        }
        CampChannelExtQuery channelExtConf = new CampChannelExtQuery();
        // 默认全量推送
        channelExtConf.setColumnExt1("1");
        // 营销用语
        String execContent = (String) object[15];
        channelExtConf.setColumnExt2(execContent);
        // 短信内容
        String smsContent = (String) object[16];
        channelExtConf.setColumnExt3(smsContent);

        String taskType = (String) object[17];
        if (StringUtils.isNotEmpty(taskType)) {
            // 任务类型 编码
            channelExtConf.setColumnExt4(taskType.split(":")[0]);
            if (taskType.split(":").length >= 2) {
                // // 任务类型 名称
                channelExtConf.setColumnExt5(taskType.split(":")[1]);
            }
        }
        // 是否为重点活动
        String isImportant = (String) object[18];
        if (StrUtil.isNotEmpty(isImportant)) {
            channelExtConf.setColumnExt6(isImportant.split(":")[0]);
        }
        // 是否为商机活动
        String isBusi = (String) object[19];
        if (StrUtil.isNotEmpty(isBusi)) {
            channelExtConf.setColumnExt7(isBusi.split(":")[0]);
        }

        return channelExtConf;
    }
}
