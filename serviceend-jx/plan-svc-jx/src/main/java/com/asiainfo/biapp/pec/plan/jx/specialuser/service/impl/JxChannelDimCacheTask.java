package com.asiainfo.biapp.pec.plan.jx.specialuser.service.impl;

import com.asiainfo.biapp.pec.plan.jx.specialuser.dao.JxChannelDimDao;
import com.asiainfo.biapp.pec.plan.jx.specialuser.model.McdDimChannelJx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 缓存渠道信息
 * </p>
 *
 * @author imcd
 * @since 2023-04-07
 */
@Component
@Slf4j
@RefreshScope
public class JxChannelDimCacheTask implements ApplicationRunner {
    //渠道表MCD_DIM_CHANNEL信息缓存
    private volatile List<McdDimChannelJx> dimChannelInfos = new ArrayList<>();
    //渠道和名称的映射
    Map<String ,String > map = new ConcurrentHashMap<>();

	@Resource
	private JxChannelDimDao jxChannelDimDao;

    @PostConstruct
    public void initBean() {
        log.info("初始化bean:JxChannelDimCacheTask");
        map = new ConcurrentHashMap<String, String>();
        dimChannelInfos = new ArrayList<>();
    }

    @Scheduled(cron = "${plan-svc-jx:channel:load-dim-channel-cron}")
    public void process() {
        try {
            log.info("渠道信息同步缓存开始");
            dimChannelInfos = jxChannelDimDao.getAllMcdDimChannel();
            for (McdDimChannelJx dimChannelInfo : dimChannelInfos) {
                map.put(dimChannelInfo.getChannelId(),dimChannelInfo.getChannelName());
            }
            log.info("渠道信息同步缓存结束");
        } catch (Exception e) {
            log.error("渠道信息同步缓存开始刷新异常", e);
        }
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        process();
    }

    public McdDimChannelJx getChannelInfoByChannelId(String channelId){
        List<McdDimChannelJx> mcdDimChannels = new ArrayList<>();
        if (StringUtils.isNotBlank(channelId)) {
            mcdDimChannels = dimChannelInfos.stream().filter(oo -> oo.getChannelId().equals(channelId)).collect(Collectors.toList());
        }
        return CollectionUtils.isNotEmpty(mcdDimChannels)?mcdDimChannels.get(0):null;
    }

    public String getChannelNameByChannelId(String channelId){
        String channelName = "";
        if(StringUtils.isNotBlank(channelId)){
            channelName = map.get(channelId);
        }
        List<McdDimChannelJx> mcdDimChannels = new ArrayList<>();
        if (StringUtils.isBlank(channelName)) {
            dimChannelInfos = jxChannelDimDao.getAllMcdDimChannel();
            mcdDimChannels = dimChannelInfos.stream().filter(oo -> oo.getChannelId().equals(channelId)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(mcdDimChannels)){
                McdDimChannelJx mcdDimChannelJx = mcdDimChannels.get(0);
                map.put(mcdDimChannelJx.getChannelId(),mcdDimChannelJx.getChannelName());
            }
        }
        return StringUtils.isNotBlank(channelName)?channelName:map.get(channelId);
    }
}
