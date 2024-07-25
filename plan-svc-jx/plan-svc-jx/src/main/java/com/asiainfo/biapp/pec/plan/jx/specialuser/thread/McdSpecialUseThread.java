package com.asiainfo.biapp.pec.plan.jx.specialuser.thread;

import cn.hutool.core.collection.CollectionUtil;
import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.service.impl.BitMapRoaringImp;
import com.asiainfo.biapp.pec.common.jx.util.BitmapCacheUtil;
import com.asiainfo.biapp.pec.core.constants.RedisKey;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.jx.specialuser.model.JxMcdSpecialUse;
import com.asiainfo.biapp.pec.plan.jx.specialuser.service.JxSpecialUseService;
import com.asiainfo.biapp.pec.plan.model.McdSpecialUse;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseMod;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseSave;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
public class McdSpecialUseThread implements Runnable {

    private List<JxMcdSpecialUse> list;
    private List<SpecialUseSave> specialUseSaves;
    JxSpecialUseService specialUseService;
    private SpecialUseMod specialUseMod;
    private int type;

    public McdSpecialUseThread(List<JxMcdSpecialUse> list, JxSpecialUseService specialUseService) {
        this.list = list;
        this.type = 1;
        this.specialUseService = specialUseService;
    }

    public McdSpecialUseThread(JxSpecialUseService specialUseService, List<SpecialUseSave> specialUseSaves) {
        this.specialUseSaves = specialUseSaves;
        this.type = 2;
        this.specialUseService = specialUseService;
    }

    public McdSpecialUseThread(SpecialUseMod specialUseMod, JxSpecialUseService specialUseService) {
        this.type = 3;
        this.specialUseService = specialUseService;
        this.specialUseMod = specialUseMod;
    }

    @Override
    public void run() {
        // 新增加
        if (type == 1) {
            add();
            return;
        }
        if (type == 2) {
            del();
            return;
        }
        if (type == 3) {
            update();
        }
    }

    /**
     * 添加
     */
    private void add() {
        // 1.更新reids数据
        List<String> collect = list.stream().map(JxMcdSpecialUse::getProductNo).collect(Collectors.toList());
        Set<String> produsctNos = new HashSet<>(collect);
        refreshRedis(produsctNos);
        // 更新 BitMap
        addBitMap();
    }

    /**
     * BitMap新增
     */
    private void addBitMap() {

        // 1.2 更新bitmap缓存
        Map<String, List<JxMcdSpecialUse>> listMap = list.stream().collect(Collectors.groupingBy(JxMcdSpecialUse::getChannelId));
        for (Map.Entry<String, List<JxMcdSpecialUse>> entry : listMap.entrySet()) {
            String channelId = entry.getKey();
            log.info("开始更新渠道:{}Bitmap缓存", channelId);
            BitmapCacheUtil.pullBwlBitmap(channelId);
            IBitMap bwlBitmap = BitmapCacheUtil.getBwlBitmap(channelId);
            if (null == bwlBitmap) {
                bwlBitmap = new BitMapRoaringImp(true);
            }
            log.info("渠道:{}Bitmap缓存更新前数据量:{}", channelId, bwlBitmap.getCount());
            for (JxMcdSpecialUse specialUse : entry.getValue()) {
                bwlBitmap.add(Long.valueOf(specialUse.getProductNo()));
            }
            BitmapCacheUtil.pushBwlBitmap(channelId, bwlBitmap);
            log.info("渠道:{}Bitmap缓存更新后数据量:{}", channelId, bwlBitmap.getCount());
        }
    }

    /**
     * 将数据拆分批次，并同步到reids，默认100条一批
     */
    private void refreshRedis(Set<String> produsctNos) {

        List<String> productList = new ArrayList<>(200);
        LambdaQueryWrapper<McdSpecialUse> queryWrapper;
        int i = 0;
        for (String produsctNo : produsctNos) {
            productList.add(produsctNo);
            i++;
            // 如果号码太多，按照100条号码一批进行更新
            if (productList.size() >= 100) {
                queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(McdSpecialUse::getChannelId, McdSpecialUse::getProductNo, McdSpecialUse::getCustType)
                        .in(McdSpecialUse::getProductNo, productList);
                List<McdSpecialUse> specialUseList = specialUseService.list(queryWrapper);
                refreshReisBatch(specialUseList, productList);
                productList.clear();
            }
        }
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(McdSpecialUse::getChannelId, McdSpecialUse::getProductNo,McdSpecialUse::getCustType)
                .in(McdSpecialUse::getProductNo, productList);
        List<McdSpecialUse> specialUseList = specialUseService.list(queryWrapper);
        refreshReisBatch(specialUseList, productList);


    }

    /**
     * 批量更新reids数据
     *
     * @param specialUseList
     */
    private void refreshReisBatch(List<McdSpecialUse> specialUseList, List<String> productList) {

        Map<String, List<McdSpecialUse>> listMap = specialUseList.stream().collect(Collectors.groupingBy(McdSpecialUse::getProductNo));
        Set<String> productSet = listMap.keySet();
        if (productList.size() > productSet.size()) {
            // 有部分号码没有免打扰数据，需要把这部分数据从redis删除
            List<String> tempList = new ArrayList<>(productSet);
            productList.removeAll(tempList);
            for (String product : productList) {
                RedisUtils.deleteKey(RedisKey.BLACK_WHITE_REDIS_KEY_PREFIX + product);
            }
        }

        //1.1 构建免打扰redis数据结构
        Map<String, Object> map = new HashMap();
        for (McdSpecialUse model : specialUseList) {
            @SuppressWarnings("unchecked")
            Set<String> set = (Set<String>) map.get(RedisKey.BLACK_WHITE_REDIS_KEY_PREFIX + model.getProductNo());
            if (set == null) {
                set = new HashSet<String>();
                map.put(RedisKey.BLACK_WHITE_REDIS_KEY_PREFIX + model.getProductNo(), set);
            }
            set.add(getKey(model));
        }
        log.info("本次需要加载到redis的免打扰客户号码数目：{}条", map.size());

        try {
            //1.2 加载免打扰信息到redis缓存，失效时间24小时(防止数据加载期间无法提供服务，多设置1小时)
            RedisUtils.pipBatchJson(map, 25 * 60 * 60);
            log.info("免打扰信息成功加载到redis缓存，加载号码{}条，过期时间：25小时 ......", map.size());
        } catch (Exception e) {
            log.error("免打扰信息redis缓存加载失败，失败{}条", list.size());
        }
    }

    /**
     * 删除
     */
    private void del() {
        // 1. 删除redis
        List<String> collect = specialUseSaves.stream().map(SpecialUseSave::getPhoneNos).collect(Collectors.toList());
        //本次修改的所有号码
        Set<String> produsctNos = new HashSet<>(collect);
        refreshRedis(produsctNos);

        // 2. 删除bitMap
        delBitMap();


    }

    /**
     * 删除bitMap
     */
    private void delBitMap() {
        // 按照渠道分组
        Map<String, List<SpecialUseSave>> map = specialUseSaves.stream().collect(Collectors.groupingBy(SpecialUseSave::getChannelId));
        // 1.2 更新bitmap缓存
        for (Map.Entry<String, List<SpecialUseSave>> entry : map.entrySet()) {
            String channelId = entry.getKey();
            log.info("开始更新(删除)渠道:{}Bitmap缓存", channelId);
            BitmapCacheUtil.pullBwlBitmap(channelId);
            IBitMap bwlBitmap = BitmapCacheUtil.getBwlBitmap(channelId);
            if (null == bwlBitmap) {
                bwlBitmap = new BitMapRoaringImp(true);
            }
            log.info("渠道:{}Bitmap缓存更新(删除)前数据量:{}", channelId, bwlBitmap.getCount());
            for (SpecialUseSave specialUse : entry.getValue()) {
                bwlBitmap.remove(Long.valueOf(specialUse.getPhoneNos()));
            }
            BitmapCacheUtil.pushBwlBitmap(channelId, bwlBitmap);
            log.info("渠道:{}Bitmap缓存更新(删除)后数据量:{}", channelId, bwlBitmap.getCount());
        }
    }

    /**
     * 更新
     */
    private void update() {
        String oldChannelIds = specialUseMod.getOldChannelId() == null ? "" : specialUseMod.getOldChannelId();
        String channelIds = specialUseMod.getChannelId() == null ? "" : specialUseMod.getChannelId();
        if (oldChannelIds.equals(channelIds)) {
            log.info("oldChannelIds 和 channelIds 相同无需更新");
            return;
        }
        String phoneNo = specialUseMod.getPhoneNos();

        List<String> oldList = new ArrayList<>();
        oldList.addAll(Arrays.asList(oldChannelIds.split(",")));
        List<String> newList = new ArrayList<>();
        newList.addAll(Arrays.asList(channelIds.split(",")));
        // 删除没有变更的渠道ID
        for (String s : oldChannelIds.split(",")) {
            if (newList.contains(s)) {
                oldList.remove(s);
                newList.remove(s);
            }
        }
        if (CollectionUtil.isEmpty(oldList) && CollectionUtil.isEmpty(newList)) {
            // 数据没有变化
            log.info("数据没有变化无需更新");
            return;
        }
        // 1. 更新redis数据
        Set<String> set = new HashSet<>();
        set.add(phoneNo);
        refreshRedis(set);

        // 2.1 删除 BitMap 老数据
        if (CollectionUtil.isNotEmpty(oldList)) {
            for (String oldChannel : oldList) {
                log.info("开始更新(删除)渠道:{}Bitmap缓存", oldChannel);
                BitmapCacheUtil.pullBwlBitmap(oldChannel);
                IBitMap bwlBitmap = BitmapCacheUtil.getBwlBitmap(oldChannel);
                if (null == bwlBitmap) {
                    bwlBitmap = new BitMapRoaringImp(true);
                }
                log.info("渠道:{}Bitmap缓存更新(删除)前数据量:{}", oldChannel, bwlBitmap.getCount());
                bwlBitmap.remove(Long.valueOf(phoneNo));
                BitmapCacheUtil.pushBwlBitmap(oldChannel, bwlBitmap);
                log.info("渠道:{}Bitmap缓存更新(删除)后数据量:{}", oldChannel, bwlBitmap.getCount());
            }
        }

        // 2.1 添加 BitMap 新数据
        if (CollectionUtil.isNotEmpty(newList)) {
            for (String oldChannel : newList) {
                log.info("开始更新(增加)渠道:{}Bitmap缓存", oldChannel);
                BitmapCacheUtil.pullBwlBitmap(oldChannel);
                IBitMap bwlBitmap = BitmapCacheUtil.getBwlBitmap(oldChannel);
                if (null == bwlBitmap) {
                    bwlBitmap = new BitMapRoaringImp(true);
                }
                log.info("渠道:{}Bitmap缓存更新(增加)前数据量:{}", oldChannel, bwlBitmap.getCount());
                bwlBitmap.add(Long.valueOf(phoneNo));
                BitmapCacheUtil.pushBwlBitmap(oldChannel, bwlBitmap);
                log.info("渠道:{}Bitmap缓存更新(增加)后数据量:{}", oldChannel, bwlBitmap.getCount());
            }
        }
    }

    /**
     * 返回客户号码在redis缓存中的黑白名单信息数据结构
     *
     * @return
     */
    public String getKey(McdSpecialUse model) {

        StringBuilder sb = new StringBuilder();
        sb.append(model.getChannelId() == null ? "-1" : model.getChannelId()).append(":")
                .append("-1:")
                .append((model.getCustType() == null || model.getCustType() <= 0) ? "-1" : model.getCustType());
        return sb.toString();
    }

}
