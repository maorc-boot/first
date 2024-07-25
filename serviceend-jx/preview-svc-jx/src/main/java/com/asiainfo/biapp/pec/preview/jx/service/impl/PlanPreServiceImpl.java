package com.asiainfo.biapp.pec.preview.jx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.service.impl.BitMapRoaringImp;
import com.asiainfo.biapp.pec.core.config.RedisTemplateFactory;
import com.asiainfo.biapp.pec.preview.jx.config.PlanPreBitMapCache;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChnPreUserNum;
import com.asiainfo.biapp.pec.preview.jx.mapper.ChnPreDao;
import com.asiainfo.biapp.pec.preview.jx.service.PlanPreService;
import com.asiainfo.biapp.pec.preview.jx.util.PreviewConst;
import com.asiainfo.biapp.pec.preview.jx.util.SftpUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mamp
 * @date 2022/6/9
 */
@Slf4j
@Service
@RefreshScope
public class PlanPreServiceImpl extends ServiceImpl<ChnPreDao, McdChnPreUserNum> implements PlanPreService {


    @Value("${pec.preview.planPreModel.filePath:D:\\pre_chn_model\\}")
    private String chnPreModelFilePath;
    @Value("${pec.preview.planPreModel.filePName:plan_ph_label.txt}")
    private String chnPreModelFilePName;

    @Value("${pec.preview.planPreModel.sftp.host:10.19.92.24}")
    private String host;
    @Value("${pec.preview.planPreModel.sftp.port:22022}")
    private int port;
    @Value("${pec.preview.planPreModel.sftp.username:iop-dev}")
    private String username;
    @Value("${pec.preview.planPreModel.sftp.password:Iop@)@#)!202301}")
    private String password;
    @Value("${pec.preview.planPreModel.sftp.serverPath:/home/iop-dev/sftp/chnPre/}")
    private String serverPath;

    @Value("${pec.preview.planPreModel.range:0.5,0.8}")
    private String range;


    @Override
    public void updatePlanPreModel() {
        updateChnPreModelByFile();
    }

    /**
     * 更新渠道偏好数据（数据来源：文件）
     */
    private void updateChnPreModelByFile() {
        log.info("开始....");
        long start = System.currentTimeMillis();

        PlanPreBitMapCache.tmpBitMaps.clear();
        String modeFlilePath = downloadFile(chnPreModelFilePName);
        if (StrUtil.isEmpty(modeFlilePath)) {
            log.error("产品偏好模型文件下载异常:{}", chnPreModelFilePName);
            return;
        }
        // 渠道偏好模型文件路径:
        File dataFile = FileUtil.file(chnPreModelFilePath + chnPreModelFilePName);
        String[] ranges = range.split(",");
        double rangeA = Double.valueOf(ranges[0]);
        double rangeB = Double.valueOf(ranges[1]);
        // 备份历史数据
        // backupHis();
        LineIterator iterator;
        try {
            iterator = FileUtils.lineIterator(dataFile);
            String line;
            String[] lineItems;
            int lineIdx = 0;
            String planId, phone, score;
            IBitMap chnBitMap;
            while (iterator.hasNext()) {
                lineIdx++;
                line = iterator.nextLine();
                lineItems = line.split("\\|");
                if (lineItems.length < 3) {
                    log.error("数据异常,第{}行,line:{}", lineIdx, line);
                }
                phone = lineItems[0];
                planId = lineItems[1];
                score = lineItems[2];
                String level = getRange(rangeA, rangeB, score);
                Double scoreNum = 0.0;
                if (StrUtil.isNotEmpty(score)) {
                    scoreNum = Double.valueOf(score);
                }
                Map<String, IBitMap> bitMapMap = PlanPreBitMapCache.tmpBitMaps.get(planId);
                Map<String, Double> scoreMapMap = PlanPreBitMapCache.tmpScores.get(planId);
                if (null == bitMapMap) {
                    bitMapMap = new HashMap<>();
                    chnBitMap = new BitMapRoaringImp(false);
                    bitMapMap.put(level, chnBitMap);
                    PlanPreBitMapCache.tmpBitMaps.put(planId, bitMapMap);
                    scoreMapMap = new HashMap<>();
                    scoreMapMap.put(level, 0.0);
                    PlanPreBitMapCache.tmpScores.put(planId, scoreMapMap);
                } else {
                    chnBitMap = bitMapMap.get(level);
                    if (null == chnBitMap) {
                        chnBitMap = new BitMapRoaringImp(false);
                        bitMapMap.put(level, chnBitMap);
                        PlanPreBitMapCache.tmpBitMaps.put(planId, bitMapMap);
                    }

                    Double tempScore = scoreMapMap.get(level);
                    if (null == tempScore) {
                        scoreMapMap.put(level, scoreNum);
                        PlanPreBitMapCache.tmpScores.put(planId, scoreMapMap);
                    } else {
                        scoreMapMap.put(level, tempScore + scoreNum);
                        PlanPreBitMapCache.tmpScores.put(planId, scoreMapMap);
                    }
                }
                chnBitMap.add(Long.valueOf(phone));
                if (lineIdx % 1000000 == 0) {
                    log.info("已经读取{}行", lineIdx);
                }
            }
            log.info(StrUtil.format("文件遍历完成，共{}行,用时{}", lineIdx, (System.currentTimeMillis() - start)));
            if (CollectionUtil.isEmpty(PlanPreBitMapCache.tmpBitMaps) || (CollectionUtil.isEmpty(PlanPreBitMapCache.tmpScores))) {
                log.info("最新产品偏好数据为空,不更新");
                return;
            }
            PlanPreBitMapCache.planPreBitMaps.clear();
            BeanUtil.copyProperties(PlanPreBitMapCache.tmpBitMaps,PlanPreBitMapCache.planPreBitMaps);
            PlanPreBitMapCache.tmpBitMaps.clear();
            PlanPreBitMapCache.planPreScores.clear();
            BeanUtil.copyProperties(PlanPreBitMapCache.tmpScores,PlanPreBitMapCache.planPreScores);
            PlanPreBitMapCache.tmpScores.clear();
            PlanPreBitMapCache.LOCAL_TIMESTAMP = String.valueOf(System.currentTimeMillis());
            // 生成本地文件并上传文件到sftp
            writeBitMapFile(PlanPreBitMapCache.planPreBitMaps, chnPreModelFilePath + PlanPreBitMapCache.PLAN_PRE_BITMAPS_FILE_NAME);
            uploadFile2Sftp(chnPreModelFilePath + PlanPreBitMapCache.PLAN_PRE_BITMAPS_FILE_NAME);
            writeBitMapFile(PlanPreBitMapCache.planPreScores, chnPreModelFilePath + PlanPreBitMapCache.PLAN_PRE_SCORE_FILE_NAME);
            uploadFile2Sftp(chnPreModelFilePath + PlanPreBitMapCache.PLAN_PRE_SCORE_FILE_NAME);

            // 发送通知
            RedisTemplateFactory.getLettuceRedisTemplate(String.class).convertAndSend(PreviewConst.PLAN_PRE_REDIS_TOPIC, PlanPreBitMapCache.LOCAL_TIMESTAMP);
            log.info("更新bitmap数据到redis,结束");
            log.info(StrUtil.format("总用时:{}", System.currentTimeMillis() - start));

        } catch (Exception e) {
            log.error("createBitmap error: ", e);
        }
    }

    private String getRange(Double rangeA, Double rangeB, String data) {
        Double value = Double.valueOf(data);
        if (value >= 0 && value < rangeA) {
            return "1";
        }
        if (value >= rangeA && value < rangeB) {
            return "2";
        }
        if (value >= rangeB) {
            return "3";
        }
        return "2";
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
            localCustFile.delete();
        }
        log.info("开始下载文件:{}", fileName);
        long start = System.currentTimeMillis();
        SftpUtils sfDat = null;
        ChannelSftp sftpDat = null;
        try {
            sfDat = new SftpUtils();
            sftpDat = sfDat.connect(host, port, username, password, null);
            boolean result = sfDat.download(serverPath, serverPath + fileName, chnPreModelFilePath + fileName, sftpDat);
            if (result) {
                log.info("下载文件:{}成功,耗时", fileName, System.currentTimeMillis() - start);
                return chnPreModelFilePath + fileName;
            } else {
                log.error("下载文件:{}失败", fileName);
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
     * 上传文件到sftp
     *
     * @param filePath
     */
    private void uploadFile2Sftp(String filePath) {
        SftpUtils sfDat = null;
        ChannelSftp sftpDat = null;
        try {
            sfDat = new SftpUtils();
            sftpDat = sfDat.connect(host, port, username, password, null);
            boolean result = sfDat.upload(serverPath, filePath, sftpDat);

            if (result) {
                log.info("上传文件:{}成功,耗时", filePath);
            } else {
            }
        } catch (Exception e) {
            log.info("上传文件失败--fileName：{}......", filePath);
        } finally {
            if (sfDat != null) {
                try {
                    sfDat.disconnect(sftpDat);
                } catch (Exception e) {
                    log.info("关闭SFTP异常！！！");
                }
            }
        }
    }

    /**
     * 下载数据并更新本地缓存
     */
    @Override
    public void downLoadAndUpdateCache() {
        log.info("downLoadAndUpdateCache start...");
        try {
            String bitMapFile = downloadFile(PlanPreBitMapCache.PLAN_PRE_BITMAPS_FILE_NAME);
            if (StrUtil.isEmpty(bitMapFile)) {
                log.warn("文件:{}不存在或者下载失败,不进行产品偏好数据加载(更新)....");
                return;
            }
            Object bitMapObj = deserialization(bitMapFile);
            PlanPreBitMapCache.tmpBitMaps = (Map<String, Map<String, IBitMap>>) bitMapObj;
            if (CollectionUtil.isNotEmpty(PlanPreBitMapCache.tmpBitMaps)) {
                PlanPreBitMapCache.planPreBitMaps.clear();
                BeanUtil.copyProperties(PlanPreBitMapCache.tmpBitMaps,PlanPreBitMapCache.planPreBitMaps);
                PlanPreBitMapCache.tmpBitMaps.clear();
            }

            String scoreFiile = downloadFile(PlanPreBitMapCache.PLAN_PRE_SCORE_FILE_NAME);
            Object scoreObj = deserialization(scoreFiile);
            PlanPreBitMapCache.tmpScores = (Map<String, Map<String, Double>>) scoreObj;
            if (CollectionUtil.isNotEmpty(PlanPreBitMapCache.tmpScores)) {
                PlanPreBitMapCache.planPreScores.clear();
                BeanUtil.copyProperties(PlanPreBitMapCache.tmpScores,PlanPreBitMapCache.planPreScores);
                PlanPreBitMapCache.tmpScores.clear();
            }
            log.info("downLoadAndUpdateCache finished...");
        } catch (Exception e) {
            log.error("产品偏好数据加载(更新)失败.", e);
        }

    }

    /**
     * 根据bitmap文件反序列化生成 bitmap对象
     *
     * @param filePath
     * @return
     */
    public Object deserialization(String filePath) {
        ObjectInputStream ois = null;
        try {
            //反序列化bitmap
            ois = new ObjectInputStream(new FileInputStream(filePath));
            Object readObject = ois.readObject();
            return readObject;
        } catch (Exception e) {
            log.error("反序列化生成对象异常. file = {}", filePath, e);
        } finally {
            IOUtils.closeQuietly(ois);
        }
        return null;
    }


    /**
     * @param obj
     * @param filePath
     * @Desc
     */
    public void writeBitMapFile(Object obj, String filePath) {
        ObjectOutputStream oos = null;
        try {
            File file = new File(filePath);
            // 获取文件夹路径
            File parentFile = file.getParentFile();
            // 不存在则创建
            if (!parentFile.exists()) {
                boolean created = parentFile.mkdirs();
                if (!created) {
                    throw new Exception("文件目录创建失败:" + parentFile.getAbsolutePath());
                }
            }
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(obj);
        } catch (Exception e) {
            log.error("生成对象文件{}出错", filePath, e);
        } finally {
            IOUtils.closeQuietly(oos);
        }
    }
}
