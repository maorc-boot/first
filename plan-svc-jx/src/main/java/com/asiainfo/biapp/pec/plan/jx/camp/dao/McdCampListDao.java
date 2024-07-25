package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.common.jx.model.CampsegSyncInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.TaskTypeDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface McdCampListDao {
    /**
     * 查询待刷入缓存活动列表
     *
     * @return
     */
    List<CampsegSyncInfo> syncReadyCampList();


    /**
     * 通过子活动ID,查询活动详情
     * @param campId 活动（子）ID
     * @return
     */
    CampsegSyncInfo selectCampInfoById(@Param("campId") String campId);

    List<TaskTypeDetail> selectChannelTaskTypeDetail(@Param("pcode") String pcode);

    /**
     * 查询不限定客户群活动
     * @return
     */
    List<Map<String,String>> selectNoCustCamp();
}
