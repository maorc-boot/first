package com.asiainfo.biapp.pec.preview.jx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.service.impl.BitMapRoaringImp;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.preview.jx.enums.CampUserCountTaskStatusEnum;
import com.asiainfo.biapp.pec.preview.jx.mapper.McdCampUserCountTaskMapper;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampUserCountResult;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampUserCountTask;
import com.asiainfo.biapp.pec.preview.jx.query.McdCampUserCountQuery;
import com.asiainfo.biapp.pec.preview.jx.service.McdCampUserCountResultService;
import com.asiainfo.biapp.pec.preview.jx.service.McdCampUserCountTaskService;
import com.asiainfo.biapp.pec.preview.jx.util.CustgroupUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mamp
 * @description 针对表【mcd_camp_user_count_task(IOP策略用户统计任务表)】的数据库操作Service实现
 * @createDate 2024-07-18 15:47:18
 */
@Service
@Slf4j
public class McdCampUserCountTaskServiceImpl extends ServiceImpl<McdCampUserCountTaskMapper, McdCampUserCountTask> implements McdCampUserCountTaskService {

    @Value("${pec.preview.highcust.sftp.host:10.19.92.24}")
    private String host;
    @Value("${pec.preview.highcust.sftp.port:22022}")
    private int port;
    @Value("${pec.preview.highcust.sftp.username:iop-dev}")
    private String username;
    @Value("${pec.preview.highcust.sftp.password:IOPqwe!24&0408}")
    private String password;
    @Value("${pec.preview.highcust.sftp.serverPath:/home/iop-dev/sftp/smartscript/}")
    private String serverPath;
    @Value("${pec.preview.highcust.localFilePath:D:\\iop7\\smartscript\\}")
    private String localFilePath;
    @Value("${pec.preview.highcust.highCustFileName: zgd_group_{}.txt}")
    private String highCustFileName;

    /**
     * 分页查询任务，每页数量
     */
    @Value("${pec.preview.userCount.taskPageSize: 10}")
    private Integer taskPageSize;

    /**
     * 覆盖数量最大级别
     */
    @Value("${pec.preview.userCount.maxRank: 5}")
    private String maxRank;

    /**
     * 锁超时时间,默认5小时
     */
    @Value("${pec.preview.userCount.lockExpireSecond: 18000}")
    private Long lockExpireSecond;

    /**
     * 最大统计级别
     */
    private static int MAX_RANK = 5;

    /**
     * 是否查询当前所有正在执行中的策略
     */
    private static int ONE_RANK = 1;

    /**
     * 汇总数据KEY
     */
    private static String TOTAL_KEY = "-1";

    /**
     * 分布式事务锁
     */
    private static String REDIS_LOCK_KEY = "IOP_CAMP_USER_COUNT_TASK_LOCK";

    /**
     * 按照渠道缓存统计数据
     */
    private Map<String, List<IBitMap>> chnBitMapListMap = new ConcurrentHashMap();

    private IBitMap highEndUserBitMap = new BitMapRoaringImp(true);

    @Resource
    private CustgroupUtil custgroupUtil;

    @Resource
    private McdCampUserCountResultService mcdCampUserCountResultService;

    @Resource
    private McdCampUserCountTaskMapper mcdCampUserCountTaskMapper;

    /**
     * 开启策略用户数统计任务
     */
    public void startTask(McdCampUserCountQuery query) {
        String redisLock = RedisUtils.getRedisLock(REDIS_LOCK_KEY, lockExpireSecond);
        if (StrUtil.isEmpty(redisLock)) {
            log.error("获取redis锁:{}失败", REDIS_LOCK_KEY);
            return;
        }
        log.info("获取redis锁:{}成功，lockValue:{}", REDIS_LOCK_KEY, redisLock);
        try {
            countCampUser(query);
        } catch (Exception e) {
            log.error("策略用户数统计任务执行失败:", e);
        } finally {
            boolean releaseRedisLock = RedisUtils.releaseRedisLock(REDIS_LOCK_KEY, redisLock);
            if (releaseRedisLock) {
                log.info("redis锁:{},value:{} 释放成功成功", REDIS_LOCK_KEY, redisLock);
            } else {
                log.error("redis锁:{},value:{} 释放成失败,请手动删除或者锁超时后再使用", REDIS_LOCK_KEY, redisLock);
            }
        }
    }

    /**
     * IOP策略用户统计
     */
    private void countCampUser(McdCampUserCountQuery query) {
        // 0. 数据初始化
        initData();
        // 1. 生成需要统计的策略数据(mcd_camp_channel_list表中状态为 54的数据)
        if (NumberUtil.equals(query.getIsCreateTask(), ONE_RANK)) {
            mcdCampUserCountTaskMapper.insertUserCountTask();
        }
        // 2. 查询待执行任务列表数量
        LambdaQueryWrapper<McdCampUserCountTask> wrapper = getQueryWrapper();
        // 待执行任务列表
        Integer count = this.count(wrapper);
        log.info("共查询到执行中策略(子策略)任务:{}个", count);
        if (count <= 0) {
            return;
        }
        // 3. 分页查询并执行任务
        int taskIndex = 0;
        List<McdCampUserCountTask> pageList = selectTaskByPage(wrapper);
        while (CollectionUtil.isNotEmpty(pageList)) {
            // 分页执行任务
            boolean execResult = execTaskByPage(pageList, taskIndex, count);
            if (!execResult) {
                break;
            }
            taskIndex += pageList.size();
            // 执行完一页任务后,查询下一页
            pageList = selectTaskByPage(wrapper);
        }
        // 计算完成,保存计算结果
        saveCountResult();
    }


    /**
     * 数据初始化
     */
    private void initData() {
        // 初始化中高端用户 BtMap
        initHighEndUserBitMap();
        // 清空历史数据
        chnBitMapListMap.clear();
    }


    /**
     * 获取查询条件
     *
     * @return
     */
    private static LambdaQueryWrapper<McdCampUserCountTask> getQueryWrapper() {
        LambdaQueryWrapper<McdCampUserCountTask> wrapper = new LambdaQueryWrapper<>();
        // 开始时间
        Date startTime = DateUtil.beginOfDay(DateUtil.date()).toSqlDate();
        // 结束时间
        Date endTime = DateUtil.beginOfDay(DateUtil.tomorrow()).toSqlDate();
        // 只查询执行中
        wrapper.eq(McdCampUserCountTask::getStatus, CampUserCountTaskStatusEnum.UNDO.getCode())
                // 只查询当前生成的
                .between(McdCampUserCountTask::getCreateTime, startTime, endTime)
                // 先按照创建时间排序
                .orderByAsc(McdCampUserCountTask::getCreateTime)
                // 再按照子活动ID排序
                .orderByAsc(McdCampUserCountTask::getCampsegId);
        return wrapper;
    }


    /**
     * 分页查询待执行任务
     *
     * @param wrapper
     * @return
     */
    List<McdCampUserCountTask> selectTaskByPage(LambdaQueryWrapper<McdCampUserCountTask> wrapper) {
        // 每次查询符合条件的N条数据
        Page<McdCampUserCountTask> queryPage = new Page<>(1, taskPageSize);
        Page<McdCampUserCountTask> pageResult = page(queryPage, wrapper);
        return pageResult.getRecords();
    }

    /**
     * 分页执行任务
     *
     * @param pageList  分页数据
     * @param taskIndex 当前执行任务编号
     * @param count     任务总数
     */
    private boolean execTaskByPage(List<McdCampUserCountTask> pageList, int taskIndex, Integer count) {
        for (McdCampUserCountTask task : pageList) {
            taskIndex++;
            if (taskIndex > count) {
                log.error("任务执行期间有新的任务产生，为防止重复执行任务，退出执行");
                return false;
            }
            log.info("开始执行第{}个任务,taskId:{}", taskIndex,task.getId());
            // 按渠道计算数据
            countByCampseg(task);
            // 计算全渠道数据
            McdCampUserCountTask totalTask = BeanUtil.copyProperties(task, McdCampUserCountTask.class);
            totalTask.setChannelId(TOTAL_KEY);
            countByCampseg(totalTask);
            log.info("第{}个任务执行完成,taskId:{}", taskIndex,task.getId());
        }
        return true;
    }


    /**
     * 单活动计算
     *
     * @param task
     */
    private void countByCampseg(McdCampUserCountTask task) {
        // 渠道ID
        String channelId = task.getChannelId();
        // 客户群ID
        String custgroupId = task.getCustgroupId();
        // 活动ID
        String campsegId = task.getCampsegId();
        try {
            // 更新任务状态为执行中
            updateTaskStatus(task, CampUserCountTaskStatusEnum.RUNNING);
            long start = System.currentTimeMillis();
            log.info("活动:{},渠道:{},客户群:{}开始计算", campsegId, channelId, custgroupId);
            // 当前渠道的的 BitMapList
            List<IBitMap> bitMapListByChannel = chnBitMapListMap.get(channelId);
            // 如果渠道List为 null ,初始化
            if (bitMapListByChannel == null) {
                bitMapListByChannel = getBitMapList();
                chnBitMapListMap.put(channelId, bitMapListByChannel);
            }
            // 当前客户群的bitmap
            IBitMap custgroupBitmap = custgroupUtil.getCustgroupBitmap(custgroupId, null);
            // 计算渠道数据
            calculate(custgroupBitmap, bitMapListByChannel);
            // 更新任务状态为执行成功
            updateTaskStatus(task, CampUserCountTaskStatusEnum.SUCCESS);
            // 耗时
            long duration = System.currentTimeMillis() - start;
            log.info("活动:{},渠道:{},客户群:{} 计算完成,耗时:{}", campsegId, channelId, custgroupId, duration);
        } catch (Exception e) {
            log.error("活动:{},渠道:{},客户群:{} 计算失败:", campsegId, channelId, custgroupId, e);
            // 更新任务状态为执行失败
            updateTaskStatus(task, CampUserCountTaskStatusEnum.ERROR);
        }
    }

    /**
     * 更新任务状态
     *
     * @param task
     * @param status
     */
    private void updateTaskStatus(McdCampUserCountTask task, CampUserCountTaskStatusEnum status) {
        // 如果是汇总数据，没有具体任务不需要更新状态
        if (TOTAL_KEY.equals(task.getChannelId())) {
            return;
        }
        LambdaUpdateWrapper<McdCampUserCountTask> updateWrapper = new LambdaUpdateWrapper<>();
        // 根据Id更新
        updateWrapper.eq(McdCampUserCountTask::getId, task.getId())
                // 更新状态
                .set(McdCampUserCountTask::getStatus, status.getCode());
        this.update(updateWrapper);
    }


    /**
     * 更新每个覆盖次数 bitmap数据
     * 以下为逻辑说明示例
     * A1 A2 A3 A4 A5
     * <p>
     * A1 && B =  B1
     * A2 && B =  B2
     * A3 && B =  B3
     * A4 && B =  B4
     * A5 && B =  B5
     * <p>
     * B - B1 -B2 -B3 -B4 -B5 = B0
     * <p>
     * A1 = A1 -B1 +B0
     * A2 = A2 -B2 +B1
     * A3 = A3 -B3 +B2
     * A4 = A4 -B4 +B3
     * A5 = A5 + B4
     * <p>
     * listA.get(1): 表示覆盖一次的用户 Bitmap
     * listA.get(2): 表示覆盖二次的用户 Bitmap
     * ...
     * listA.get(n): 表示覆盖n次的用户 Bitmap
     */
    public void calculate(IBitMap custBitMap, List<IBitMap> listA) {
        // listB 保存临时的计算结果
        List<IBitMap> listB = getBitMapList();
        // 先用listA中的每个BitMap与目标BitMap(活动客户群的bitmap)求交集
        // 并将结果保存 listA中对应 index(i)的元素中，注意i 从1开始
        for (int i = 1; i <= MAX_RANK; i++) {
            IBitMap bitmapA = listA.get(i);
            IBitMap bitmapB = bitmapA.andWithoutModify(custBitMap);
            listB.set(i, bitmapB);
        }
        // 将目标Bitmap数据复制一份到 bitmapB0
        IBitMap bitmapB0 = new BitMapRoaringImp(true).orWithModify(custBitMap);
        // B0 = B - B1 -B2 -..-Bn ，n的最大值为  MAX_NUM
        for (int i = 1; i <= MAX_RANK; i++) {
            bitmapB0 = bitmapB0.andNotWithModify(listB.get(i));
        }
        // 将B0 设置为 listB的第一个元素
        listB.set(0, bitmapB0);

        // 除了 listA.get(MAX_NUM) ,其它的 值为 A[i] = A[i] -B[i] +B[i-1]
        // A[MAX_NUM]  = A[MAX_NUM] +A[MAX_NUM-1]
        for (int i = 1; i <= MAX_RANK; i++) {
            IBitMap bitmapA = listA.get(i);
            if (i == MAX_RANK) {
                bitmapA.orWithModify(listB.get(i - 1));
            }
            bitmapA.andNotWithModify(listB.get(i)).orWithModify(listB.get(i - 1));
        }
    }

    /**
     * 获取包含 MAX_RANK +1 个元素的  List<IBitMap>
     *
     * @return
     */
    private List<IBitMap> getBitMapList() {
        List<IBitMap> list = new ArrayList<>(MAX_RANK + 1);
        for (int i = 0; i <= MAX_RANK; i++) {
            list.add(new BitMapRoaringImp(true));
        }
        return list;
    }

    /**
     * 保存计算结果
     */
    private void saveCountResult() {
        log.info("开始保存计算结果...");
        if (CollectionUtil.isEmpty(chnBitMapListMap)) {
            log.warn("chnBitMapListMap 为空");
            return;
        }
        String dataDate = DatePattern.PURE_DATE_FORMAT.format(DateUtil.date());
        List<McdCampUserCountResult> resultList = new ArrayList<>();
        for (Map.Entry<String, List<IBitMap>> entry : chnBitMapListMap.entrySet()) {
            addData2ResultList(entry, dataDate, resultList);
        }
        if (CollectionUtil.isEmpty(resultList)) {
            log.warn("【IOP策略用户统计】计算结果为空,dataDate:{}");
            return;
        }
        mcdCampUserCountResultService.saveBatch(resultList);
        log.info("【IOP策略用户统计】计算结果已经保存，共{}条,dataDate:{}", resultList.size(), dataDate);
    }

    /**
     * 添加结果数据到结果集合
     *
     * @param entry
     * @param dataDate
     * @param resultList
     */
    private void addData2ResultList(Map.Entry<String, List<IBitMap>> entry, String dataDate, List<McdCampUserCountResult> resultList) {
        String channelId = entry.getKey();
        List<IBitMap> bitMaps = entry.getValue();
        for (int i = 1; i <= MAX_RANK; i++) {
            IBitMap bitMapByRank = bitMaps.get(i);
            McdCampUserCountResult result = new McdCampUserCountResult();
            result.setChannelId(channelId)
                    // 用户级别
                    .setCoverUserRank(i)
                    // 用户数量
                    .setUserNum(bitMapByRank.getCount())
                    // 中高端用户数量
                    .setHighEndUserNum(bitMapByRank.andWithoutModify(highEndUserBitMap).getCount())
                    // 数据日期
                    .setDataDate(dataDate);
            resultList.add(result);
        }
    }

    /**
     * 初始化中高端用户 BtMap
     */
    private void initHighEndUserBitMap() {
        highEndUserBitMap = new BitMapRoaringImp(true);
        // 下载中高端用户清单
        String filePathName = downLoadHighUserFile();
        File highEndUserListFile = new File(filePathName);
        // 遍历文件，放入bitmap
        LineIterator li = null;
        try {
            li = FileUtils.lineIterator(highEndUserListFile);
            String line;
            while (li.hasNext()) {
                line = li.nextLine();
                Long phoneNo = strToLong(line);
                if (null == phoneNo) {
                    continue;
                }
                highEndUserBitMap.add(phoneNo);
            }
        } catch (IOException e) {
            log.error("【IOP策略用户统计】生成中高端用户bigMap异常:", e);
            throw new RuntimeException(e);
        } finally {
            if (li != null) {
                li.close();
            }
        }
    }

    /**
     * 下载中高端客户群文件，返回文件全路径
     *
     * @return
     */
    private String downLoadHighUserFile() {
        SftpUtils sftpUtils = null;
        ChannelSftp channelSftp = null;
        String newHighCustFileName = StrUtil.format(highCustFileName, DateUtil.format(DateUtil.offset(DateUtil.date(), DateField.DAY_OF_MONTH, -1), DatePattern.PURE_DATE_FORMAT));
        ;
        try {
            sftpUtils = new SftpUtils();
            channelSftp = sftpUtils.connect(host, port, username, password);
            log.info("【IOP策略用户统计】sftp链接成功,待下载文件名称：{}，远程存放路径：{}，本地存放路径：{}", newHighCustFileName, serverPath, localFilePath);
            boolean result = sftpUtils.download(serverPath, newHighCustFileName, localFilePath, channelSftp);
            if (!result) {
                log.info("下载【IOP策略用户统计】中高端客户清单文件失败:{},", serverPath + newHighCustFileName);
            }
        } catch (Exception e) {
            log.error("下载【IOP策略用户统计】中高端客户清单文件失败,异常信息为:{}", e);
        } finally {
            if (sftpUtils != null && channelSftp != null) {
                try {
                    sftpUtils.disconnect(channelSftp);
                } catch (JSchException e) {
                    log.error("【IOP策略用户统计】下载中高端用户sftp连接关闭异常", e);
                }
            }
        }
        return localFilePath + newHighCustFileName;
    }

    /**
     * String 转 Long
     *
     * @param str
     * @return
     */
    private Long strToLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            log.error("【IOP策略用户统计】strToLong error: {}", str, e);
        }
        return null;
    }
}




