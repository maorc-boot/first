package com.asiainfo.biapp.pec.plan.jx.channelconfig.service.impl;

import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.dao.IMcdSensitiveDao;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.service.IMcdSensitiveService;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdChannel;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdCustgroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class McdSensitiveService implements IMcdSensitiveService {

    @Autowired
    IMcdSensitiveDao mcdSensitiveDao;

    @Override
    public List<McdChannel> getChannels() {
        return mcdSensitiveDao.getOnlineChannels();
    }

    @Override
    public long queryChannelSettingCnt(String channelId) {
        String keys = "%MCD_SYNC_FILTER_GROUP_CHANNEL_"+channelId+"%";
        return mcdSensitiveDao.queryChannelSettingCnt(keys);
    }

    @Override
    public List<Map<String, Object>> queryChannelSetting(String id, String channelId, Pager pager) {
        String pageNum = String.valueOf(pager.getPageNum());
        String pageSize = String.valueOf(pager.getPageSize());
        String keys = "%MCD_SYNC_FILTER_GROUP_CHANNEL_"+channelId+"%";
        return mcdSensitiveDao.queryChannelSetting(id, keys, pageNum, pageSize);
    }

    @Override
    public boolean saveConfig(String id, String channelId, String khqList) {
        String keys = "";
        String desc = channelId + "渠道敏感客户群";
        String[] khqId = khqList.split(",");
        for (String khq : khqId) {
            try {
                keys = "MCD_SYNC_FILTER_GROUP_CHANNEL_"+channelId+"_"+khq;
                int count = mcdSensitiveDao.queryChannelSettingCnt(keys);
                if (count > 0) {
                    log.warn("--key 已经存在了" + keys);
                    return false;
                }
                mcdSensitiveDao.saveConfig(id, channelId, khq,keys,desc);
            } catch (Exception e) {
                log.error("--saveConfig--", e);
            }
        }
        return true;
    }

    @Override
    public int getMoreMyCustomCount(String id, String keyWords) {
        int num = 0;
        try {
            num = mcdSensitiveDao.getMoreMyCustomCount(id,keyWords);
        } catch (Exception e) {
        }
        return num;
    }

    @Override
    public List<McdCustgroup> getMoreMyCustom(String id, String keyWords, Pager pager) {
        List<McdCustgroup> custGroupList = null;
        String pageNum = String.valueOf(pager.getPageNum());
        String pageSize = String.valueOf(pager.getPageSize());
        try {
            custGroupList = mcdSensitiveDao.getMoreMyCustom(id,keyWords,pageNum,pageSize);
        } catch (Exception e) {
            log.error("查询客户群出错");
        }
        return custGroupList;
    }

    @Override
    public boolean deleteSettings(String channelId, String khqId) {
        String keys = "MCD_SYNC_FILTER_GROUP_CHANNEL_" + channelId + "_" + khqId;
        boolean delete = false;
        try {
            delete = mcdSensitiveDao.deleteSettings(keys);
        } catch (Exception e) {
            log.error("删除失败");
        }
        return delete;
    }
}
