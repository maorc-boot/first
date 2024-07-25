package com.asiainfo.biapp.pec.plan.jx.dna.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.constant.RedisDicKey;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.dao.McdCampTaskDao;
import com.asiainfo.biapp.pec.plan.dao.McdCampTaskDateDao;
import com.asiainfo.biapp.pec.plan.jx.dna.constant.ConstantDNA;
import com.asiainfo.biapp.pec.plan.jx.dna.dao.IDnaCustgroupDayMonthUpdateDao;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDNACustgroupUpdateTaskService;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDNACustomGroupService;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDnaCustgroupDayMonthUpdateService;
import com.asiainfo.biapp.pec.plan.jx.dna.vo.DNACustgroupUpdateTask;
import com.asiainfo.biapp.pec.plan.jx.dna.vo.DnaCustgroupUpdateVo;
import com.asiainfo.biapp.pec.plan.model.McdCampTask;
import com.asiainfo.biapp.pec.plan.model.McdCampTaskDate;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupDef;
import com.asiainfo.biapp.pec.plan.service.IMcdCampChannelListService;
import com.asiainfo.biapp.pec.plan.service.IMcdCampTaskDateService;
import com.asiainfo.biapp.pec.plan.service.IMcdCustgroupDefService;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * description: DNA客群更新调度(日周期/月周期)service实现
 *
 * @author: lvchaochao
 * @date: 2023/12/18
 */
@Service
@Slf4j
@RefreshScope
public class DnaCustgroupDayMonthUpdateServiceImpl implements IDnaCustgroupDayMonthUpdateService {

    @Autowired
    private IDnaCustgroupDayMonthUpdateDao dnaCustgroupDayMonthUpdateDao;

    @Autowired
    private IDNACustomGroupService idnaCustomGroupService;

    @Resource
    private IMcdCustgroupDefService custgroupDefService;

    @Autowired
    private IMcdCampChannelListService mcdCampChannelListService;

    @Resource
    private McdCampTaskDao campTaskDao;

    @Resource
    private McdCampTaskDateDao campTaskDateDao;

    @Resource
    private IMcdCampTaskDateService campTaskDateService;

    @Autowired
    private IDNACustgroupUpdateTaskService idnaCustgroupUpdateTaskService;

    /**
     * 查询待更新周期客群批次大小
     */
    @Value("${dna.custGroup.updateBatchSize:2}")
    private int updateBatchSize;

    /**
     * 日周期更新任务核心线程数
     */
    @Value("${dna.custGroup.dayCustUpdateThreadNum:5}")
    private int dayCustUpdateThreadNum;

    /**
     * 月周期更新任务核心线程数
     */
    @Value("${dna.custGroup.monthCustUpdateThreadNum:5}")
    private int monthCustUpdateThreadNum;

    private static ExecutorService dayCustUpdateScheduleJobExecutor;

    private static ExecutorService monthCustUpdateScheduleJobExecutor;

    @PostConstruct
    public void init() {
        dayCustUpdateScheduleJobExecutor = Executors.newFixedThreadPool(dayCustUpdateThreadNum);
        monthCustUpdateScheduleJobExecutor = Executors.newFixedThreadPool(monthCustUpdateThreadNum);
    }

    /**
     * DNA客群更新调度(日周期/月周期)任务
     * 1. 调用dna接口1.4 生成客群清单文件
     * 2. 下载清单文件到本地(plan-svc服务本地)
     * 3. 将获取到的清单文件按照coc文件格式命名
     * 4. 按照coc格式生成校验文件
     * 5. 上传清单文件&校验文件到coc清单文件所在目录
     * 6. 更新mcd_custgroup_def客群相关信息
     * 7. mcd_camp_task_date插数据
     * 8. 更新DNA_CUSTGROUP_UPDATE_TASK任务表状态为完成
     *
     * @param request 入参 type: day-日周期 month-月周期
     */
    @Override
    public void dnaCustgroupDayMonthUpdateTask(JSONObject request) {
        log.info("DNA客群更新调度任务开始，入参={}", JSONUtil.toJsonStr(request));
        String type = request.getStr("type");
        long start = System.currentTimeMillis();
        // a. 日周期
        if (ConstantDNA.CUSTGROUP_CYCLE.equalsIgnoreCase(type)) {
            delDnaCustgroupDayUpdate(start);
        } else {
            // b. 月周期
            delDnaCustgroupMonthUpdate(start);
        }
    }

    /**
     * 处理dna客群更新日周期逻辑
     *
     * @return {@link List}<{@link DNACustgroupUpdateTask}>
     */
    private void delDnaCustgroupDayUpdate(long start) {
        try {
            // 1. 查询当天的最新数据日期对应的任务
            LambdaQueryWrapper<DNACustgroupUpdateTask> wrapper = Wrappers.<DNACustgroupUpdateTask>query().lambda()
                    .eq(DNACustgroupUpdateTask::getUpdateCycle, ConstantDNA.CUSTGROUP_DAY_CYCLE)
                    .eq(DNACustgroupUpdateTask::getDataDate, Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd")))
                    .eq(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_WAITING);
            List<DNACustgroupUpdateTask> todayDayTask = idnaCustgroupUpdateTaskService.list(wrapper);
            log.info("delDnaCustgroupDayUpdate-->日周期：当天的最新数据日期对应的任务={}条", todayDayTask.size());
            // 通知接口插入任务的数据日期改为当天，且最新数据日期的任务不会重复插入
            // if (CollectionUtil.isNotEmpty(todayDayTask)) {
            //     // 1.1 更新最新数据日期对应的任务状态为已完成
            //     List<String> taskIds = todayDayTask.stream().map(DNACustgroupUpdateTask::getTaskId).collect(Collectors.toList());
            //     idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
            //             .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_COMPLETED)
            //             .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
            //             .in(DNACustgroupUpdateTask::getTaskId, taskIds));
            // }
            // 2. 查询数据日期为前一天待更新的客群总数
            // Integer dayPre = dayPre(DateUtil.parse(DateUtil.format(new Date(), "yyyyMMdd"), "yyyyMMdd"), -1);
            // LambdaQueryWrapper<DNACustgroupUpdateTask> wrapper2 = Wrappers.<DNACustgroupUpdateTask>query().lambda()
            //         .eq(DNACustgroupUpdateTask::getUpdateCycle, ConstantDNA.CUSTGROUP_DAY_CYCLE)
            //         .eq(DNACustgroupUpdateTask::getDataDate, dayPre)
            //         .eq(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_WAITING);
            // .last("limit " + updateBatchSize);
            // List<DNACustgroupUpdateTask> day = idnaCustgroupUpdateTaskService.list(wrapper2);
            if (CollectionUtil.isEmpty(todayDayTask)) {
                log.warn("delDnaCustgroupDayUpdate-->日周期：没有待更新的客群");
                return;
            }
            // log.info("delDnaCustgroupDayUpdate-->日周期：待更新的客群总数={}条", day.size());
            // 2. 批量更新DNA_CUSTGROUP_UPDATE_TASK任务表状态为中间状态--防止任务被重复查询、重复执行
            List<String> taskIds = todayDayTask.stream().map(DNACustgroupUpdateTask::getTaskId).collect(Collectors.toList());
            idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                    .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_MIDDLE)
                    .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                    .in(DNACustgroupUpdateTask::getTaskId, taskIds));
            // 3. 根据 客群id_数据日期 分组==》不通活动使用同一个客群&数据日期一致时，只需要其中一个客群请求接口获取清单文件即可
            Map<String, List<DNACustgroupUpdateTask>> dayGroup = todayDayTask.stream().collect(
                    Collectors.groupingBy(i -> i.getCustomGroupId() + StrUtil.UNDERLINE + i.getDataDate(), Collectors.toList()));
            log.info("delDnaCustgroupDayUpdate-->日周期：根据客群id_数据日期分组={}, 分组后客群={}个", JSONUtil.toJsonStr(dayGroup), dayGroup.keySet().size());
            // 4. 根据分组后的客群 多线程异步去处理每个客群对应的更新任务逻辑
            final CountDownLatch countDownLatch_ = new CountDownLatch(dayGroup.keySet().size());
            for (Map.Entry<String, List<DNACustgroupUpdateTask>> entry : dayGroup.entrySet()) {
                dayCustUpdateScheduleJobExecutor.execute(new DayCustUpdateThread(entry, countDownLatch_));
            }
            // 线程没处理完，此处会等待所有线程操作处理完才继续执行下一步！
            countDownLatch_.await();
            log.info("delDnaCustgroupDayUpdate-->日周期：dna客群日周期更新任务结束，共更新={}条，耗时={}秒", todayDayTask.size(), (System.currentTimeMillis() - start) / 1000);
        } catch (InterruptedException e) {
            log.error("delDnaCustgroupDayUpdate-->日周期：客群任务执行异常：", e);
        }
    }

    /**
     * 日周期客群更新线程类
     *
     * @author lvcc
     * @date 2024/01/05
     */
    class DayCustUpdateThread implements Runnable {

        private Map.Entry<String, List<DNACustgroupUpdateTask>> entry;
        private CountDownLatch countDownLatch_;

        public DayCustUpdateThread (Map.Entry<String, List<DNACustgroupUpdateTask>> entry, CountDownLatch countDownLatch_) {
            this.entry = entry;
            this.countDownLatch_ = countDownLatch_;
        }

        @Override
        public void run() {
            // for (Map.Entry<String, List<DNACustgroupUpdateTask>> entry : dayGroup.entrySet()) {
                String key = entry.getKey();
                List<DNACustgroupUpdateTask> val = entry.getValue();
                String customGroupId = key.split(StrUtil.UNDERLINE)[0];
                String dataDateori = key.split(StrUtil.UNDERLINE)[1];
                Thread.currentThread().setName("日周期：客群=" + customGroupId + "执行线程");
                log.info("delDnaCustgroupDayUpdate-->日周期：客群={}更新开始", customGroupId);
                // 分布式锁
                String lockName = ConstantDNA.CUSTOM_DAY_UPDATE_REDIS_LOCK_PRE + StrUtil.UNDERLINE + customGroupId + StrUtil.UNDERLINE + dataDateori;
                String lockId = RedisUtils.getRedisLock(lockName, 3 * 60);
                if (StrUtil.isEmpty(lockId)) {
                    log.info("delDnaCustgroupDayUpdate-->日周期：未能获取到redis 加载锁：({})，其它线程正在处理此任务，直接返回......", lockName);
                    // continue;
                }
                List<String> taskIds = val.stream().map(DNACustgroupUpdateTask::getTaskId).collect(Collectors.toList()); // 客群对应的所有的任务id
                log.info("delDnaCustgroupDayUpdate-->日周期：客群={}的所有任务id={}", customGroupId, JSONUtil.toJsonStr(taskIds));
                try {
                    // 4. 批量更新任务状态为执行中
                    idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                            .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXECUTING)
                            .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                            .in(DNACustgroupUpdateTask::getTaskId, taskIds));
                    // 5. 查询task_date表有数据且datadate是最新的话，不需要更新   注：扫描待执行任务前做了判断  当天数据日期的任务在活动保存的时候task_date表已经生成最新的数据了，则不需要走 下面的 同步任务逻辑了 直接更新任务状态为已完成了
                    // Integer dataDate = Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd"));
                    // val.forEach(dnaCustgroupUpdateVo -> {
                    //     McdCampTaskDate mcdCampTaskDate = qryTaskDate(dnaCustgroupUpdateVo, dataDate);
                    //     if (ObjectUtil.isNotEmpty(mcdCampTaskDate)) {
                    //         log.warn("delDnaCustgroupDayUpdate-->日周期：活动={}，客群={},数据日期={}的日周期清单已更新", dnaCustgroupUpdateVo.getCampsegId(), dnaCustgroupUpdateVo.getCustomGroupId(), dataDate);
                    //         // 更新DNA_CUSTGROUP_UPDATE_TASK任务表状态为完成
                    //         // 5.1 进入到这个判断里面，则DNA_CUSTGROUP_UPDATE_TASK表中的DNA_FILE_NAME值为空，表示活动保存的时候task_date表已经生成最新的数据了，则不需要走同步任务逻辑了  只需更新任务状态为已完成
                    //         updateDnaTaskStat(dnaCustgroupUpdateVo, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_COMPLETED, null);
                    //     }
                    // });
                    // // 5.2 查询并过滤掉该客群且日期为今天以及状态为 已完成的数据==>获取最新的 执行中状态的任务
                    // LambdaQueryWrapper<DNACustgroupUpdateTask> wrapper = Wrappers.<DNACustgroupUpdateTask>query().lambda()
                    //         .eq(DNACustgroupUpdateTask::getUpdateCycle, ConstantDNA.CUSTGROUP_DAY_CYCLE)
                    //         .eq(DNACustgroupUpdateTask::getCustomGroupId, customGroupId)
                    //         .eq(DNACustgroupUpdateTask::getDataDate, dataDate)
                    //         .eq(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXECUTING);
                    // List<DNACustgroupUpdateTask> executing = idnaCustgroupUpdateTaskService.list(wrapper);
                    // log.info("delDnaCustgroupDayUpdate-->日周期：客群={},数据日期={}的最新的执行中状态的任务={}", customGroupId, dataDate, JSONUtil.toJsonStr(executing));
                    // val = executing; // 将最新的执行中状态任务对象重新赋值val
                    // if (CollectionUtil.isEmpty(val)) {
                    //     log.warn("delDnaCustgroupDayUpdate-->日周期：DNA客群={}的清单文件都是最新且活动均已生成待执行任务", customGroupId);
                    //     return;
                    // }
                    // taskIds = val.stream().map(DNACustgroupUpdateTask::getTaskId).collect(Collectors.toList());
                    // log.info("delDnaCustgroupDayUpdate-->日周期：客群={}的过滤后剩的所有任务id={}", customGroupId, JSONUtil.toJsonStr(taskIds));
                    // 6. 客群清单文件处理
                    // 6.1 调用dna接口1.4 生成客群清单文件
                    // 6.2 下载清单文件到本地(plan-svc服务本地)
                    Map<String, String> map = idnaCustomGroupService.dowloadCustFile(customGroupId);
                    if (ObjectUtil.isEmpty(map)) {
                        log.warn("delDnaCustgroupDayUpdate-->日周期：DNA客群={}调用dna计算接口获取清单文件为空,查询调dna接口处日志！", customGroupId);
                        // 批量更新任务状态为异常
                        idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                                .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXCEPTION)
                                .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                                .set(DNACustgroupUpdateTask::getExceptionMsg, "日周期：DNA客群id=" + customGroupId + "调用dna计算接口获取清单文件为空,查询调dna接口处日志")
                                .in(DNACustgroupUpdateTask::getTaskId, taskIds));
                        // continue;
                    }
                    // 6.3 将获取到的清单文件按照coc文件格式命名 MCD_GROUP_客群id_数据日期.txt  MCD_GROUP_客群id_数据日期.CHK
                    // 6.3.1 获取清单文件名
                    String formatFileName = String.format(ConstantDNA.DNA_2_COC_CUSTOMGROUP_FILENAME, customGroupId, DateUtil.format(new Date(), "yyyyMMdd"));
                    String prefix = FileUtil.getPrefix(formatFileName);
                    // 6.4 按照coc格式生成校验文件
                    File chkFile = FileUtil.newFile(map.get("localPath") + "/" + prefix + ".CHK");
                    File dnaCustFile = FileUtil.newFile(map.get("localPath") + "/" + map.get("fileName"));
                    File newCustFile = FileUtil.rename(dnaCustFile, formatFileName, true);
                    if (!chkFile.exists()) {
                        chkFile.createNewFile();
                    }
                    // 6.4.1 写校验文件 文件名,文件大小,文件行数
                    FileUtil.writeString(formatFileName + StrUtil.COMMA + newCustFile.length() + StrUtil.COMMA + map.get("count"), chkFile, map.get("encoding"));
                    // 6.5 上传清单文件&校验文件到coc清单文件所在目录
                    uploadCustFile(chkFile, dnaCustFile, newCustFile, map);
                    // 7. 更新mcd_custgroup_def客群相关信息
                    McdCustgroupDef mcdCustgroupDef = new McdCustgroupDef();
                    BeanUtil.copyProperties(val.get(0), mcdCustgroupDef);
                    mcdCustgroupDef.setCustomNum(Integer.valueOf(String.valueOf(map.get("count"))));
                    mcdCustgroupDef.setActualCustomNum(Integer.valueOf(String.valueOf(map.get("count"))));
                    mcdCustgroupDef.setFileName(formatFileName);
                    mcdCustgroupDef.setDataDate(Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd"))); // 和coc客群(T-1)区别，数据日期设置为T当天时间
                    mcdCustgroupDef.setCustomSourceId(ConstantDNA.CUSTOM_SOURCE_DNA); // 数据来源1:coc 2:dna 3:多波次
                    custgroupDefService.updateById(mcdCustgroupDef);
                    // 8. mcd_camp_task_date批量插数据
                    // 8.1 查询活动的相关信息==>获取客群对应的所有的活动id
                    List<String> campsegIds = val.stream().map(DNACustgroupUpdateTask::getCampsegId).collect(Collectors.toList());
                    // 8.2 根据客群对应的所有活动id批量查询任务表信息
                    List<McdCampTask> mcdCampTasks = campTaskDao.selectList(Wrappers.<McdCampTask>query().lambda().in(McdCampTask::getCampsegId, campsegIds));
                    // 8.3 根据活动id分组
                    Map<String, List<McdCampTask>> taskListByCampsegId = mcdCampTasks.stream().collect(
                            Collectors.groupingBy(McdCampTask::getCampsegId, Collectors.toList()));
                    List<McdCampTaskDate> taskDateList = new ArrayList<>();
                    val.forEach(dnaCustgroupUpdateVo -> {
                        List<McdCampTask> mcdCampTasksList = taskListByCampsegId.get(dnaCustgroupUpdateVo.getCampsegId());
                        if (CollectionUtil.isEmpty(mcdCampTasksList)) {
                            log.warn("delDnaCustgroupDayUpdate-->日周期：活动={}未查询出任务信息,需排查活动审批通过后任务未插入原因", dnaCustgroupUpdateVo.getCampsegId());
                            updateDnaTaskStat(dnaCustgroupUpdateVo, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXCEPTION, "日周期：活动=" + dnaCustgroupUpdateVo.getCampsegId() + "未查询出任务信息,需排查活动审批通过后任务未插入原因");
                        } else {
                            // 8.4 组装mcd_camp_task_date数据
                            McdCampTask mcdCampTask = mcdCampTasksList.get(0); // 取第一个原因：任务表每个活动对应的只有一条数据
                            McdCampTaskDate date = new McdCampTaskDate();
                            date.setTaskId(mcdCampTask.getTaskId());
                            date.setDataDate(mcdCustgroupDef.getDataDate());
                            date.setExecStatus(Constant.TASK_STATUS_UNDO);
                            date.setCustListCount(mcdCustgroupDef.getCustomNum());
                            date.setPlanExecTime(new Date());
                            date.setCampsegId(mcdCampTask.getCampsegId());
                            taskDateList.add(date);
                        }
                    });
                    // 8.5 批量保存mcd_camp_task_date数据
                    campTaskDateService.saveBatch(taskDateList);
                    // 9. 批量更新DNA_CUSTGROUP_UPDATE_TASK任务表状态为完成
                    idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                            .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_COMPLETED)
                            .set(DNACustgroupUpdateTask::getCustomNum, Integer.valueOf(map.get("count")))
                            .set(DNACustgroupUpdateTask::getDnaFileName, map.get("fileName"))
                            .set(DNACustgroupUpdateTask::getReplacedFileName, formatFileName)
                            .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                            .in(DNACustgroupUpdateTask::getTaskId, taskIds));
                    log.info("delDnaCustgroupDayUpdate-->日周期：DNA客群={}更新结束", customGroupId);
                } catch (Exception e) {
                    log.error("delDnaCustgroupDayUpdate-->日周期：DNA客群={}更新任务异常：", customGroupId, e);
                    // 10. 批量更新任务状态为异常
                    idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                            .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXCEPTION)
                            .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                            .set(DNACustgroupUpdateTask::getExceptionMsg, "日周期：DNA客群id=" + customGroupId + "日周期更新任务异常: " + e.getMessage())
                            .in(DNACustgroupUpdateTask::getTaskId, taskIds));
                } finally {
                    if (RedisUtils.releaseRedisLock(lockName, lockId)) {
                        log.info("delDnaCustgroupDayUpdate-->日周期：成功释放分布式redis锁:{},lockId:({}),集群的其他服务器将尝试处理此任务......", lockName, lockId);
                    } else {
                        log.error("delDnaCustgroupDayUpdate-->日周期：未能成功释放分布式锁:{},lockId:({}),该异常会导致3分钟内无法处理，请手动删除该锁......", lockName, lockId);
                    }
                    countDownLatch_.countDown();
                }
            }
        // }
    }

    /**
     * 处理dna客群更新月周期逻辑
     *
     * @return {@link List}<{@link DNACustgroupUpdateTask}>
     */
    private void delDnaCustgroupMonthUpdate(long start) {
        List<DNACustgroupUpdateTask> month = null;
        try {
            // 1. 查询待更新的客群总数
            LambdaQueryWrapper<DNACustgroupUpdateTask> wrapper = Wrappers.<DNACustgroupUpdateTask>query().lambda()
                    .eq(DNACustgroupUpdateTask::getUpdateCycle, ConstantDNA.CUSTGROUP_MONTH_CYCLE)
                    .eq(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_WAITING);
            // .last("limit " + updateBatchSize);
            month = idnaCustgroupUpdateTaskService.list(wrapper);
            if (CollectionUtil.isEmpty(month)) {
                log.warn("delDnaCustgroupMonthUpdate-->月周期：没有待更新的客群");
                return;
            }
            log.info("delDnaCustgroupMonthUpdate-->月周期：待更新的客群总数={}条", month.size());
            // 2. 批量更新DNA_CUSTGROUP_UPDATE_TASK任务表状态为中间状态--防止任务被重复查询、重复执行
            List<String> taskIds = month.stream().map(DNACustgroupUpdateTask::getTaskId).collect(Collectors.toList());
            idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                    .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_MIDDLE)
                    .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                    .in(DNACustgroupUpdateTask::getTaskId, taskIds));
            // 3. 根据 客群id_数据日期 分组==》不通活动使用同一个客群&数据日期一致时，只需要其中一个客群请求接口获取清单文件即可
            Map<String, List<DNACustgroupUpdateTask>> monthGroup = month.stream().collect(
                    Collectors.groupingBy(i -> i.getCustomGroupId() + StrUtil.UNDERLINE + i.getDataDate(), Collectors.toList()));
            log.info("delDnaCustgroupDayUpdate-->月周期：根据客群id_数据日期分组={}, 分组后客群={}个", JSONUtil.toJsonStr(monthGroup), monthGroup.keySet().size());
            // 4. 根据分组后的客群 多线程异步去处理每个客群对应的更新任务逻辑
            final CountDownLatch countDownLatch_ = new CountDownLatch(monthGroup.keySet().size());
            for (Map.Entry<String, List<DNACustgroupUpdateTask>> entry : monthGroup.entrySet()) {
                monthCustUpdateScheduleJobExecutor.execute(new MonthCustUpdateThread(entry, countDownLatch_));
            }
            // 线程没处理完，此处会等待所有线程操作处理完才继续执行下一步！
            countDownLatch_.await();
            log.info("delDnaCustgroupMonthUpdate-->月周期：dna客群月周期更新任务结束，共更新={}条，耗时={}秒", month.size(), (System.currentTimeMillis() - start) / 1000);
        } catch (Exception e) {
            log.error("delDnaCustgroupMonthUpdate-->月周期：客群任务执行异常：", e);
        }
    }

    /**
     * 月周期更新线程类
     *
     * @author lvcc
     * @date 2024/01/08
     */
    class MonthCustUpdateThread implements Runnable {

        private Map.Entry<String, List<DNACustgroupUpdateTask>> entry;
        private CountDownLatch countDownLatch_;

        public MonthCustUpdateThread (Map.Entry<String, List<DNACustgroupUpdateTask>> entry, CountDownLatch countDownLatch_) {
            this.entry = entry;
            this.countDownLatch_ = countDownLatch_;
        }

        @Override
        public void run() {
            String key = entry.getKey();
            String customGroupId = key.split(StrUtil.UNDERLINE)[0]; // 某客群id
            List<DNACustgroupUpdateTask> val = entry.getValue(); // 某一个客群对应的所有任务信息
            // String dataDateori = key.split(StrUtil.UNDERLINE)[1]; // 客群对应的数据日期
            // 外层是客群循环 所以客群对应的任务信息是重复的 只需要取第一个即可
            DNACustgroupUpdateTask dnaCustgroupUpdateVo = val.get(0);
            Thread.currentThread().setName("月周期：客群=" + customGroupId + "执行线程");
            log.info("delDnaCustgroupMonthUpdate-->月周期：客群={}更新开始", customGroupId);
            // for (DNACustgroupUpdateTask dnaCustgroupUpdateVo : val) {
                // 分布式锁
                String lockName = ConstantDNA.CUSTOM_MONTH_UPDATE_REDIS_LOCK_PRE + StrUtil.UNDERLINE + dnaCustgroupUpdateVo.getCustomGroupId() + StrUtil.UNDERLINE + dnaCustgroupUpdateVo.getDataDate();
                String lockId = RedisUtils.getRedisLock(lockName, 3 * 60);
                if (StrUtil.isEmpty(lockId)) {
                    log.info("delDnaCustgroupMonthUpdate-->月周期：未能获取到redis 加载锁：({})，其它线程正在处理此任务，直接返回......", lockName);
                    // continue;
                }
                // 1. 批量更新任务状态为执行中
                List<String> taskIds = val.stream().map(DNACustgroupUpdateTask::getTaskId).collect(Collectors.toList());
                log.info("delDnaCustgroupMonthUpdate-->月周期：客群={}所有任务id={}", customGroupId, JSONUtil.toJsonStr(taskIds));
                idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                        .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXECUTING)
                        .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                        .in(DNACustgroupUpdateTask::getTaskId, taskIds));
                try {
                    // 2. 查询客群对应的某一个活动对应task_date表有数据  由于是按照客群_数据日期分组  所以一个客群分组下的活动肯定都是同一天创建且生成待执行任务的  如果不是，就得排查活动创建审批通过后生成待执行任务逻辑
                    McdCampTaskDate mcdCampTaskDate = campTaskDateDao.selectOne(Wrappers.<McdCampTaskDate>query().lambda().eq(McdCampTaskDate::getCampsegId, dnaCustgroupUpdateVo.getCampsegId()));
                    if (ObjectUtil.isEmpty(mcdCampTaskDate)) {
                        log.warn("delDnaCustgroupDayUpdate-->月周期：McdCampTaskDate表没有活动={}的待执行任务数据,需排查活动审批通过后任务未插入原因", dnaCustgroupUpdateVo.getCampsegId());
                        return;
                    }
                    // 2.1 查询出活动创建审批通过后生成待执行任务的"计划执行时间" 按照这个时间推算下一次的更新时间
                    String planExecTime = DateUtil.format(mcdCampTaskDate.getPlanExecTime(), "yyyyMMdd");
                    // 2.2 找出当前的计划执行时间对应的下个自然月的dataDate
                    Integer nextDataDate = addMonth(DateUtil.parse(String.valueOf(planExecTime), "yyyyMMdd"), 1);
                    if (!nextDataDate.equals(Integer.parseInt(DateUtil.format(new Date(), "yyyyMMdd")))) {
                        log.warn("delDnaCustgroupMonthUpdate-->月周期：活动={}，客群={}，原计划执行时间={}，计算后的执行时间={}的月周期清单文件未到更新时间", dnaCustgroupUpdateVo.getCampsegId(), dnaCustgroupUpdateVo.getCustomGroupId(), planExecTime, nextDataDate);
                        // 2.3 进入到这个判断里面，则DNA_CUSTGROUP_UPDATE_TASK表中的DNA_FILE_NAME值为空，表示活动保存的时候task_date表已经生成最新的数据了，目前还没到更新时间
                        // 只需批量更新任务状态为待执行(等待下一次执行)--月周期时间较长，调度是每天会跑，这样防止生成更多的无效数据(保存任务哪里校验表里有待执行任务则不会重复插入数据)
                        idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                                .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_WAITING)
                                .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                                .in(DNACustgroupUpdateTask::getTaskId, taskIds));
                        return;
                        // continue;
                    }
                    // 3. 客群清单文件处理
                    // 3.1 调用dna接口1.4 生成客群清单文件
                    // 3.2 下载清单文件到本地(plan-svc服务本地)
                    Map<String, String> map = idnaCustomGroupService.dowloadCustFile(dnaCustgroupUpdateVo.getCustomGroupId());
                    if (ObjectUtil.isEmpty(map)) {
                        log.warn("delDnaCustgroupMonthUpdate-->月周期：DNA客群={}调用dna计算接口获取清单文件为空！", dnaCustgroupUpdateVo.getCustomGroupId());
                        // 3.3 批量更新DNA_CUSTGROUP_UPDATE_TASK任务表状态为异常
                        idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                                .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXCEPTION)
                                .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                                .set(DNACustgroupUpdateTask::getExceptionMsg, "DNA客群id=" + dnaCustgroupUpdateVo.getCustomGroupId() + "调用dna计算接口获取清单文件为空,查询调dna接口处日志")
                                .in(DNACustgroupUpdateTask::getTaskId, taskIds));
                        // continue;
                    }
                    // 4. 处理月周期客群更新逻辑
                    String formatFileName = delCustgroupUpdateLogic(val, map);
                    // 5. 批量更新DNA_CUSTGROUP_UPDATE_TASK任务表状态为完成
                    idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                            .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_COMPLETED)
                            .set(DNACustgroupUpdateTask::getCustomNum, Integer.valueOf(map.get("count")))
                            .set(DNACustgroupUpdateTask::getDnaFileName, map.get("fileName"))
                            .set(DNACustgroupUpdateTask::getReplacedFileName, formatFileName)
                            .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                            .in(DNACustgroupUpdateTask::getTaskId, taskIds));
                    log.info("delDnaCustgroupMonthUpdate-->月周期：客群={}更新结束", customGroupId);
                } catch (Exception e) {
                    log.error("delDnaCustgroupMonthUpdate-->月周期：DNA客群={}月周期更新任务异常：", dnaCustgroupUpdateVo.getCustomGroupId(), e);
                    // 6. 批量更新DNA_CUSTGROUP_UPDATE_TASK任务表状态为异常
                    idnaCustgroupUpdateTaskService.update(Wrappers.<DNACustgroupUpdateTask>update().lambda()
                            .set(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXCEPTION)
                            .set(DNACustgroupUpdateTask::getUpdateTime, new Date())
                            .set(DNACustgroupUpdateTask::getExceptionMsg, "月周期：DNA客群id=" + dnaCustgroupUpdateVo.getCustomGroupId() + "月周期更新任务异常: " + e.getMessage())
                            .in(DNACustgroupUpdateTask::getTaskId, taskIds));
                } finally {
                    if (RedisUtils.releaseRedisLock(lockName, lockId)) {
                        log.info("delDnaCustgroupMonthUpdate-->月周期：成功释放分布式redis锁:{},lockId:({}),集群的其他服务器将尝试处理此任务......", lockName, lockId);
                    } else {
                        log.error("delDnaCustgroupMonthUpdate-->月周期：未能成功释放分布式锁:{},lockId:({}),该异常会导致3分钟内无法处理，请手动删除该锁......", lockName, lockId);
                    }
                    countDownLatch_.countDown();
                }
            // }
        }
    }

    /**
     * 更新DNA_CUSTGROUP_UPDATE_TASK表状态
     *
     * @param dnaCustgroupUpdateVo 待更新对象
     * @param status               状态
     */
    private void updateDnaTaskStat(DNACustgroupUpdateTask dnaCustgroupUpdateVo, Integer status, String msg) {
        LambdaUpdateWrapper<DNACustgroupUpdateTask> update = new LambdaUpdateWrapper<>();
        update.set(DNACustgroupUpdateTask::getExecStatus, status)
              .set(DNACustgroupUpdateTask::getUpdateTime, new Date());
        if (StrUtil.isNotEmpty(msg)) {
            update.set(DNACustgroupUpdateTask::getExceptionMsg, msg);
        }
        update.eq(DNACustgroupUpdateTask::getTaskId, dnaCustgroupUpdateVo.getTaskId());
        idnaCustgroupUpdateTaskService.update(update);
    }

    /**
     * 待更新客群任务表状态更新
     * 若DNA_CUSTGROUP_UPDATE_TASK表中的DNA_FILE_NAME值为空，表示活动保存的时候task_date表已经生成最新的数据了，则不需要走同步任务逻辑了
     *
     * @param dnaCustgroupUpdateVo       客群信息
     * @param dnaCustgroupUpdateTaskStat 任务状态
     */
    private void custgroupUpdateTaskStatUpdate(DNACustgroupUpdateTask dnaCustgroupUpdateVo, Integer dnaCustgroupUpdateTaskStat, Map<String, String> map, String formatFileName) {
        dnaCustgroupUpdateVo.setExecStatus(dnaCustgroupUpdateTaskStat);
        if (ObjectUtil.isNotEmpty(map) && StrUtil.isNotEmpty(formatFileName)) {
            dnaCustgroupUpdateVo.setCustomNum(Integer.valueOf(map.get("count")));
            dnaCustgroupUpdateVo.setDnaFileName(map.get("fileName"));
            dnaCustgroupUpdateVo.setReplacedFileName(formatFileName);
            dnaCustgroupUpdateVo.setUpdateTime(new Date());
        }
        idnaCustgroupUpdateTaskService.saveOrUpdate(dnaCustgroupUpdateVo);
    }

    /**
     * DNA客群更新通知接口--dna侧调用
     * dna标签更新，调此接口通知iop进行更新客群清单
     * 1. iop查询出执行中/暂停活动使用的日周期/月周期客群信息
     * 2. 将查询出来的客群信息保存到一张任务表，再起一个调度，从任务表中分批查询客群信息去更新(这样可以防止需要更新的客群数据量较大时，不会出现在某一个节点执行)
     * 注：若DNA_CUSTGROUP_UPDATE_TASK表中的DNA_FILE_NAME值为空，表示活动保存的时候task_date表已经生成最新的数据了，则不需要走同步任务逻辑了
     *
     * @param request 入参 dataDate--客群数据日期
     */
    @Override
    public void dnaCustgroupUpdateNotice(JSONObject request) throws Exception {
        // 获取数据日期
        Integer dataDate = request.getInt("dataDate");
        String custGroupId = request.getStr("custGroupId");
        long start = System.currentTimeMillis();
        log.info("dnaCustgroupUpdateNotice-->入DNA待更新客群任务表开始");
        // iop查询出执行中/暂停活动使用的日周期/月周期客群信息
        List<DnaCustgroupUpdateVo> needUpdateCustgroup = dnaCustgroupDayMonthUpdateDao.queryNeedUpdateCustgroup(custGroupId);
        if (CollectionUtil.isEmpty(needUpdateCustgroup)) {
            log.warn("dnaCustgroupUpdateNotice-->没有需要更新的客群");
            return;
        }
        log.info("dnaCustgroupUpdateNotice-->查询出需要更新的所有客群={}条", needUpdateCustgroup.size());
        List<DNACustgroupUpdateTask> taskListDay = new ArrayList<>();
        List<DNACustgroupUpdateTask> taskListMonth = new ArrayList<>();
        // a. 日周期待更新客群
        dayUpdate(needUpdateCustgroup, taskListDay);
        // b. 月周期待更新客群
        monthUpdate(needUpdateCustgroup, taskListMonth);
        log.info("dnaCustgroupUpdateNotice-->入DNA待更新客群任务表结束,耗时={}秒", (System.currentTimeMillis() - start) / 1000);
    }

    /**
     * 日周期待更新客群
     *
     * @param needUpdateCustgroup 需要更新的所有客群
     * @param taskListDay 需要更新的日客群
     */
    private void dayUpdate(List<DnaCustgroupUpdateVo> needUpdateCustgroup, List<DNACustgroupUpdateTask> taskListDay) {
        // 1. 分组获取日周期客群
        List<DnaCustgroupUpdateVo> dayCycleCustom = needUpdateCustgroup.stream().filter(a -> 3 == a.getUpdateCycle()).collect(Collectors.toList());
        log.info("dnaCustgroupUpdateNotice-->查询出需要更新的日周期客群={}条", dayCycleCustom.size());
        if (CollectionUtil.isNotEmpty(dayCycleCustom)) {
            log.info("日周期逻辑处理开始");
            dayCycleCustom.forEach(vo -> {
                // 当天创建的活动清单已是最新，若活动创建时间在通知接口调度执行之前，则不需要将此活动使用的客群增加到任务表中
                if (!Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd")).equals(vo.getDataDate())) {
                    DNACustgroupUpdateTask task = new DNACustgroupUpdateTask();
                    task.setTaskId(IdUtils.generateId());
                    task.setCampsegId(vo.getCampsegId());
                    task.setCustomGroupId(vo.getCustomGroupId());
                    // 数据日期与保存任务的时间一致(取当天插任务的时间)==》如果取客群定义表数据日期的话，由于日周期更新任务执行取得是前一天的数据日期，则中间有某一次任务更新异常，客群定义表数据日期没有被及时更新，则后续任务则不会再跑了
                    task.setDataDate(Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd")));
                    task.setExecStatus(ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_WAITING);
                    task.setUpdateCycle(vo.getUpdateCycle());
                    task.setCustomNum(vo.getCustomNum());
                    task.setReplacedFileName(vo.getFileName());
                    taskListDay.add(task);
                }
            });
            if (CollectionUtil.isEmpty(taskListDay)) {
                log.warn("dnaCustgroupUpdateNotice-->没有需要更新的日周期客群");
                return;
            }
            List<Integer> stats = Arrays.asList(ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_WAITING, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_MIDDLE, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXECUTING);
            // 2. 获取任务表日周期且状态是50/505/51的数据
            LambdaQueryWrapper<DNACustgroupUpdateTask> wrapper = Wrappers.<DNACustgroupUpdateTask>query().lambda()
                    .eq(DNACustgroupUpdateTask::getUpdateCycle, ConstantDNA.CUSTGROUP_DAY_CYCLE)
                    .in(DNACustgroupUpdateTask::getExecStatus, stats);
            List<DNACustgroupUpdateTask> day = idnaCustgroupUpdateTaskService.list(wrapper);
            log.info("dnaCustgroupUpdateNotice-->任务表日周期待执行任务={}条", day.size());
            // 3. 在活动执行有效期内 任务重复跑会导致待更新客群被重复查询 需要过滤（若任务表存在同一活动、同一客群、同一执行周期、同一数据日期的任务，则不重复插） taskList - day
            // 3.1 对于日周期  任务表应该每天都有该活动的一条数据
            List<DNACustgroupUpdateTask> collect = taskListDay.stream()
                    .filter(item -> !day.stream()
                    .map(e -> e.getCampsegId() + "&" + e.getCustomGroupId() + "&" + e.getUpdateCycle() + "&" + e.getDataDate())
                    .collect(Collectors.toList())
                    .contains(item.getCampsegId() + "&" + item.getCustomGroupId() + "&" + item.getUpdateCycle() + "&" + item.getDataDate()))
                    .collect(Collectors.toList());
            log.info("dnaCustgroupUpdateNotice-->剔除同一活动、同一客群、同一更新周期、同一数据日期的任务={}条", collect.size());
            if (collect.size() > 0) {
                // 4. 查询当天数据日期的日周期任务
                LambdaQueryWrapper<DNACustgroupUpdateTask> wrapper2 = Wrappers.<DNACustgroupUpdateTask>query().lambda()
                        .eq(DNACustgroupUpdateTask::getUpdateCycle, ConstantDNA.CUSTGROUP_DAY_CYCLE)
                        .eq(DNACustgroupUpdateTask::getDataDate, Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd")));
                List<DNACustgroupUpdateTask> day2 = idnaCustgroupUpdateTaskService.list(wrapper2);
                log.info("dnaCustgroupUpdateNotice-->任务表日周期当天执行成功的任务={}条", day2.size());
                // 4.1 过滤掉当天数据日期的日周期任务 （同一个活动的任务当已有了最新数据日期时不再重复插入）
                List<DNACustgroupUpdateTask> finall = collect.stream()
                        .filter(item -> !day2.stream()
                                .map(DNACustgroupUpdateTask::getCampsegId)
                                .collect(Collectors.toList())
                                .contains(item.getCampsegId()))
                        .collect(Collectors.toList());
                idnaCustgroupUpdateTaskService.saveOrUpdateBatch(finall);
                log.info("日周期逻辑处理结束,保存待更新客群finall={}条", finall.size());
            } else {
                idnaCustgroupUpdateTaskService.saveOrUpdateBatch(collect);
                log.info("日周期逻辑处理结束,保存待更新客群collect={}条", collect.size());
            }
        }
    }

    /**
     * 月周期待更新客群
     *
     * @param needUpdateCustgroup 需要更新的所有客群
     * @param taskListMonth 需要更新的月客群
     */
    private void monthUpdate(List<DnaCustgroupUpdateVo> needUpdateCustgroup, List<DNACustgroupUpdateTask> taskListMonth) {
        List<DnaCustgroupUpdateVo> monthCycleCustom = needUpdateCustgroup.stream().filter(a -> 2 == a.getUpdateCycle()).collect(Collectors.toList());
        log.info("dnaCustgroupUpdateNotice-->查询出需要更新的月客群={}条", monthCycleCustom.size());
        if (CollectionUtil.isNotEmpty(monthCycleCustom)) {
            log.info("月周期逻辑处理开始");
            monthCycleCustom.forEach(vo -> {
                DNACustgroupUpdateTask task = new DNACustgroupUpdateTask();
                task.setTaskId(IdUtils.generateId());
                task.setCampsegId(vo.getCampsegId());
                task.setCustomGroupId(vo.getCustomGroupId());
                // task.setDataDate(dataDate);
                task.setDataDate(vo.getDataDate());
                task.setExecStatus(ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_WAITING);
                task.setUpdateCycle(vo.getUpdateCycle());
                task.setCustomNum(vo.getCustomNum());
                task.setReplacedFileName(vo.getFileName());
                taskListMonth.add(task);
            });
            // 获取任务表月周期且状态是50的数据
            LambdaQueryWrapper<DNACustgroupUpdateTask> wrapper = Wrappers.<DNACustgroupUpdateTask>query().lambda()
                    .eq(DNACustgroupUpdateTask::getUpdateCycle, ConstantDNA.CUSTGROUP_MONTH_CYCLE)
                    .eq(DNACustgroupUpdateTask::getExecStatus, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_WAITING);
            List<DNACustgroupUpdateTask> month = idnaCustgroupUpdateTaskService.list(wrapper);
            log.info("dnaCustgroupUpdateNotice-->任务表月周期待执行任务={}条", month.size());

            // taskList - month(若任务表存在同一活动、同一客群、同一执行周期的任务，则不重复插)  由于调度一直跑，同一个活动、同一个客群被重复扫到时，以这个活动第一次插入任务表数据为准
            List<DNACustgroupUpdateTask> collect = taskListMonth.stream()
                    .filter(item -> !month.stream()
                    .map(e -> e.getCampsegId() + "&" + e.getCustomGroupId() + "&" + e.getUpdateCycle())
                    .collect(Collectors.toList())
                    .contains(item.getCampsegId() + "&" + item.getCustomGroupId() + "&" + item.getUpdateCycle()))
                    .collect(Collectors.toList());
            idnaCustgroupUpdateTaskService.saveOrUpdateBatch(collect);
            log.info("月周期逻辑处理结束,保存待更新客群={}条", collect.size());
        }
    }

    /**
     * 查询task_date表有数据且datadate是最新的
     *
     * @param dnaCustgroupUpdateVo 客群信息
     * @return {@link McdCampTaskDate}
     */
    private McdCampTaskDate qryTaskDate(DNACustgroupUpdateTask dnaCustgroupUpdateVo, Integer dataDate) {
        LambdaQueryWrapper<McdCampTaskDate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdCampTaskDate::getCampsegId, dnaCustgroupUpdateVo.getCampsegId())
                .eq(McdCampTaskDate::getDataDate, dataDate);
        return campTaskDateDao.selectOne(queryWrapper);
    }

    /**
     * 处理日周期/月周期客群更新逻辑
     *
     * @param val                  需要更新的日周期/月周期客群信息
     * @param map                  dna接口返回的信息
     * @return {@link String} 替换后的文件名
     * @throws Exception 异常
     */
    private String delCustgroupUpdateLogic(List<DNACustgroupUpdateTask> val, Map<String, String> map) throws Exception {
        // 1. 将获取到的清单文件按照coc文件格式命名 MCD_GROUP_客群id_数据日期.txt  MCD_GROUP_客群id_数据日期.CHK
        // 1.1 获取清单文件名
        String formatFileName = String.format(ConstantDNA.DNA_2_COC_CUSTOMGROUP_FILENAME, val.get(0).getCustomGroupId(), DateUtil.format(new Date(), "yyyyMMdd"));
        String prefix = FileUtil.getPrefix(formatFileName);
        // 2. 按照coc格式生成校验文件
        File chkFile = FileUtil.newFile(map.get("localPath") + "/" + prefix + ".CHK");
        File dnaCustFile = FileUtil.newFile(map.get("localPath") + "/" + map.get("fileName"));
        File newCustFile = FileUtil.rename(dnaCustFile, formatFileName, true);
        if (!chkFile.exists()) {
            chkFile.createNewFile();
        }
        // 2.1 写校验文件 文件名,文件大小,文件行数
        FileUtil.writeString(formatFileName + StrUtil.COMMA + newCustFile.length() + StrUtil.COMMA + map.get("count"), chkFile, map.get("encoding"));
        // 3. 上传清单文件&校验文件到coc清单文件所在目录
        uploadCustFile(chkFile, dnaCustFile, newCustFile, map);
        // 4. 更新mcd_custgroup_def客群相关信息
        McdCustgroupDef mcdCustgroupDef = new McdCustgroupDef();
        BeanUtil.copyProperties(val.get(0), mcdCustgroupDef);
        mcdCustgroupDef.setCustomNum(Integer.valueOf(String.valueOf(map.get("count"))));
        mcdCustgroupDef.setActualCustomNum(Integer.valueOf(String.valueOf(map.get("count"))));
        mcdCustgroupDef.setFileName(formatFileName);
        mcdCustgroupDef.setDataDate(Integer.valueOf(DateUtil.format(new Date(), "yyyyMM")));
        mcdCustgroupDef.setCustomSourceId(ConstantDNA.CUSTOM_SOURCE_DNA); // 数据来源1:coc 2:dna 3:多波次
        custgroupDefService.updateById(mcdCustgroupDef);
        // 5. mcd_camp_task_date批量插数据
        // 5.1 查询活动的相关信息==>获取客群对应的所有的活动id
        List<String> campsegIds = val.stream().map(DNACustgroupUpdateTask::getCampsegId).collect(Collectors.toList());
        // 5.2 根据客群对应的所有活动id批量查询任务表信息
        List<McdCampTask> mcdCampTasks = campTaskDao.selectList(Wrappers.<McdCampTask>query().lambda().in(McdCampTask::getCampsegId, campsegIds));
        // 5.3 根据活动id分组
        Map<String, List<McdCampTask>> taskListByCampsegId = mcdCampTasks.stream().collect(
                Collectors.groupingBy(McdCampTask::getCampsegId, Collectors.toList()));
        List<McdCampTaskDate> taskDateList = new ArrayList<>();
        val.forEach(dnaCustgroupUpdateVo -> {
            List<McdCampTask> mcdCampTasksList = taskListByCampsegId.get(dnaCustgroupUpdateVo.getCampsegId());
            if (CollectionUtil.isEmpty(mcdCampTasksList)) {
                log.warn("delDnaCustgroupDayUpdate-->月周期：活动={}未查询出任务信息,需排查活动审批通过后任务未插入原因", dnaCustgroupUpdateVo.getCampsegId());
                updateDnaTaskStat(dnaCustgroupUpdateVo, ConstantDNA.DNA_CUSTGROUP_UPDATE_TASK_EXCEPTION, "月周期：活动=" + dnaCustgroupUpdateVo.getCampsegId() + "未查询出任务信息,需排查活动审批通过后任务未插入原因");
            } else {
                // 5.4 组装mcd_camp_task_date数据
                McdCampTask mcdCampTask = mcdCampTasksList.get(0); // 取第一个原因：任务表每个活动对应的只有一条数据
                McdCampTaskDate date = new McdCampTaskDate();
                date.setTaskId(mcdCampTask.getTaskId());
                date.setDataDate(mcdCustgroupDef.getDataDate());
                date.setExecStatus(Constant.TASK_STATUS_UNDO);
                date.setCustListCount(mcdCustgroupDef.getCustomNum());
                date.setPlanExecTime(new Date());
                date.setCampsegId(mcdCampTask.getCampsegId());
                taskDateList.add(date);
            }
        });
        // 5.5 批量保存mcd_camp_task_date数据
        campTaskDateService.saveBatch(taskDateList);
        return formatFileName;
    }

    /**
     * 上传清单文件&校验文件到coc清单文件所在目录
     *
     * @param chkFile     校验文件
     * @param dnaCustFile 更名后的客群文件
     * @param newCustFile dna接口获取到的客群文件
     * @param map         调用dna接口返回
     */
    private void uploadCustFile(File chkFile, File dnaCustFile, File newCustFile, Map<String, String> map) {
        SftpUtils sftpUtils = new SftpUtils();
        // 1. coc客群清单存放sftp配置
        String custFileFtpUsername = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_USER);
        String custFileFtpPassword = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_PASSWD);
        String custFileHost = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_HOST);
        String custFileServerPort = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_PORT);
        String custFileServerPath = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_SERVER_PATH);
        // 2. 连接sftp
        ChannelSftp channelSftp = sftpUtils.connect(custFileHost, Integer.parseInt(custFileServerPort), custFileFtpUsername, custFileFtpPassword);
        if (FileUtil.isNotEmpty(chkFile)) {
            // 3. 先上传校验文件，校验文件上传成功后，再上传清单文件
            boolean uploadCustChkFile = sftpUtils.upload(custFileServerPath, chkFile.getName(), map.get("localPath"), channelSftp);
            if (uploadCustChkFile) {
                log.info("校验文件={}上传sftp成功", chkFile.getName());
                if (FileUtil.isNotEmpty(newCustFile)) {
                    // 4. 上传清单文件
                    boolean uploadCustFile = sftpUtils.upload(custFileServerPath, newCustFile.getName(), map.get("localPath"), channelSftp);
                    if (uploadCustFile) {
                        log.info("源文件={}，更名后文件={}上传sftp成功", dnaCustFile.getName(), newCustFile.getName());
                    }
                }
            }
        }
    }

    /**
     * 根据当前日期获取下一个自然月
     *
     * @param date  目标日期
     * @param count 下N个月
     * @return {@link Integer}
     */
    public static Integer addMonth(Date date, int count) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, count);
        String format = DateUtil.format(gc.getTime(), "yyyyMMdd");
        return Integer.valueOf(format);
    }

    /**
     * 根据当前日期前一天
     *
     * @param date 当天日期
     * @param count 表示前一天
     * @return {@link Integer}
     */
    public static Integer dayPre(Date date, int count) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DATE, count);
        String format = DateUtil.format(gc.getTime(), "yyyyMMdd");
        return Integer.valueOf(format);
    }

    // public static void main(String[] args) {
    //     DateTime yyyyMMdd = DateUtil.parse("20240101", "yyyyMMdd");
    //     Integer integer = dayPre(yyyyMMdd, -1);
    //     System.out.println(integer);
    // }
}
