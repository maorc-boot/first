package com.asiainfo.biapp.pec.preview.jx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.common.jx.model.McdDimChannel;
import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.service.McdDimChannelService;
import com.asiainfo.biapp.pec.common.jx.service.impl.BitMapRoaringImp;
import com.asiainfo.biapp.pec.common.jx.util.BitmapCacheUtil;
import com.asiainfo.biapp.pec.core.constants.RedisKey;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChnPreUserNum;
import com.asiainfo.biapp.pec.preview.jx.mapper.ChnPreDao;
import com.asiainfo.biapp.pec.preview.jx.service.IChnPreService;
import com.asiainfo.biapp.pec.preview.jx.service.ISensitiveCustService;
import com.asiainfo.biapp.pec.preview.jx.util.CustgroupUtil;
import com.asiainfo.biapp.pec.preview.jx.util.PreviewConst;
import com.asiainfo.biapp.pec.preview.jx.util.SftpUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author mamp
 * @date 2022/6/9
 */
@Slf4j
@Service
@RefreshScope
public class ChnPreServiceImpl extends ServiceImpl<ChnPreDao, McdChnPreUserNum> implements IChnPreService {


    @Value("${pec.preview.chnPreModel.filePath:D:\\pre_chn_model\\}")
    private String chnPreModelFilePath;
    @Value("${pec.preview.chnPreModel.bitmapFilePath:D:\\pre_chn_model\\bitmap\\}")
    private String chnPreModelBitmapFilePath;
    @Value("${pec.preview.chnPreModel.filePName:MCD_GROUP_MODEL1_20220630.txt}")
    private String chnPreModelFilePName;

    @Value("${pec.preview.chnPreModel.sftp.host:10.19.92.24}")
    private String host;
    @Value("${pec.preview.chnPreModel.sftp.port:22022}")
    private int port;
    @Value("${pec.preview.chnPreModel.sftp.username:iop-dev}")
    private String username;
    @Value("${pec.preview.chnPreModel.sftp.password:Iop@)@#)!202301}")
    private String password;
    @Value("${pec.preview.chnPreModel.sftp.serverPath:/home/iop-dev/sftp/chnPre/}")
    private String serverPath;

    @Autowired
    private CustgroupUtil custgroupUtil;

    @Resource(name = "commonMcdDimChannelServiceImpl")
    private McdDimChannelService dimChannelService;

    @Resource
    private ISensitiveCustService sensitiveCustService;

    @Resource(name = "commonMcdDimChannelServiceImpl")
    private McdDimChannelService channelService;

    @Override
    public void updateChnPreModel() {
        updateChnPreModelByFile("1");
    }

    /**
     * 备份历史数据
     */
    private void backupHis() {
        String backupDir = chnPreModelBitmapFilePath + "his" + File.separator;
        if (!FileUtil.exist(backupDir)) {
            FileUtil.mkdir(backupDir);
        }
        FileUtil.listFileNames(chnPreModelBitmapFilePath).stream().forEach(file -> {
            FileUtil.copy(chnPreModelBitmapFilePath + file, backupDir + file, true);
            FileUtil.del(chnPreModelBitmapFilePath + file);
        });
    }


    /**
     * 计算客户群渠道偏好数据
     *
     * @param custGroupId  客户群ID
     * @param dataDate     数据日期
     * @param custFileName 客户群清单文件名称
     */
    @Override
    public boolean custChnPreCal(String custGroupId, String dataDate, String custFileName) {

        boolean result = custgroupUtil.downloadCustFile(custFileName);
        if (!result) {
            log.error("下载清单文件失败:custGroupId={},dataDate={},custFileName={}", custGroupId, dataDate, custFileName);
            return false;
        }
        //获取redis锁
        String lockKey = RedisKey.LOCK + PreviewConst.CUST_CHN_PRE_CAL;
        String lockId = "";
        try {
            log.info("尝试获取客户群偏好数据计算任务redis分布式锁:({})......", lockKey);
            lockId = RedisUtils.getRedisLock(lockKey, 60 * 5);
            if (StringUtils.isBlank(lockId)) {
                log.info("未获取到客户群偏好数据计算任务redis分布式锁:({})......", lockKey);
                return false;
            }

            IBitMap bitMap = custgroupUtil.createCustBitMap(custGroupId, dataDate, custFileName);
            int custTotalNum = bitMap.getCount();
            if (0 == custTotalNum) {
                log.warn("清单文件为空:custGroupId={},dataDate={},custFileName={}", custGroupId, dataDate, custFileName);
                return false;
            }

            List<McdChnPreUserNum> list = new ArrayList<>();
            LambdaQueryWrapper<McdDimChannel> query = new LambdaQueryWrapper<>();
            // 查询所有在线的渠道
            query.eq(McdDimChannel::getOnlineStatus, 1);
            List<McdDimChannel> channels = dimChannelService.list(query);
            for (McdDimChannel channel : channels) {
                String channelId = channel.getChannelId();
                IBitMap chnPreBitmap = BitmapCacheUtil.getChnPreBitmap(channelId);
                if (chnPreBitmap == null || chnPreBitmap.getCount() <= 0) {
                    continue;
                }
                int num = chnPreBitmap.andWithoutModify(bitMap).getCount();
                log.info("channel : {} , 用户数: {}", channelId, num);
                McdChnPreUserNum chnPreUserNum = new McdChnPreUserNum();
                chnPreUserNum.setCustgroupId(custGroupId);
                chnPreUserNum.setChannelId(channelId);
                chnPreUserNum.setDataDate(dataDate);
                chnPreUserNum.setUserNum(num);
                if (custTotalNum == 0) {
                    chnPreUserNum.setUserRatio(0.0);
                } else {
                    //保留四位小数，四舍五入
                    double userRatioDouble = num / (double) custTotalNum;
                    BigDecimal userRatioBig = new BigDecimal(userRatioDouble);
                    double userRatio = userRatioBig.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                    chnPreUserNum.setUserRatio(userRatio);
                }
                chnPreUserNum.setPreLevel(1);
                list.add(chnPreUserNum);
            }
            UpdateWrapper<McdChnPreUserNum> wrapper = new UpdateWrapper<>();
            wrapper.lambda().eq(McdChnPreUserNum::getCustgroupId, custGroupId)
                    .eq(McdChnPreUserNum::getDataDate, dataDate);
            // custGroupId + dataDate 不能重复，生成前先删除之前的数据
            this.remove(wrapper);
            saveBatch(list);
        } catch (Exception e) {
            log.error("客户群偏好数据计算失败", e);
            return false;
        } finally {
            log.info("执行客户群偏好数据计算完成，开始释放redis锁:({}), lockId:({})......", lockKey, lockId);
            if (StringUtils.isNoneBlank(lockId)) {
                RedisUtils.releaseRedisLock(lockKey, lockId);
            }
            // 客户群偏好数据计算结束后,更新敏感客户群数据
            sensitiveCustService.refreshSensitiveBitmapByCustgroupId(custGroupId);
        }
        return true;
    }

    /**
     * 更新渠道偏好数据（数据来源：文件）
     */
    private void updateChnPreModelByFile(String preLevel) {
        log.info("开始....");
        long start = System.currentTimeMillis();
        String modeFlilePath = downloadFile(chnPreModelFilePName);
        if (StrUtil.isEmpty(modeFlilePath)) {
            log.error("渠道偏好模型文件下载异常:{}", chnPreModelFilePName);
            return;
        }
        // 渠道偏好模型文件路径:
        File dataFile = FileUtil.file(chnPreModelFilePath + chnPreModelFilePName);

        // 备份历史数据
        backupHis();
        Map<String, IBitMap> firstChnPreBitMaps = new ConcurrentHashMap();
        LineIterator iterator;
        try {
            iterator = FileUtils.lineIterator(dataFile);
            String line;
            String[] lineItems;
            int lineIdx = 0;
            String channelId, phone;
            IBitMap chnBitMap;
            while (iterator.hasNext()) {
                lineIdx++;
                line = iterator.nextLine();
                lineItems = line.split(",");
                if (lineItems.length < 2) {
                    log.error("数据异常,第{}行,line:{}", lineIdx, line);
                }
                channelId = lineItems[1];
                phone = lineItems[0];
                chnBitMap = firstChnPreBitMaps.get(channelId);
                if (null == chnBitMap) {
                    chnBitMap = new BitMapRoaringImp(false);
                    firstChnPreBitMaps.put(channelId, chnBitMap);
                }
                chnBitMap.add(Long.valueOf(phone));
                if (lineIdx % 1000000 == 0) {
                    log.info("已经读取{}行", lineIdx);
                }
            }
            log.info(StrUtil.format("文件遍历完成，共{}行,用时{}", lineIdx, (System.currentTimeMillis() - start)));

            List<String> onlineChannelIds = getOnlineChannelIds();

            log.info("开始更新bitmap数据到redis");
            for (Map.Entry<String, IBitMap> entry : firstChnPreBitMaps.entrySet()) {
                log.info("***渠道:{},偏好用户数量:{}***", entry.getKey(), entry.getValue().getCount());
                BitmapCacheUtil.pushChnPreBitmap(entry.getKey(), entry.getValue());
                onlineChannelIds.remove(entry.getKey());
            }
            // 没有偏好数据的渠道bitmap也要清空
            if (CollectionUtil.isNotEmpty(onlineChannelIds)) {
                for (String chnId : onlineChannelIds) {
                    BitmapCacheUtil.pushChnPreBitmap(chnId, new BitMapRoaringImp(true));
                }
            }
            log.info("更新bitmap数据到redis,结束");
            log.info(StrUtil.format("总用时:{}", System.currentTimeMillis() - start));

        } catch (Exception e) {
            log.error("createBitmap error: ", e);
        }
    }

    /**
     * 下载文件
     *
     * @return
     */
    private String downloadFile(String fileName) {

        File localCustFile = FileUtil.file(chnPreModelFilePath + fileName);
        if (localCustFile.exists()) {
            log.info("文件{}已经下载，删除重新下载......", fileName);
            localCustFile.deleteOnExit();
        }
        SftpUtils sfDat = null;
        ChannelSftp sftpDat = null;
        try {
            sfDat = new SftpUtils();
            sftpDat = sfDat.connect(host, port, username, password, null);
            boolean result = sfDat.download(serverPath, serverPath + fileName, chnPreModelFilePath + fileName, sftpDat);

            if (result) {
                return chnPreModelFilePath + fileName;
            }
        } catch (Exception e) {
            log.info("下载客群文件失败--fileName：{}......", fileName);
            return null;
        } finally {
            if (sfDat != null) {
                try {
                    sfDat.disconnect(sftpDat);
                } catch (Exception e) {
                    log.info("关闭SFTP异常！！！");
                }
            }
        }
        return null;
    }

    /**
     * 获取所有状态为”在线"渠道的ID
     *
     * @return
     */
    private List<String> getOnlineChannelIds() {
        LambdaQueryWrapper<McdDimChannel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdDimChannel::getOnlineStatus, "1").select(McdDimChannel::getChannelId);
        List<McdDimChannel> channelList = channelService.list(queryWrapper);
        List<String> channelIdList = channelList.stream().map(c -> c.getChannelId()).collect(Collectors.toList());
        if (null == channelIdList) {
            channelIdList = new ArrayList<>();
        }
        return channelIdList;
    }
}
