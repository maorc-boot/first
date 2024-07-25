package com.asiainfo.biapp.pec.plan.jx.channelconfig.dao;

import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdChannel;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdCustgroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IMcdSensitiveDao {
    List<McdChannel> getOnlineChannels();

    int queryChannelSettingCnt(@Param("keys") String keys);

    List<Map<String, Object>> queryChannelSetting(@Param("id") String id, @Param("keys") String keys,
                                                  @Param("pageNum") String pageNum, @Param("pageSize") String pageSize);

    void saveConfig(@Param("id") String id, @Param("channelId") String channelId, @Param("khq") String khq,
                    @Param("keys") String keys, @Param("desc") String desc);

    int getMoreMyCustomCount(@Param("id") String id, @Param("keyWords") String keyWords);

    List<McdCustgroup> getMoreMyCustom(@Param("id") String id, @Param("keyWords") String keyWords,
                                       @Param("pageNum") String pageNum, @Param("pageSize") String pageSize);

    boolean deleteSettings(@Param("keys") String keys);
}
