package com.asiainfo.biapp.pec.plan.jx.channelconfig.service;

import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdChannel;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdCustgroup;

import java.util.List;
import java.util.Map;

public interface IMcdSensitiveService {
    List<McdChannel> getChannels();

    long queryChannelSettingCnt(String channelId);

    List<Map<String, Object>> queryChannelSetting(String id, String channelId, Pager pager);

    boolean saveConfig(String id, String channelId, String khqList);

    int getMoreMyCustomCount(String id, String keyWords);

    List<McdCustgroup> getMoreMyCustom(String id, String keyWords, Pager pager);

    boolean deleteSettings(String channelId, String khqId);
}
