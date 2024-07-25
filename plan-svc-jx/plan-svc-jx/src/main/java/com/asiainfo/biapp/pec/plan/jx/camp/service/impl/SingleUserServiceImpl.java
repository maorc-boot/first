package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.asiainfo.biapp.pec.common.jx.constant.CommonConstJx;
import com.asiainfo.biapp.pec.common.jx.model.CampsegSyncInfo;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.SingleUserQueryDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampChannelListJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SingleUserDetailQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SingleUserReq;
import com.asiainfo.biapp.pec.plan.jx.camp.service.SingleUserService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.feign.CgfInnerRequestFeignUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.service.feign.McdLogChnExpFeignClient;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.MarketResultDetail;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.SingleUserCampsegInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.SingleUserChannelPlanStatus;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.SingleUserMarketResultDetail;
import com.asiainfo.biapp.pec.plan.jx.specialuser.service.impl.JxChannelDimCacheTask;
import com.asiainfo.biapp.pec.plan.jx.utils.DateTool;
import com.asiainfo.biapp.pec.plan.model.McdCampChannelList;
import com.asiainfo.biapp.pec.plan.service.IMcdCampChannelListService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mamp
 * @date 2022/12/14
 */
@Service
@Slf4j
public class SingleUserServiceImpl implements SingleUserService {

    @Value("${plan.singelUser.channelIds:906,804}")
    private String channelIds;

    private static final String EXE_LOG_MONTHLY_PREFIX = "MCD_CHANNEL_EXE_LOG_";

    @Resource
    private CgfInnerRequestFeignUtil cgfInnerRequestFeignUtil;

    @Resource
    private JxChannelDimCacheTask jxChannelDimCacheTask;

    @Resource
    private SingleUserQueryDao singleUserQueryDao;

    @Resource
    private IMcdCampChannelListService campChannelListService;

    @Resource
    private McdLogChnExpFeignClient  mcdLogChnExpFeignClient;


    @Override
    public IPage<SingleUserCampsegInfo> queryCampsegInfo(SingleUserReq req) {
        String phoneNo = req.getPhoneNo();
        Map<String, String> stringMap = RedisUtils.getHashStringMap(CommonConstJx.MCD_CHANNEL_ADIVS);

        // 获取用户所有的已推荐数据
        String queryPhoneKey = StrUtil.format("MCD:REC:{}", phoneNo);
        // 用户的已推荐数据
        Map<String, String> recommendMap = RedisUtils.getHashStringMap(queryPhoneKey);
        //客户归属客户群容器
        List<CampsegSyncInfo> campList = new ArrayList<>();
        for (String channelId : channelIds.split(",")) {
            String value = stringMap.get(channelId);
            JSONArray jsonArray = JSONUtil.parseArray(value);
            List<String> list = JSONUtil.toList(jsonArray, String.class);
            for (String channelAdivId : list) {
                // 查询当前渠道+运营位的所有活动详情
                String campsegRedisKey = "MCD:CAMPSEG:" + channelId + ":" + channelAdivId;
                Map<Object, Object> recInfoMap = RedisUtils.getMap(campsegRedisKey);

                JSONObject reqJson = new JSONObject();
                reqJson.set("phoneNo", phoneNo).set("channelId", channelId).set("channelAdivId", channelAdivId);
                JSONObject resJson = JSONUtil.parseObj(cgfInnerRequestFeignUtil.listCustomMatch(reqJson));

                JSONObject dataJson = resJson.getJSONObject("data");
                JSONArray matchedActivities = dataJson.getJSONArray("matchedActivities");
                for (int i = 0; i < matchedActivities.size(); i++) {
                    JSONObject matchItemJson = (JSONObject) matchedActivities.get(i);
                    String activityId = matchItemJson.getStr("activityId");
                    Object o = recInfoMap.get(activityId);
                    CampsegSyncInfo campsegInfo = JSONUtil.toBean(o.toString(), CampsegSyncInfo.class, true);
                    campList.add(campsegInfo);
                }
            }
        }
        Map<String, List<CampsegSyncInfo>> resulteMap = new LinkedHashMap<>();
        for (CampsegSyncInfo syncInfo : campList) {
            String campsegId = syncInfo.getCampsegId();
            List<CampsegSyncInfo> syncInfos = resulteMap.get(campsegId);
            if (CollectionUtil.isEmpty(syncInfos)) {
                syncInfos = new ArrayList<>();
            }
            syncInfos.add(syncInfo);
            resulteMap.put(campsegId, syncInfos);
        }
        List<SingleUserCampsegInfo> result = new ArrayList<>();
        for (Map.Entry<String, List<CampsegSyncInfo>> entry : resulteMap.entrySet()) {
            String key = entry.getKey();
            List<CampsegSyncInfo> value = entry.getValue();
            if (CollectionUtil.isEmpty(value)) {
                continue;
            }
            CampsegSyncInfo campsegSyncInfo = value.get(0);
            SingleUserCampsegInfo info = new SingleUserCampsegInfo();
            info.setCampsegId(key);
            info.setCampsegName(campsegSyncInfo.getCampsegName());
            info.setCampsegRootId(campsegSyncInfo.getCampsegRootId());
            info.setExpireDate(campsegSyncInfo.getStartDate() + "~" + campsegSyncInfo.getEndDate());
            info.setPlanId(campsegSyncInfo.getPlanId());
            info.setPlanName(campsegSyncInfo.getPlanName());

            List<SingleUserChannelPlanStatus> userChannelPlanStatusList = new ArrayList<>();
            for (CampsegSyncInfo syncInfo : value) {
                SingleUserChannelPlanStatus planStatus = new SingleUserChannelPlanStatus();
                planStatus.setChannelId(syncInfo.getChannelId());
                planStatus.setChannelName(jxChannelDimCacheTask.getChannelNameByChannelId(syncInfo.getChannelId()));
                // 在当前产品已推荐过的活动
                String recommendCampId = recommendMap.get(syncInfo.getPlanId());
                if (syncInfo.getCampsegId().equals(recommendCampId)) {
                    planStatus.setStatus(2);
                    planStatus.setStatusDesc("政策曝光");
                } else {
                    planStatus.setStatus(100);
                    planStatus.setStatusDesc("待推荐");
                    userChannelPlanStatusList.add(planStatus);
                }
            }
            info.setUserChannelPlanStatusList(userChannelPlanStatusList);
            result.add(info);
        }
        IPage<SingleUserCampsegInfo> page = new Page(req.getCurrent(), req.getSize());
        if (CollectionUtil.isEmpty(result)) {
            page.setRecords(result);
            page.setPages(0);
            page.setTotal(0);
            return page;
        }

        Integer current = req.getCurrent();
        Integer size = req.getSize();
        int start = (current - 1) * size;
        int end = (current * size) > result.size() ? result.size() : (current * size);
        page.setRecords(result.subList(start, end));
        int pages = result.size() % size == 0 ? result.size() % size : result.size() % size;
        page.setPages(pages);
        page.setTotal(result.size());
        return page;
    }



    @Override
    public IPage<SingleUserCampsegInfo> queryCampsegInfoByCondition(SingleUserReq req) {
        String phoneNo = req.getPhoneNo();


        SingleUserDetailQuery query = new SingleUserDetailQuery();
        query.setPhoneNo(phoneNo);
        query.setQueryMonth(req.getQueryMonth());
        query.setCampsegStatus(req.getCampsegStatus()+"");
        query.setMarketResult(req.getMarketResult());

        Map<String, String> stringMap = RedisUtils.getHashStringMap(CommonConstJx.MCD_CHANNEL_ADIVS);

        //客户归属客户群容器
        List<CampsegSyncInfo> campList = new ArrayList<>();
        for (String channelId : channelIds.split(",")) {
            String value = stringMap.get(channelId);
            JSONArray jsonArray = JSONUtil.parseArray(value);
            List<String> list = JSONUtil.toList(jsonArray, String.class);
            for (String channelAdivId : list) {
                // 查询当前渠道+运营位的所有活动详情
                String campsegRedisKey = "MCD:CAMPSEG:" + channelId + ":" + channelAdivId;
                Map<Object, Object> recInfoMap = RedisUtils.getMap(campsegRedisKey);

                JSONObject reqJson = new JSONObject();
                reqJson.set("phoneNo", phoneNo).set("channelId", channelId).set("channelAdivId", channelAdivId);
                JSONObject resJson = JSONUtil.parseObj(cgfInnerRequestFeignUtil.listCustomMatch(reqJson));

                JSONObject dataJson = resJson.getJSONObject("data");
                JSONArray matchedActivities = dataJson.getJSONArray("matchedActivities");
                for (int i = 0; i < matchedActivities.size(); i++) {
                    JSONObject matchItemJson = (JSONObject) matchedActivities.get(i);
                    String activityId = matchItemJson.getStr("activityId");
                    Object o = recInfoMap.get(activityId);
                    if (Objects.isNull(o)){
                        continue;
                    }
                    CampsegSyncInfo campsegInfo = JSONUtil.toBean(o.toString(), CampsegSyncInfo.class, true);
                    campList.add(campsegInfo);
                }
            }
        }


        ActionResponse actionResponse = mcdLogChnExpFeignClient.queryChannelExeLogInfo(query);
        List<CampsegSyncInfo> syncInfoList=(List<CampsegSyncInfo> ) actionResponse.getData();

        if (CollectionUtil.isNotEmpty(syncInfoList)){
            String jsonString = JSON.toJSONString(syncInfoList);
            syncInfoList = JSON.parseArray(jsonString, CampsegSyncInfo.class);
            campList.addAll(syncInfoList);
        }

        Map<String, List<CampsegSyncInfo>> resulteMap = new LinkedHashMap<>();
        for (CampsegSyncInfo syncInfo : campList) {
            String campsegId = syncInfo.getCampsegId();
            List<CampsegSyncInfo> syncInfos = resulteMap.get(campsegId);
            if (CollectionUtil.isEmpty(syncInfos)) {
                syncInfos = new ArrayList<>();
            }
            syncInfos.add(syncInfo);
            resulteMap.put(campsegId, syncInfos);
        }

        List<SingleUserCampsegInfo> result = new ArrayList<>();
        for (Map.Entry<String, List<CampsegSyncInfo>> entry : resulteMap.entrySet()) {
            String key = entry.getKey();
            List<CampsegSyncInfo> value = entry.getValue();
            if (CollectionUtil.isEmpty(value)) {
                continue;
            }
            CampsegSyncInfo campsegSyncInfo = value.get(0);
            SingleUserCampsegInfo info = new SingleUserCampsegInfo();
            info.setCampsegId(key);
            info.setCampsegName(campsegSyncInfo.getCampsegName());
            info.setCampsegRootId(campsegSyncInfo.getCampsegRootId());
            info.setExpireDate(campsegSyncInfo.getStartDate() + "~" + campsegSyncInfo.getEndDate());
            info.setPlanId(campsegSyncInfo.getPlanId());
            info.setPlanName(campsegSyncInfo.getPlanName());

            List<SingleUserChannelPlanStatus> userChannelPlanStatusList = new ArrayList<>();
            List<SingleUserMarketResultDetail> singleUserMarketResultDetailList = new ArrayList<>();
            List<SingleUserMarketResultDetail> removeSingleUserMarketResultDetailList = new ArrayList<>();
            for (CampsegSyncInfo syncInfo : value) {
                query.setCampsegId(syncInfo.getCampsegId());
                query.setChannelId(syncInfo.getChannelId());

                ActionResponse channelInfo = mcdLogChnExpFeignClient.queryChannelInfo(query);
                singleUserMarketResultDetailList = (List<SingleUserMarketResultDetail>) channelInfo.getData();


                //如果渠道列表不为空 则遍历渠道下所有操作信息
                if(singleUserMarketResultDetailList != null && !singleUserMarketResultDetailList.isEmpty()){
                    String jsonStr = JSON.toJSONString(singleUserMarketResultDetailList);
                    singleUserMarketResultDetailList = JSON.parseArray(jsonStr, SingleUserMarketResultDetail.class);

                    for(SingleUserMarketResultDetail singleUserMarketResultDetail : singleUserMarketResultDetailList){

                        //获取渠道名称及id
                        String channelId = singleUserMarketResultDetail.getChannelId();
                        String channelName = singleUserMarketResultDetail.getChannelName();
                        query.setChannelId(channelId);

                        //查询用户在当前活动所关联各个渠道信息操作详情生命周期

                        ActionResponse resultDetail = mcdLogChnExpFeignClient.queryMarketingResultDetail(query);
                        List<MarketResultDetail> marketResultDetailList = (List<MarketResultDetail> ) resultDetail.getData();


                        if(marketResultDetailList != null && !marketResultDetailList.isEmpty()){
                            String jsonSt = JSON.toJSONString(marketResultDetailList);
                            marketResultDetailList = JSON.parseArray(jsonSt, MarketResultDetail.class);

                            for (MarketResultDetail marketResultDetail : marketResultDetailList) {
                                int status =  marketResultDetail.getStatus();

                                //针对前端页面操作方便  另追加用户最新操作状态列表
                                SingleUserChannelPlanStatus singleUserChannelPlanLastStatus = new SingleUserChannelPlanStatus();
                                singleUserChannelPlanLastStatus.setChannelId(channelId);
                                singleUserChannelPlanLastStatus.setChannelName(channelName);
                                singleUserChannelPlanLastStatus.setStatus(status);

                                if(status == 0){
                                    singleUserChannelPlanLastStatus.setStatusDesc("办理失败");
                                    marketResultDetail.setStatusDesc("办理失败");
                                }else if(status == 1){
                                    singleUserChannelPlanLastStatus.setStatusDesc("成功办理");
                                    marketResultDetail.setStatusDesc("成功办理");
                                }else if(status == 2){
                                    singleUserChannelPlanLastStatus.setStatusDesc("政策曝光");
                                    marketResultDetail.setStatusDesc("政策曝光");
                                }else{
                                    removeSingleUserMarketResultDetailList.add(singleUserMarketResultDetail);
                                    continue;
                                }
                                userChannelPlanStatusList.add(singleUserChannelPlanLastStatus);
                            }
                        }else{
                            marketResultDetailList = new ArrayList<>();
                            //针对前端页面操作方便  另追加用户最新操作状态列表
                            SingleUserChannelPlanStatus singleUserChannelPlanLastStatus = new SingleUserChannelPlanStatus();
                            singleUserChannelPlanLastStatus.setChannelId(channelId);
                            singleUserChannelPlanLastStatus.setChannelName(channelName);

                            singleUserChannelPlanLastStatus.setStatus(100);
                            singleUserChannelPlanLastStatus.setStatusDesc("待推荐");
                            //创建单个渠道所有操作列表(只有未推荐)
                            MarketResultDetail marketResultDetail = new MarketResultDetail();
                            marketResultDetail.setStatus(100);
                            marketResultDetail.setStatusDesc("待推荐");
                            marketResultDetail.setLogTime("");
                            marketResultDetail.setCampsegChannelDescription(syncInfo.getCampsegDesc());
                            marketResultDetailList.add(marketResultDetail);
                            userChannelPlanStatusList.add(singleUserChannelPlanLastStatus);

                        }
                        singleUserMarketResultDetail.setMarketResultChannelDetailList(marketResultDetailList);
                    }
                }else {
                    //如果在日志表中查询不到任何相关信息及说明该活动为实时状态 最新状态未推荐
                    //根据活动渠道关联表 查询该活动所关联渠道
                    List<McdCampChannelList> channelList = campChannelListService.listMcdCampChannelListByCampsegRootId(syncInfo.getCampsegRootId());
                    singleUserMarketResultDetailList = new ArrayList<>();
                    this.dealTrueTimeCampsegInfo(singleUserMarketResultDetailList, channelList, userChannelPlanStatusList);
                }


            }

            singleUserMarketResultDetailList.removeAll(removeSingleUserMarketResultDetailList);
            info.setMarketResultDetailList(singleUserMarketResultDetailList);
            info.setUserChannelPlanStatusList(userChannelPlanStatusList);
            result.add(info);

        }
        IPage<SingleUserCampsegInfo> page = new Page(req.getCurrent(), req.getSize());
        if (CollectionUtil.isEmpty(result)) {
            page.setRecords(result);
            page.setPages(0);
            page.setTotal(0);
            return page;
        }

        Integer current = req.getCurrent();
        Integer size = req.getSize();
        int start = (current - 1) * size;
        int end = (current * size) > result.size() ? result.size() : (current * size);
        page.setRecords(result.subList(start, end));
        int pages = result.size() % size == 0 ? result.size() % size : result.size() % size;
        page.setPages(pages);
        page.setTotal(result.size());
        return page;
    }


    @Override
    public IPage<SingleUserCampsegInfo> queryCampsegInfoByOtherCondition(SingleUserReq req) {
        String phoneNo = req.getPhoneNo();

        SingleUserDetailQuery query = new SingleUserDetailQuery();
        query.setPhoneNo(phoneNo);
        query.setQueryMonth(req.getQueryMonth());
        query.setCampsegStatus(req.getCampsegStatus()+"");
        query.setMarketResult(req.getMarketResult());

        ActionResponse actionResponse = mcdLogChnExpFeignClient.queryChannelExeLogInfo(query);
        List<CampsegSyncInfo> syncInfoList=(List<CampsegSyncInfo> ) actionResponse.getData();
        if (syncInfoList == null){
            return null;
        }
        String jsonString = JSON.toJSONString(syncInfoList);
        syncInfoList = JSON.parseArray(jsonString, CampsegSyncInfo.class);

        Map<String, List<CampsegSyncInfo>> resulteMap = new LinkedHashMap<>();
        for (CampsegSyncInfo syncInfo : syncInfoList) {
            String campsegId = syncInfo.getCampsegId();
            List<CampsegSyncInfo> syncInfos = resulteMap.get(campsegId);
            if (CollectionUtil.isEmpty(syncInfos)) {
                syncInfos = new ArrayList<>();
            }
            syncInfos.add(syncInfo);
            resulteMap.put(campsegId, syncInfos);
        }

        List<SingleUserCampsegInfo> result = new ArrayList<>();
        for (Map.Entry<String, List<CampsegSyncInfo>> entry : resulteMap.entrySet()) {
            String key = entry.getKey();
            List<CampsegSyncInfo> value = entry.getValue();
            if (CollectionUtil.isEmpty(value)) {
                continue;
            }
            CampsegSyncInfo campsegSyncInfo = value.get(0);
            SingleUserCampsegInfo info = new SingleUserCampsegInfo();
            info.setCampsegId(key);
            info.setCampsegName(campsegSyncInfo.getCampsegName());
            info.setCampsegRootId(campsegSyncInfo.getCampsegRootId());
            info.setExpireDate(campsegSyncInfo.getStartDate() + "~" + campsegSyncInfo.getEndDate());
            info.setPlanId(campsegSyncInfo.getPlanId());
            info.setPlanName(campsegSyncInfo.getPlanName());

            List<SingleUserChannelPlanStatus> userChannelPlanStatusList = new ArrayList<>();
            List<SingleUserMarketResultDetail> singleUserMarketResultDetailList = new ArrayList<>();
            List<SingleUserMarketResultDetail> removeSingleUserMarketResultDetailList = new ArrayList<>();
            for (CampsegSyncInfo syncInfo : value) {
                query.setCampsegId(syncInfo.getCampsegId());
                query.setChannelId(syncInfo.getChannelId());

                ActionResponse channelInfo = mcdLogChnExpFeignClient.queryChannelInfo(query);
                singleUserMarketResultDetailList = (List<SingleUserMarketResultDetail>) channelInfo.getData();

                //如果渠道列表不为空 则遍历渠道下所有操作信息
                if(singleUserMarketResultDetailList != null && !singleUserMarketResultDetailList.isEmpty()){
                    String json = JSON.toJSONString(singleUserMarketResultDetailList);
                    singleUserMarketResultDetailList = JSON.parseArray(json, SingleUserMarketResultDetail.class);
                    for(SingleUserMarketResultDetail singleUserMarketResultDetail : singleUserMarketResultDetailList){

                        //获取渠道名称及id
                        String channelId = singleUserMarketResultDetail.getChannelId();
                        String channelName = singleUserMarketResultDetail.getChannelName();
                        query.setChannelId(channelId);

                        //查询用户在当前活动所关联各个渠道信息操作详情生命周期

                        ActionResponse resultDetail = mcdLogChnExpFeignClient.queryMarketingResultDetail(query);
                        List<MarketResultDetail> marketResultDetailList = (List<MarketResultDetail> ) resultDetail.getData();

                        if(marketResultDetailList != null && !marketResultDetailList.isEmpty()){
                            String jsonStr = JSON.toJSONString(marketResultDetailList);
                            marketResultDetailList = JSON.parseArray(jsonStr, MarketResultDetail.class);

                            for (MarketResultDetail marketResultDetail : marketResultDetailList) {
                                int status =  marketResultDetail.getStatus();

                                //针对前端页面操作方便  另追加用户最新操作状态列表
                                SingleUserChannelPlanStatus singleUserChannelPlanLastStatus = new SingleUserChannelPlanStatus();
                                singleUserChannelPlanLastStatus.setChannelId(channelId);
                                singleUserChannelPlanLastStatus.setChannelName(channelName);
                                singleUserChannelPlanLastStatus.setStatus(status);

                                if(status == 0){
                                    singleUserChannelPlanLastStatus.setStatusDesc("办理失败");
                                    marketResultDetail.setStatusDesc("办理失败");
                                }else if(status == 1){
                                    singleUserChannelPlanLastStatus.setStatusDesc("成功办理");
                                    marketResultDetail.setStatusDesc("成功办理");
                                }else if(status == 2){
                                    singleUserChannelPlanLastStatus.setStatusDesc("政策曝光");
                                    marketResultDetail.setStatusDesc("政策曝光");
                                }else{
                                    removeSingleUserMarketResultDetailList.add(singleUserMarketResultDetail);
                                    continue;
                                }
                                userChannelPlanStatusList.add(singleUserChannelPlanLastStatus);
                            }
                        }else{
                            marketResultDetailList = new ArrayList<>();
                            //针对前端页面操作方便  另追加用户最新操作状态列表
                            SingleUserChannelPlanStatus singleUserChannelPlanLastStatus = new SingleUserChannelPlanStatus();
                            singleUserChannelPlanLastStatus.setChannelId(channelId);
                            singleUserChannelPlanLastStatus.setChannelName(channelName);

                            singleUserChannelPlanLastStatus.setStatus(100);
                            singleUserChannelPlanLastStatus.setStatusDesc("待推荐");
                            //创建单个渠道所有操作列表(只有未推荐)
                            MarketResultDetail marketResultDetail = new MarketResultDetail();
                            marketResultDetail.setStatus(100);
                            marketResultDetail.setStatusDesc("待推荐");
                            marketResultDetail.setLogTime("");
                            marketResultDetail.setCampsegChannelDescription(syncInfo.getCampsegDesc());
                            marketResultDetailList.add(marketResultDetail);
                            userChannelPlanStatusList.add(singleUserChannelPlanLastStatus);

                        }
                        singleUserMarketResultDetail.setMarketResultChannelDetailList(marketResultDetailList);
                    }
                }else {
                    //如果在日志表中查询不到任何相关信息及说明该活动为实时状态 最新状态未推荐
                    //根据活动渠道关联表 查询该活动所关联渠道
                    List<McdCampChannelList> channelList = campChannelListService.listMcdCampChannelListByCampsegRootId(syncInfo.getCampsegRootId());

                    this.dealTrueTimeCampsegInfo(singleUserMarketResultDetailList, channelList, userChannelPlanStatusList);
                }


            }

            singleUserMarketResultDetailList.removeAll(removeSingleUserMarketResultDetailList);
            info.setMarketResultDetailList(singleUserMarketResultDetailList);
            info.setUserChannelPlanStatusList(userChannelPlanStatusList);
            result.add(info);

        }

        IPage<SingleUserCampsegInfo> page = new Page(req.getCurrent(), req.getSize());
        if (CollectionUtil.isEmpty(result)) {
            page.setRecords(result);
            page.setPages(0);
            page.setTotal(0);
            return page;
        }

        Integer current = req.getCurrent();
        Integer size = req.getSize();
        int start = (current - 1) * size;
        int end = (current * size) > result.size() ? result.size() : (current * size);
        page.setRecords(result.subList(start, end));
        int pages = result.size() % size == 0 ? result.size() % size : result.size() % size;
        page.setPages(pages);
        page.setTotal(result.size());
        return page;
    }


    /**
     * 实时活动填充数据
     * @param singleUserMarketResultDetailList
     * @param channelList
     * @param userChannelPlanStatusList
     */
    private void dealTrueTimeCampsegInfo(List<SingleUserMarketResultDetail> singleUserMarketResultDetailList,
                                         List<McdCampChannelList> channelList, List<SingleUserChannelPlanStatus> userChannelPlanStatusList) {
        if(channelList != null && !channelList.isEmpty()){
            for(McdCampChannelList channelInfo : channelList){
                String channelId = channelInfo.getChannelId();
                if("901".equals(channelId)){
                    continue;
                }
                String channelName = jxChannelDimCacheTask.getChannelNameByChannelId(channelId);

                //创建用户当前渠道最新操作信息(只有未推荐)
                SingleUserChannelPlanStatus singleUserChannelPlanLastStatus = new SingleUserChannelPlanStatus();
                singleUserChannelPlanLastStatus.setChannelId(channelId);
                singleUserChannelPlanLastStatus.setChannelName(channelName);
                singleUserChannelPlanLastStatus.setStatus(100);
                singleUserChannelPlanLastStatus.setStatusDesc("待推荐");
                userChannelPlanStatusList.add(singleUserChannelPlanLastStatus);

                //创建用户当前渠道操作详情信息(只有未推荐)
                SingleUserMarketResultDetail singleUserMarketResultDetail = new SingleUserMarketResultDetail();
                singleUserMarketResultDetail.setChannelId(channelId);
                singleUserMarketResultDetail.setChannelName(channelName);

                //创建单个渠道所有操作列表(只有未推荐)
                List<MarketResultDetail> marketResultDetailList = new ArrayList<>();
                MarketResultDetail marketResultDetail = new MarketResultDetail();
                marketResultDetail.setStatus(100);
                marketResultDetail.setStatusDesc("待推荐");
                marketResultDetail.setLogTime("");

                marketResultDetailList.add(marketResultDetail);
                singleUserMarketResultDetail.setMarketResultChannelDetailList(marketResultDetailList);
                singleUserMarketResultDetailList.add(singleUserMarketResultDetail);
            }
        }
    }


    /**
     * 根据前端所传查询月标识 获取查询月
     * @param queryMonthFlag
     * @return
     */
    private String getQueryMonth(String queryMonthFlag) {
        Date currentDate = new Date();
        String queryMonth = "";
        SimpleDateFormat formator = new SimpleDateFormat("yyyyMM");
        if(queryMonthFlag.equals("0")){
            queryMonth = formator.format(DateTool.getPreMonth(currentDate, 0));
        }else if(queryMonthFlag.equals("1")){
            queryMonth = formator.format(DateTool.getPreMonth(currentDate, 1));
        }else{
            queryMonth = formator.format(DateTool.getPreMonth(currentDate, 2));
        }
        return queryMonth;
    }

    public IPage<SingleUserCampsegInfo> test(SingleUserReq req) {
        IPage<SingleUserCampsegInfo> page = new Page(req.getCurrent(), req.getSize());
        SingleUserCampsegInfo campsegInfo = new SingleUserCampsegInfo("202211141719456427097", "测试888", "202211141719455436089", "11002368", "588元4000分钟国内主叫", "2022-12-01~2022-12-31", new ArrayList<>(), new ArrayList<>());
        campsegInfo.setUserChannelPlanStatusList(Arrays.asList(new SingleUserChannelPlanStatus("906", "营业厅网台", 1, "待推荐"), new SingleUserChannelPlanStatus("922", "HUI买APP", 2, "政策曝光")));
        page.setRecords(Arrays.asList(campsegInfo));
        page.setTotal(1L);
        page.setPages(1L);
        return page;
    }
}
