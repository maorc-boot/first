package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.client.app.element.vo.PlanDefVO;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.common.ConstantCamp;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampImportTaskMapper;
import com.asiainfo.biapp.pec.plan.jx.camp.enums.ImportTaskStatus;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampImportTask;
import com.asiainfo.biapp.pec.plan.jx.camp.req.*;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ChannelConfService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampsegServiceJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdCampImportTaskService;
import com.asiainfo.biapp.pec.plan.jx.utils.ExcelUtils;
import com.asiainfo.biapp.pec.plan.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.model.McdDimAdivInfo;
import com.asiainfo.biapp.pec.plan.service.IMcdCampDefService;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import com.asiainfo.biapp.pec.plan.vo.CustgroupDetailVO;
import com.asiainfo.biapp.pec.plan.vo.req.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author mamp
 * @since 2023-04-10
 */
@Service
@Slf4j
public class McdCampImportTaskServiceImpl extends ServiceImpl<McdCampImportTaskMapper, McdCampImportTask> implements McdCampImportTaskService {

    //并发五个线程
    private static ExecutorService threadPool = null;

    @Autowired
    private ChannelConfServcieHolder servcieHolder;

    @Autowired
    private IMcdCampsegServiceJx campsegService;

    @Autowired
    private IMcdCampDefService campDefService;


    @PostConstruct
    public void init() {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(50);
        RejectedExecutionHandler policy = new ThreadPoolExecutor.DiscardPolicy();
        initThreadPool(queue, policy);
    }

    /**
     * 初始化线程池
     *
     * @param queue  等待队列
     * @param policy 拒绝策略
     */
    private static void initThreadPool(BlockingQueue<Runnable> queue, RejectedExecutionHandler policy) {
        threadPool = new ThreadPoolExecutor(1, 10,
                0, TimeUnit.SECONDS, queue, policy);
    }

    /**
     * 通过上传的excel，生成活动
     *
     * @param file
     * @param task
     * @return
     * @throws Exception
     */
    @Override
    public boolean loadCampFromFile(MultipartFile file, McdCampImportTask task, UserSimpleInfo user) throws Exception {
        try {
            if (null != user) {
                task.setCreateUser(user.getUserId());
            }
            // 进度
            task.setTaskProgress("0");
            // 执行中
            task.setTaskStatus(ConstantCamp.IMPORT_TASK_STATUS_RUNNING);
            // 剩余时间
            task.setRemainTime("-");
            // 创建任务
            save(task);
            List<Object[]> result = ExcelUtils.readeExcelData(file.getInputStream(), 0, 2);
            // 异步导入活动
            threadPool.execute(() -> saveCamps(result, task, user));
            return true;
        } catch (Exception e) {
            log.error("loadCampFromFile error", e);
        }
        return false;

    }

    /**
     * 查询任务列表
     *
     * @param query
     * @return
     */
    @Override
    public Page<McdCampImportTask> queryTask(ImportTaskQuery query) {
        Page<McdCampImportTask> page = new Page<>();
        page.setCurrent(query.getCurrent());
        page.setSize(query.getSize());
        LambdaQueryWrapper<McdCampImportTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(McdCampImportTask::getCreateTime);
        Page<McdCampImportTask> taskPage = this.page(page, queryWrapper);
        // 将 状态编码转成 文字
        for (McdCampImportTask record : taskPage.getRecords()) {
            record.setTaskStatus(ImportTaskStatus.valueOfId(record.getTaskStatus()));
        }
        return taskPage;
    }

    /**
     * 读取文件数据并保存活动
     *
     * @param result
     * @param task
     * @return
     */
    private boolean saveCamps(List<Object[]> result, McdCampImportTask task, UserSimpleInfo user) {
        int successNum = 0;
        int failNum = 0;
        LambdaUpdateWrapper<McdCampImportTask> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(McdCampImportTask::getId, task.getId());
        try {
            if (CollectionUtils.isEmpty(result)) {
                log.error("文件内容为空");
                return false;
            }
            log.info(" task:{} 需要导入活动数量:{} ", task.getId(), result.size());
            for (Object[] objects : result) {

                if (saveCamp(objects, task, user)) {
                    successNum++;
                    continue;
                }
                failNum++;
            }
            log.info("task:{} 成功导入活动数量:{} ,失败数量: {}  ", task.getId(), successNum, failNum);
            task.setTaskProgress("100%");
            task.setCampTotal(successNum);
            task.setTaskStatus(ConstantCamp.IMPORT_TASK_STATUS_SUCCESS);
            task.setRemainTime("0");
            // 更新任务状态
            this.update(task, wrapper);
        } catch (Exception e) {
            task.setTaskProgress("-");
            task.setCampTotal(successNum);
            task.setTaskStatus(ConstantCamp.IMPORT_TASK_STATUS_ERROR);
            task.setFailReason("第" + (successNum + 1) + "行异常:" + e.getMessage());
            this.update(task, wrapper);
            log.error("批量保存策略异常:", e);
        }

        return successNum > 0;
    }

    /**
     * 保存单个活动
     *
     * @param object
     * @param task
     * @return
     * @throws ParseException
     */
    private boolean saveCamp(Object[] object, McdCampImportTask task, UserSimpleInfo user) throws Exception {

        if (object == null || object.length < 16) {
            throw new Exception("数据不完整");
        }
        String campsegName = (String) object[0];
        String campsegDesc = (String) object[1];
        String custGroupId = (String) object[2];
        String planId = (String) object[3];
        String startDate = (String) object[7];
        String endDate = (String) object[8];
        String campsegType = (String) object[9];
        String activityType = (String) object[10];
        String activityObjective = (String) object[11];
        String channelId = (String) object[13];
        String channelAdivId = (String) object[14];
        String execContent = (String) object[15];
        // 融合产品
        String fixPlanIds = (String) object[4];
        // 同系列产品
        String evaluationPlanIds = (String) object[5];
        // 互斥产品
        String exPlanIds = (String) object[6];
        // 是否预演
        String isPreview = (String) object[12];
        ChannelConfService confService = servcieHolder.getService(channelId);
        TacticsInfoJx req = new TacticsInfoJx();
        // 策略创建类型: 2-批量导入
        req.setType(1);
        // 默认不提交
        req.setIsSubmit(0);
        CampBaseInfoJxVO baseInfo = new CampBaseInfoJxVO();
        // 活动名称
        baseInfo.setCampsegName(campsegName);
        // 活动描述
        baseInfo.setCampsegDesc(campsegDesc);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 活动开始时间
        baseInfo.setStartDate(sdf.parse(startDate));
        // 活动结束时间
        baseInfo.setEndDate(sdf.parse(endDate));
        // rootId
        baseInfo.setCampsegRootId("0");
        baseInfo.setCampsegId(IdUtils.generateId());
        baseInfo.setCampDefType(1);
        // 创建人
        baseInfo.setCreateUserId(task.getCreateUser());
        // 创建时间
        baseInfo.setCreateTime(new Date());
        baseInfo.setTacticsMap("[{\"group\":[\"1\",\"2\",\"3\"],\"children\":[]}]");
        // 策略类型
        baseInfo.setCampsegTypeId(Integer.valueOf(campsegType.split(":")[0]));
        // 活动类型
        baseInfo.setActivityType(activityType.split(":")[0]);
        // 活动目的
        baseInfo.setActivityObjective(activityObjective.split(":")[0]);
        // 是否预演
        String[] previewItems = isPreview.split(":");
        baseInfo.setPreviewCamp(previewItems[0]);
        List<PlanExtInfo> extInfos = new ArrayList<>();
        PlanExtInfo planExtInfo = new PlanExtInfo();
        planExtInfo.setPlanId(planId);
        extInfos.add(planExtInfo);
        req.setPlanExtInfoList(extInfos);
        // 融合产品
        planExtInfo.setCampFusionPlans(getPlanBaseInfos(fixPlanIds));
        // 同系列产品
        planExtInfo.setCampSeriesPlan(getPlanBaseInfos(evaluationPlanIds));
        //  互斥产品
        planExtInfo.setCampExclusivePlan(getPlanBaseInfos(exPlanIds));

        req.setBaseCampInfo(baseInfo);
        List<CampScheme> campSchemes = new ArrayList<>();
        req.setCampSchemes(campSchemes);
        CampScheme campScheme = new CampScheme();
        campSchemes.add(campScheme);
        // 子活动基本信息
        CampBaseInfoVO baseCampInfo = new CampBaseInfoVO();
        // 将父活动信息复制到子活动
        BeanUtil.copyProperties(baseInfo, baseCampInfo);
        // 重新生成子活动ID
        baseCampInfo.setCampsegId(IdUtils.generateId());
        // 子活动rootId
        baseCampInfo.setCampsegRootId(baseInfo.getCampsegId());
        campScheme.setBaseCampInfo(baseCampInfo);

        // 产品信息
        List<PlanDefVO> product = new ArrayList<>();
        PlanDefVO planDefVO = new PlanDefVO();
        planDefVO.setPlanId(planId);
        product.add(planDefVO);
        campScheme.setProduct(product);

        // 客户群信息
        List<CustgroupDetailVO> customer = new ArrayList<>();
        CustgroupDetailVO custgroupDetailVO = new CustgroupDetailVO();
        custgroupDetailVO.setCustomGroupId(custGroupId);
        // 默认为一次性
        custgroupDetailVO.setUpdateCycle(1);
        customer.add(custgroupDetailVO);
        campScheme.setCustomer(customer);

        // 渠道信息
        List<ChannelInfo> channels = new ArrayList<>();

        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setChannelId(channelId);
        CampChannelConfQuery channelConf = new CampChannelConfQuery();
        channelInfo.setChannelConf(channelConf);
        channelConf.setExecContent(execContent);

        // 渠道扩展信息
        CampChannelExtQuery extConf = confService.getChannelExtConf(object, task);
        channelConf.setChannelExtConf(extConf);
        channels.add(channelInfo);
        campScheme.setChannels(channels);

        // 运营位
        McdDimAdivInfo adivInfo = new McdDimAdivInfo();
        adivInfo.setAdivId(channelAdivId);
        channelInfo.setAdivInfo(adivInfo);

        List<CampChildrenScheme> childrenSchemes = new ArrayList<>();
        req.setChildrenSchemes(childrenSchemes);
        CampChildrenScheme campChildrenScheme = new CampChildrenScheme();
        childrenSchemes.add(campChildrenScheme);
        // 子活动ID
        campChildrenScheme.setCampsegId(IdUtils.generateId());
        // 产品id，客群id，渠道id，运营位id]
        List<String> data = new ArrayList<>();
        campChildrenScheme.setData(data);
        // 产品ID
        data.add(planDefVO.getPlanId());
        // 客群id
        data.add(custGroupId);
        // 渠道id
        data.add(channelInfo.getChannelId());
        // 运营位id
        data.add(channelInfo.getAdivInfo().getAdivId());
        // 保存活动
        campsegService.saveCamp(req, user);
        // 更新活动创建类开为 ,导入-2
        LambdaUpdateWrapper<McdCampDef> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(McdCampDef::getCampCreateType, ConstantCamp.CAMP_CREATE_TYPE_IMPORT).eq(McdCampDef::getCampsegRootId, baseInfo.getCampsegId()).or().eq(McdCampDef::getCampsegId, baseInfo.getCampsegId());
        campDefService.update(wrapper);
        return true;
    }

    /**
     * 将产品字符串转为集合
     *
     * @param planIds
     * @return
     */
    List<PlanBaseInfo> getPlanBaseInfos(String planIds) {
        List<PlanBaseInfo> list = new ArrayList<>();
        if (StrUtil.isEmpty(planIds)) {
            return list;
        }
        for (String planId : planIds.split(",")) {
            PlanBaseInfo baseInfo = new PlanBaseInfo();
            baseInfo.setPlanId(planId);
            list.add(baseInfo);
        }
        return list;
    }
}
