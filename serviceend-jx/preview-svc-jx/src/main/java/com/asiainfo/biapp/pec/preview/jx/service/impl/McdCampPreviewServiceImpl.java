package com.asiainfo.biapp.pec.preview.jx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.client.jx.preview.model.McdCampPreview;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustConf;
import com.asiainfo.biapp.pec.preview.jx.entity.McdPreviewInfo;
import com.asiainfo.biapp.pec.preview.jx.mapper.McdCampPreviewMapper;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampPreviewExecResult;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampPreviewResult;
import com.asiainfo.biapp.pec.preview.jx.model.PreveiwResultReq;
import com.asiainfo.biapp.pec.preview.jx.service.McdCampPreviewLogService;
import com.asiainfo.biapp.pec.preview.jx.service.McdCampPreviewService;
import com.asiainfo.biapp.pec.preview.jx.service.McdChannelSensitiveCustConfService;
import com.asiainfo.biapp.pec.preview.jx.service.feign.CampPreviewFilterLogClient;
import com.asiainfo.biapp.pec.preview.jx.thread.McdPreviewThread;
import com.asiainfo.biapp.pec.preview.jx.util.CustgroupUtil;
import com.asiainfo.biapp.pec.preview.jx.util.PreviewConst;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * 营销活动预演 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-09-22
 */
@Service
@Slf4j
@RefreshScope
public class McdCampPreviewServiceImpl extends ServiceImpl<McdCampPreviewMapper, McdCampPreview> implements McdCampPreviewService {

    private ExecutorService previewExecutor = ThreadUtil.newExecutor(5, 10);

    private final String NO_DATA = "--";

    @Resource
    private McdCampPreviewMapper previewMapper;

    @Autowired
    private McdCampPreviewLogService campPreviewLogService;

    @Autowired
    private CustgroupUtil custgroupUtil;

    @Autowired
    private CampPreviewFilterLogClient previewFilterLogClient;

    /**
     * 敏感客户群配置服务
     */
    @Autowired
    private McdChannelSensitiveCustConfService sensitiveCustConfService;


    @Value("${pec.preview.fqc.filterChnIds:801,901,932,957}")
    private String fqcFilterChnIds;
    @Value("${pec.preview.recommend.recommendFilterChnIds:906,922,924,961}")
    private String recommendFilterChnIds;

    @Value("${pec.preview.planPre.scores:0.3,0.6,0.8}")
    private String scores;

    /**
     * 查询待执行预演任务列表
     *
     * @return
     */
    @Override
    public List<McdPreviewInfo> queryCampPreviewInfoList() {
        return previewMapper.queryCampPreviewInfoList();
    }


    /**
     * 开启预演任务
     *
     * @return
     */
    @Override
    public boolean startPreview() {
        try {
            List<McdPreviewInfo> previewInfoList = queryCampPreviewInfoList();
            if (CollectionUtil.isEmpty(previewInfoList)) {
                log.info("待预演任务数量为0");
                return true;
            }
            log.info("待预演任务数量:{},详情:{}", previewInfoList.size(), JSONUtil.toJsonStr(previewInfoList));
            previewInfoList.forEach(preview -> {
                submitPreviewTask(preview);
            });
        } catch (Exception e) {
            log.error("启动预演任务失败:", e);
            return false;
        }
        return true;
    }

    /**
     * 提交预演任务
     *
     * @param preview
     */
    private void submitPreviewTask(McdPreviewInfo preview) {
        if (null == preview || StrUtil.isEmpty(preview.getCampsegId()) || ObjectUtil.isEmpty(preview.getTargetUserNum())) {
            log.error("预演数据为空或者客群数量为空");
            return;
        }
        String lockKey = PreviewConst.PREVIEW_LOCK_PREFIX + "_" + preview.getCampsegId();
        String lockId = RedisUtils.getRedisLock(lockKey, 2 * 60);

        if (StrUtil.isEmpty(lockId)) {
            log.info("未能获取到预演redis锁：({})，其它线程正在处理此任务，直接返回......", lockKey);
            return;
        }
        try {
            // 1. 更新活动状态为预演中
            this.updateCampsegStatus(preview, PreviewConst.CAMPSEG_STATUS_PREVIEW_RUNNING, PreviewConst.CAMPSEG_STATUS_CRAFT);
            this.updateCampChannelListStatus(preview, PreviewConst.CAMPSEG_STATUS_PREVIEW_RUNNING, PreviewConst.CAMPSEG_STATUS_CRAFT);
            // 2. 更新预演表状态为预演任务已提交
            this.updateCampPreviewStatus(preview, PreviewConst.PREVIEW_STATUS_SUBMITED);
            // 3. 启动预演线程
            McdPreviewThread mcdPreviewThread = new McdPreviewThread(preview, this, campPreviewLogService, custgroupUtil);
            previewExecutor.execute(mcdPreviewThread);
            log.info("活动:{},渠道：{}已经提交预演任务", preview.getCampsegId(), preview.getChannelId());
        } catch (Exception e) {
            log.error("提交预演活动:{}任务异常:", preview.getCampsegId(), e);
        } finally {
            if (RedisUtils.releaseRedisLock(lockKey, lockId)) {
                log.info("成功释放预演redis锁:{},lockId:({})", lockKey, lockId);
            } else {
                log.error("未能成功释放预演redis锁:{},lockId:({}),该异常会导致2分钟内无法处理，请手动删除该锁......", lockKey, lockId);
            }
        }

    }

    /**
     * 更新活动义表状态
     *
     * @param previewInfo   活动
     * @param campsegStatId 目标状态
     * @param curStatId     当前状态
     * @return
     */
    @Override
    public int updateCampsegStatus(McdPreviewInfo previewInfo, int campsegStatId, int curStatId) {
        return previewMapper.updateCampsegStatus(previewInfo.getCampsegPid(), previewInfo.getCampsegRootId(), campsegStatId, curStatId);
    }

    /**
     * 更新channel_list表状态
     *
     * @param previewInfo   活动预演信息
     * @param campsegStatId 状态
     * @return
     */
    @Override
    public int updateCampChannelListStatus(McdPreviewInfo previewInfo, int campsegStatId, int curStatId) {
        return previewMapper.updateCampChannelListStatus(previewInfo.getCampsegId(), campsegStatId, campsegStatId);
    }

    /**
     * 更新预演表状态
     *
     * @param previewInfo
     * @param status
     * @return
     */
    @Override
    public boolean updateCampPreviewStatus(McdPreviewInfo previewInfo, int status) {
        UpdateWrapper<McdCampPreview> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(McdCampPreview::getPreviewStatus, status)
                .eq(McdCampPreview::getCampsegId, previewInfo.getCampsegId())
                .eq(McdCampPreview::getChannelId, previewInfo.getChannelId());
        return this.update(wrapper);
    }

    /**
     * 是否活动的所有渠道预演完成
     *
     * @param campsegRootId 活动ID
     * @return
     */
    @Override
    public boolean isAllChannelPreviewFinish(String campsegRootId) {
        LambdaQueryWrapper<McdCampPreview> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(McdCampPreview::getChannelId)
                .eq(McdCampPreview::getCampsegRootId, campsegRootId)
                .ne(McdCampPreview::getPreviewStatus, PreviewConst.PREVIEW_STATUS_DONE);
        List<McdCampPreview> list = this.list(queryWrapper);
        return list.size() == 0;
    }

    @Override
    public String getFqcFilterChnIds() {
        return fqcFilterChnIds;
    }

    /**
     * 获取需要已推荐过滤的需求ID
     *
     * @return
     */
    @Override
    public String getRecommendFilterChnIds() {
        return recommendFilterChnIds;
    }

    /**
     * 查询执行前预演结果
     *
     * @param campsegRootId 根活动ID
     * @return
     */
    @Override
    public List<McdCampPreviewResult> queryPreviewLog(String campsegRootId, String campsegId) {
        List<McdCampPreviewResult> results = previewMapper.queryPreviewLog(campsegRootId, campsegId);
        for (McdCampPreviewResult result : results) {
            addSucessRateData(result);
        }
        return results;
    }

    /**
     * 添加转换率预测数据
     *
     * @param result
     */
    private void addSucessRateData(McdCampPreviewResult result) {
        try {
            Double planSuccessRate = result.getPlanSuccessRate();
            // 产品转化率预测
            if (planSuccessRate != null) {
                result.setPlanAccuracyRate(StrUtil.concat(true, NumberUtil.roundStr(planSuccessRate * 100, 2), "%"));
            } else {
                result.setPlanAccuracyRate(NO_DATA);
            }
            Double channelSuccessRate = result.getChannelSuccessRate();
            // 产品转化率预测
            if (channelSuccessRate != null) {
                result.setContactCoverage(StrUtil.concat(true, NumberUtil.roundStr(channelSuccessRate * 100, 2), "%"));
            } else {
                result.setContactCoverage(NO_DATA);
            }
            // 综合转化率
            Double successRate = result.getSuccessRate();
            if (successRate != null) {
                result.setSucessRate(StrUtil.concat(true, NumberUtil.roundStr(successRate * 100, 2), "%"));
            } else {
                result.setSucessRate(NO_DATA);
            }
        } catch (Exception e) {
            log.error("添加转换率预测数据异常:{}", JSONUtil.toJsonStr(result), e);
        }
    }

    /**
     * 查询执行中预演结果
     *
     * @param param
     * @return
     */
    @Override
    public List<McdCampPreviewExecResult> queryPreviewExecLog(PreveiwResultReq param) {
        // 执行中预演过滤用户数据在GP库中，通过pec-log接口查询
        ActionResponse<List<Map<String, String>>> response = previewFilterLogClient.queryPreviewResult(param.getCampsegRootId(), param.getStarDate(), param.getEndDate());
        Map<String, List<Map<String, String>>> resultMap = new HashMap<>();
        response.getData().stream().forEach(map -> {
            List<Map<String, String>> campsegList = resultMap.get(map.get("campseg_id"));
            if (null == campsegList) {
                campsegList = new ArrayList<>();
            }
            campsegList.add(map);
            resultMap.put(map.get("campseg_id"), campsegList);
        });
        // 查询渠道名称，产品名称等基本信息
        List<McdCampPreviewExecResult> tempResults = previewMapper.queryPreviewExecLog(param.getCampsegRootId(), param.getCampsegId());
        List<McdCampPreviewExecResult> execResults = new ArrayList<>();
        tempResults.stream().forEach(result -> {
            List<Map<String, String>> maps = resultMap.get(result.getCampsegId());
            if (CollectionUtil.isEmpty(maps)) {
                return;
            }
            for (Map<String, String> map : maps) {
                McdCampPreviewExecResult execResult = new McdCampPreviewExecResult();
                BeanUtil.copyProperties(result, execResult);
                execResult.setRuleName(map.get("filter_name"));
                execResult.setFilterNum(map.get("filter_num"));
                execResults.add(execResult);
            }
        });
        return execResults;
    }

    /**
     * 通过渠道ID查询 敏感客户群列表
     *
     * @param channelId 渠道ID
     */
    @Override
    public List<McdChannelSensitiveCustConf> getSensitiveCustsByChannleId(String channelId) {

        QueryWrapper<McdChannelSensitiveCustConf> wrapper = new QueryWrapper();
        wrapper.lambda().eq(McdChannelSensitiveCustConf::getChannelId, channelId);
        return sensitiveCustConfService.list(wrapper);
    }

}
