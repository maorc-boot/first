package com.asiainfo.biapp.pec.plan.jx.intellpushprism.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna.LabelFissionGetCustIdReqDTO;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.McdPrismMyAttentionPlan;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.IIntellPushPrismAIService;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.IMcdPrismMyAttentionPlanService;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * description: 智推棱镜-AI流程控制层
 *
 * @author: lvchaochao
 * @date: 2024/5/25
 */
@RestController
@RequestMapping("/api/intellpushprism/ai")
@Api(value = "智推棱镜-AI流程", tags = {"智推棱镜-AI流程控制层"})
@Slf4j
public class IntellPushPrismAIController {

    @Autowired
    private IIntellPushPrismAIService intellPushPrismAIService;

    @Autowired
    private IMcdPrismMyAttentionPlanService mcdPrismMyAttentionPlanService;

    @Autowired
    private HttpServletRequest request;

    /**
     * dna客群更新通知线程池
     */
    private ExecutorService dealPlanTagRelDegreeExecutor = ThreadUtil.newExecutor(1);

    /**
     * dna客群清单文件生成通知线程池
     */
    private ExecutorService dnaCustFileGeneratedNoticeExecutor = ThreadUtil.newExecutor(1);

    @ApiOperation(value = "获取根据标签裂变的客群信息", notes = "AI推理-获取根据标签裂变的客群信息---暂时不用")
    @PostMapping("/getFissionCustInfo")
    public ActionResponse getFissionCustInfo(@RequestBody LabelFissionGetCustIdReqDTO reqVO) {
        log.info("AI推理-获取根据标签裂变的客群信息入参={}", JSONUtil.toJsonStr(reqVO));
        return intellPushPrismAIService.getFissionCustInfo(reqVO);
    }

    @ApiOperation(value = "获取根据标签裂变的客群对应的规则", notes = "AI推理-获取根据标签裂变的客群对应的规则---暂时不用")
    @PostMapping("/getFissionCustRule")
    public ActionResponse getFissionCustRule(@RequestBody LabelFissionGetCustIdReqDTO reqVO) {
        log.info("AI推理-获取根据标签裂变的客群对应的规则入参={}", JSONUtil.toJsonStr(reqVO));
        return intellPushPrismAIService.getFissionCustInfo(reqVO);
    }

    @ApiOperation(value = "AI执行推理任务", notes = "AI执行推理任务获取推荐产品、客群、渠道")
    @PostMapping("/getInferenceResByAI")
    public ActionResponse getInferenceResByAI(@RequestBody AISaveTaskReqVO reqVO) {
        log.info("AI执行推理任务入参={}", JSONUtil.toJsonStr(reqVO, new JSONConfig().setIgnoreNullValue(true)));
        return intellPushPrismAIService.getInferenceResByAI(reqVO);
    }

    @ApiOperation(value = "客群打分区间数据查询", notes = "AI推理-根据产品、客群查询得分区间(产品-客群一对一)")
    @PostMapping("/queryScoreRange")
    public ActionResponse queryScoreRange(@RequestBody QueryScoreRangeReqVO reqVO) {
        log.info("客群打分区间数据查询入参={}", JSONUtil.toJsonStr(reqVO));
        return intellPushPrismAIService.queryScoreRange(reqVO);
    }

    @ApiOperation(value = "客群打分区间数据批量查询", notes = "AI推理-根据产品、客群批量查询得分区间(产品-客群多对多)")
    @PostMapping("/queryScoreRangeBatch")
    public ActionResponse queryScoreRangeBatch(@RequestBody QueryScoreRangeReqBatchVO reqVO) {
        log.info("客群打分区间数据批量查询入参={}", JSONUtil.toJsonStr(reqVO));
        return intellPushPrismAIService.queryScoreRangeBatch(reqVO);
    }

    @ApiIgnore // 此注解作用是：可以用在类、方法上，方法参数中，用来屏蔽某些接口或参数，使其不在页面上显示
    @ApiOperation(value = "智慧大脑产品同步IOP", notes = "AI推理-智慧大脑产品同步IOP(智慧大脑侧调用)")
    @PostMapping("/syncAIProduct")
    public ActionResponse syncAIProduct(@RequestBody SyncAIProductReqVO reqVO) {
        log.info("智慧大脑产品同步IOP入参={}", JSONUtil.toJsonStr(reqVO));
        intellPushPrismAIService.syncAIProduct(reqVO);
        return ActionResponse.getSuccessResp();
    }

    @ApiIgnore
    @ApiOperation(value = "智慧大脑推荐客群回调", notes = "AI推理-智慧大脑推荐客群回调(智慧大脑侧调用)")
    @PostMapping("/callbackTask")
    public ActionResponse callbackTask(@RequestBody CallbackTaskReqVO reqVO) {
        log.info("智慧大脑推荐客群回调入参={}", JSONUtil.toJsonStr(reqVO));
        return intellPushPrismAIService.callbackTask(reqVO);
    }

    @ApiIgnore
    @ApiOperation(value = "客群清单文件生成通知接口", notes = "客群清单文件生成通知接口(针对数值型标签)")
    @PostMapping("/dnaCustFileGeneratedNotice")
    public ActionResponse dnaCustFileGeneratedNotice(@RequestBody JSONObject request) {
        log.info("DNA客群清单文件生成通知开始, 入参={}", JSONUtil.toJsonStr(request));
        ActionResponse response = ActionResponse.getFaildResp();
        try {
            dnaCustFileGeneratedNoticeExecutor.execute(() -> {
                intellPushPrismAIService.dnaCustFileGeneratedNotice(request);
            });
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("DNA客群清单文件生成通知失败：", e);
        }
        return response;
    }

    @ApiOperation(value = "获取该产品最近5个活动的成功用户数和转化率", notes = "获取该产品最近5个活动的成功用户数和转化率")
    @PostMapping("/getCampEvalTop5ByPlan")
    public ActionResponse getCampEvalTop5ByPlan(@RequestBody McdIdQuery query) {
        log.info("获取该产品最近5个活动的成功用户数和转化率入参={}", JSONUtil.toJsonStr(query));
        return intellPushPrismAIService.getCampEvalTop5ByPlan(query);
    }

    @ApiOperation(value = "获取该产品AI画像标签TOP10", notes = "获取该产品AI画像标签TOP10")
    @PostMapping("/getAITagTop10ByPlan")
    public ActionResponse getAITagTop10ByPlan(@RequestBody McdIdQuery query) {
        log.info("获取该产品AI画像标签TOP10入参={}", JSONUtil.toJsonStr(query));
        return intellPushPrismAIService.getAITagTop10ByPlan(query);
    }

    @ApiOperation(value = "处理智推棱镜AI推理场景产品标签关联度数据", notes = "处理智推棱镜AI推理场景产品标签关联度数据")
    @PostMapping("/dealPlanTagRelDegree")
    public ActionResponse dealPlanTagRelDegree(@RequestBody JSONObject request) {
        log.info("处理智推棱镜AI推理场景产品标签关联度数据任务...");
        ActionResponse response = ActionResponse.getFaildResp();
        try {
            dealPlanTagRelDegreeExecutor.execute(() -> {
                intellPushPrismAIService.dealPlanTagRelDegree(request);
            });
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.error("处理智推棱镜AI推理场景产品标签关联度数据任务失败", e);
        }
        return response;
    }

    @ApiOperation(value = "添加我关注的产品", notes = "添加我关注的产品")
    @PostMapping("/addMyAttentionPlan")
    public ActionResponse addMyAttentionPlan(@RequestBody List<McdPrismMyAttentionPlan> mcdPrismMyAttentionPlan) {
        log.info("添加我关注的产品入参={}", JSONUtil.toJsonStr(mcdPrismMyAttentionPlan));
        boolean saveBatch = mcdPrismMyAttentionPlanService.saveOrUpdateBatch(mcdPrismMyAttentionPlan);
        if (saveBatch) {
            return ActionResponse.getSuccessResp("添加我关注的产品成功");
        } else {
            return ActionResponse.getFaildResp("添加我关注的产品失败");
        }
    }

    @ApiOperation(value = "删除我关注的产品", notes = "删除我关注的产品")
    @PostMapping("/removeMyAttentionPlan")
    public ActionResponse removeMyAttentionPlan(@RequestBody List<McdPrismMyAttentionPlan> mcdPrismMyAttentionPlan) {
        log.info("删除我关注的产品入参={}", JSONUtil.toJsonStr(mcdPrismMyAttentionPlan));
        List<String> planIds = mcdPrismMyAttentionPlan.stream().map(McdPrismMyAttentionPlan::getPlanId).collect(Collectors.toList());
        boolean removeByIds = mcdPrismMyAttentionPlanService.removeByIds(planIds);
        if (removeByIds) {
            return ActionResponse.getSuccessResp("删除我关注的产品成功");
        } else {
            return ActionResponse.getFaildResp("删除我关注的产品失败");
        }
    }

    @ApiOperation(value = "分页查询我关注的产品", notes = "分页查询我关注的产品")
    @PostMapping("/queryMyAttentionPlan")
    public ActionResponse queryMyAttentionPlan(@RequestBody McdPageQuery query) {
        String userId = UserUtil.getUserId(request);
        log.info("用户={}分页查询我关注的产品入参={}", userId, JSONUtil.toJsonStr(query));
        Page<McdPrismMyAttentionPlan> page = new Page<>(query.getCurrent(), query.getSize());
        Page<McdPrismMyAttentionPlan> mcdPrismMyAttentionPlanPage = mcdPrismMyAttentionPlanService.page(page, Wrappers.<McdPrismMyAttentionPlan>query().lambda()
                .like(StrUtil.isNotEmpty(query.getKeyWords()), McdPrismMyAttentionPlan::getPlanId, query.getKeyWords())
                .or()
                .like(StrUtil.isNotEmpty(query.getKeyWords()), McdPrismMyAttentionPlan::getPlanName, query.getKeyWords())
                .eq(McdPrismMyAttentionPlan::getCreateUserId, userId));
        return ActionResponse.getSuccessResp(mcdPrismMyAttentionPlanPage);
    }

    @ApiOperation(value = "查询AI客群瘦身分析结果", notes = "根据产品查询AI客群瘦身分析结果")
    @PostMapping("/queryAICustLoseWeightRes")
    public ActionResponse queryAICustLoseWeightRes(@RequestBody AICustLoseWeightReqVO query) {
        log.info("根据产品查询AI客群瘦身分析结果入参={}", JSONUtil.toJsonStr(query));
        return intellPushPrismAIService.queryAICustLoseWeightRes(query);
    }

    @ApiOperation(value = "测试接口-手动执行推理任务", notes = "测试接口-手动执行推理任务")
    @GetMapping("/testHandelExec")
    public ActionResponse testHandelExec(@RequestParam("aiTaskIds") String aiTaskIds, @RequestParam("endDate") String endDate) {
        return intellPushPrismAIService.testHandelExec(aiTaskIds, endDate);
    }
}
