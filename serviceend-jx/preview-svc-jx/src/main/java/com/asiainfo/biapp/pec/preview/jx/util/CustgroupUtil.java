package com.asiainfo.biapp.pec.preview.jx.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.service.impl.BitMapRoaringImp;
import com.asiainfo.biapp.pec.common.jx.util.BitmapCacheUtil;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.preview.jx.custgroup.model.McdCustgroupDef;
import com.asiainfo.biapp.pec.preview.jx.custgroup.service.McdCustgroupDefService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author mamp
 * @date 2022/9/22
 */
@Component
@Slf4j
@RefreshScope
public class CustgroupUtil {

    @Autowired
    private McdCustgroupDefService custgroupDefService;
    /**
     * bitmap文件目录
     */
    @Value("${pec.preview.custgroup.bitmapFileDir:D:\\CUST_BITMAP\\}")
    private String custgroupBitmapDir;


    @Value("${mcd.common.custFile.host}")
    private String custFileServerHost;
    @Value("${mcd.common.custFile.port}")
    private String custFileServerPort;
    @Value("${mcd.common.custFile.user}")
    private String custFileFtpUsername;
    @Value("${mcd.common.custFile.password}")
    private String custFileFtpPassword;
    @Value("${mcd.common.custFile.serverPath}")
    private String custFileServerPath;
    @Value("${mcd.common.custFile.encoding}")
    private String custFileFtpEncoding;
    @Value("${mcd.common.custFile.locaPath}")
    private String localPath;


    /**
     * @param custgroupId 客户群ID
     * @param channelId   偏好渠道ID，如果渠道ID为空，则返回原始客户群
     * @return
     */
    public IBitMap getCustgroupBitmap(String custgroupId, String channelId) throws Exception {
        // 客户群Bitmap
        IBitMap bitMap = new BitMapRoaringImp(true);
        // 获取客户群最新数据日期
        McdCustgroupDef custgroupDef = getCustgroupDatadate(custgroupId);
        if (null == custgroupDef) {
            return bitMap;
        }
        String custBitmapFilePath = StrUtil.format("{}MCD_GROUP_{}_{}.bitmap", custgroupBitmapDir, custgroupId, custgroupDef.getDataDate());

        if (!FileUtil.exist(custBitmapFilePath)) {
            // bitmap不存在，重新生成
            if (downloadCustFile(custgroupDef.getFileName())) {
                bitMap = createCustBitMap(custgroupDef.getCustomGroupId(), String.valueOf(custgroupDef.getDataDate()), custgroupDef.getFileName());
            } else {
                throw new Exception("下载客户群清单文件异常！");
            }
        } else {
            // 根据bitmap文件反序列化生成 bitmap对象
            bitMap = deserialization(custBitmapFilePath);
        }
        if (StrUtil.isEmpty(channelId)) {
            // 返回客户群bitmap
            return bitMap;
        }
        //IBitMap chnPreBitmap = ChnPreBitMapCache.firstChnPreBitMaps.get(channelId);
        IBitMap chnPreBitmap = BitmapCacheUtil.getChnPreBitmap(channelId);
        if (null == chnPreBitmap) {
            return bitMap;
        }
        // 客户bitmap和偏好交集后返回
        return bitMap.andWithModify(chnPreBitmap);

    }

    /**
     * 获取客户群信息
     *
     * @param custgroupId 客户群ID
     * @return
     */
    private McdCustgroupDef getCustgroupDatadate(String custgroupId) {
        QueryWrapper<McdCustgroupDef> wrapper = new QueryWrapper();
        wrapper.lambda().eq(McdCustgroupDef::getCustomGroupId, custgroupId);
        return custgroupDefService.getOne(wrapper);

    }

    /**
     * 获取免打扰的bit
     *
     * @param channelId
     * @return
     */
    public IBitMap getBwlBitMapByChnId(String channelId) {
        IBitMap bitMap = BitmapCacheUtil.getBwlBitmap(channelId);
        if (null == bitMap) {
            bitMap = new BitMapRoaringImp(true);
        }
        return bitMap;
    }

    /**
     * 根据bitmap文件反序列化生成 bitmap对象
     *
     * @param filePath
     * @return
     */
    public static BitMapRoaringImp deserialization(String filePath) {
        ObjectInputStream ois = null;
        try {
            //反序列化bitmap
            ois = new ObjectInputStream(new FileInputStream(filePath));
            Object readObject = ois.readObject();
            return (BitMapRoaringImp) readObject;
        } catch (Exception e) {
            log.error("反序列化生成Bitmap异常. file = {}", filePath, e);
        } finally {
            IOUtils.closeQuietly(ois);
        }
        return null;
    }

    public static void main(String[] args) {
        BitMapRoaringImp map = new BitMapRoaringImp(false);
        map.add(13429001000L);
        map.add(13429001001L);
        map.add(13429001002L);

        System.out.println(map.getCount());
        BitMapRoaringImp map1 = new BitMapRoaringImp(true);
        map1.add(13429001000L);
        map1.add(13429001001L);

        map.andNotWithModify(map1);

        System.out.println(map.getCount());

    }

    /**
     * @param bitMap
     * @param filePath
     * @Desc
     */
    public void writeBitMapFile(IBitMap bitMap, String filePath) {
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
            IBitMap or = new BitMapRoaringImp(true).orWithModify(bitMap);
            oos.writeObject(or);
        } catch (Exception e) {
            log.error("生成bitmap文件{}出错", filePath, e);
        } finally {
            IOUtils.closeQuietly(oos);
        }
    }


    /**
     * 下载客户群清单文件
     *
     * @param fileName
     * @return
     */
    public boolean downloadCustFile(String fileName) {
        String fileNameNoSuffix = fileName.substring(0, fileName.lastIndexOf("."));
        if (org.springframework.util.StringUtils.isEmpty(localPath) || org.springframework.util.StringUtils.isEmpty(custFileFtpUsername) || org.springframework.util.StringUtils.isEmpty(custFileFtpPassword)
                || org.springframework.util.StringUtils.isEmpty(custFileServerHost) || org.springframework.util.StringUtils.isEmpty(custFileServerPort) || org.springframework.util.StringUtils.isEmpty(custFileServerPath)) {
            log.error("客群清单文件{}接口机配置错误，直接返回......", fileName);
            return false;
        }
        File localCustFile = FileUtil.file(localPath + fileName);
        if (localCustFile.exists()) {
            log.info("客群清单文件{}已经下载，删除重新下载......", fileName);
            localCustFile.deleteOnExit();
        }
        //文件下载分布式锁
        String lockId = RedisUtils.getRedisLock(fileName, 10 * 60);
        if (org.apache.commons.lang.StringUtils.isEmpty(lockId)) {
            log.info("未能获取到redis 加载锁：({})，其它线程正在下载此文件，直接返回......", fileName);
            return false;
        }
        SftpUtils sfDat = null;
        ChannelSftp sftpDat = null;
        try {
            sfDat = new SftpUtils();
            sftpDat = sfDat.connect(custFileServerHost, Integer.parseInt(custFileServerPort), custFileFtpUsername, custFileFtpPassword, custFileFtpEncoding);
            boolean chkIsTrue = sfDat.download(custFileServerPath, custFileServerPath + fileNameNoSuffix + ".CHK",
                    localPath + fileNameNoSuffix + ".CHK", sftpDat);
            boolean txtIsTrue = sfDat.download(custFileServerPath, custFileServerPath + fileName, localPath + fileName, sftpDat);
            if (!txtIsTrue || !chkIsTrue) {
                log.info("客户群校验文件/清单文件下载异常--fileName..{}", fileName);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("下载客群文件失败--fileName：{}......", fileName, e);
            throw e;
        } finally {
            if (RedisUtils.releaseRedisLock(fileName, lockId)) {
                log.info("成功释放分布式redis锁:{},lockId:({}),集群的其他服务器将尝试下载该文件......", fileName, lockId);
            } else {
                log.error("未能成功释放分布式锁:{},lockId:({}),该异常会导致本周期的该文件无法被集群其他机器下载，请手动删除该锁......", fileName, lockId);
            }
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
     * 生成客户群 bitMap
     *
     * @param custGroupId  客户群编码
     * @param dataDate     数据日期
     * @param custFileName 清单文件名称 MCD_GROUP_{客户群编码}_{数据日期}.txt
     * @return
     */
    public IBitMap createCustBitMap(String custGroupId, String dataDate, String custFileName) {
        String custFile = StrUtil.format("{}{}", localPath, custFileName);
        log.info("开始生成bitmap,清单文件路径:{}", custFile);
        IBitMap bitMap = new BitMapRoaringImp(false);
        try {
            LineIterator li = FileUtils.lineIterator(FileUtil.file(custFile));
            String line;
            while (li.hasNext()) {
                line = li.nextLine();
                // 第一列是电话号码
                bitMap.add(Long.valueOf(line.split(",")[0]));
            }
            log.info("bitmap已生成,count:{}", bitMap.getCount());

            // bitMap文件不是必须的
            log.info("开始生成bitmap文件");
            String custBitMapFile = StrUtil.format("{}MCD_GROUP_{}_{}.bitmap", custgroupBitmapDir, custGroupId, dataDate);
            writeBitMapFile(bitMap, custBitMapFile);
            log.info("bitmap文件已生成,文件路径:{}", custBitMapFile);
        } catch (IOException e) {
            log.error("createCustBitMap error:custGroupId = {}, dataDate = {} ", custGroupId, dataDate, e);
        }
        log.info("bitmap size = {} ", bitMap.getCount());
        return bitMap;
    }

}
