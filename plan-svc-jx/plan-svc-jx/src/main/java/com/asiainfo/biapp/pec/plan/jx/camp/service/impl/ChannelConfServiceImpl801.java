package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampImportTask;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ChannelConfService;
import com.asiainfo.biapp.pec.plan.vo.req.CampChannelExtQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author maxq3
 * @date 2024/7/15
 */
@Service
@Slf4j
public class ChannelConfServiceImpl801 implements ChannelConfService {
    public static final String CHANNEL_ID = "801";
    /**
     * 获取渠道配置信息
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
        //渠道扩展表信息
        Map<String,Object> channelExt = (Map<String, Object>) object[20];
        // 是否全量推送 0否 1是
        channelExtConf.setColumnExt1(channelExt.get("columnExt1") == null? "":channelExt.get("columnExt1").toString());
        // 执行端口
        channelExtConf.setColumnExt2(channelExt.get("columnExt2") == null? "":channelExt.get("columnExt2").toString());
        // 取消推荐规则设置 0否 1是
        channelExtConf.setColumnExt3(channelExt.get("columnExt3") == null? "":channelExt.get("columnExt3").toString());
        // 取消推荐时间 1~7
        channelExtConf.setColumnExt4(channelExt.get("columnExt4") == null? "":channelExt.get("columnExt4").toString());

        // 开启智能话术 0：不开启 ,1：使用省端口模型，1：使用一级IOP模型
        channelExtConf.setColumnExt18(channelExt.get("columnExt18") == null? "":channelExt.get("columnExt18").toString());
        // 话术ID 注：活动引用话术库话术ID
        channelExtConf.setColumnExt19(channelExt.get("columnExt19") == null? "":channelExt.get("columnExt19").toString());
        // 标准化素材ID
        channelExtConf.setColumnExt20(channelExt.get("columnExt20") == null? "":channelExt.get("columnExt20").toString());

        // 频次控制参数 格式：几天_几次 (801暂未使用17 临时存储)
        channelExtConf.setColumnExt17(channelExt.get("frequency") == null ? "1_1" : channelExt.get("frequency").toString());
        return channelExtConf;
    }

    /**
     * 渠道ID
     *
     * @return
     */
    @Override
    public String getChannelId() {
        return CHANNEL_ID;
    }
}
