package com.asiainfo.biapp.pec.plan.jx.hisdata.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.OutInterface;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.plan.jx.hisdata.service.CmpApproveProcessRecordHisService;
import com.asiainfo.biapp.pec.plan.jx.hisdata.service.HisDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2023/5/20
 */
@RestController
@RequestMapping("/hisdata")
@Slf4j
@Api(value = "江西:4.0历史数据迁移到7.0", tags = "江西:4.0历史数据迁移到7.0")
public class HisDataController {

    @Resource
    private HisDataService hisDataService;

    @Resource
    private CmpApproveProcessRecordHisService recordHisService;

    @GetMapping("/camp")
    @ApiOperation(value = "活动数据迁移", tags = "活动数据迁移")
    ActionResponse handle(@RequestParam(value = "stat", required = false) String stat,
                          @RequestParam("channelId") String channelId,
                          @RequestParam(value = "campsegId", required = false) String campsegId
    ) {
        log.info("活动数据迁移,channelId:{},stat:{},campsegId:{}", channelId, stat, campsegId);
        ActionResponse resp = ActionResponse.getSuccessResp();
        if (StrUtil.isEmpty(channelId)) {
            resp.setStatus(ResponseStatus.ERROR);
            resp.setMessage("渠道ID不能为空");
            return resp;
        }
        hisDataService.handleCamp(stat, channelId, campsegId);
        return resp;
    }

    @GetMapping("/record")
    @ApiOperation(value = "审批记录迁移", tags = "活动数据迁移")
    ActionResponse handleApproveRecord(@RequestParam("approveFlowId") String approveFlowId) {
        ActionResponse resp = ActionResponse.getSuccessResp();
        if (StrUtil.isEmpty(approveFlowId)) {
            resp.setStatus(ResponseStatus.ERROR);
            resp.setMessage("审批flowID不能为空");
            return resp;
        }
        hisDataService.handleApproveRecord(approveFlowId);
        return resp;
    }

    @GetMapping("/listRecord")
    @ApiOperation(value = "通过活动ID查询审批记录", tags = "通过活动ID查询审批记录")
    ActionResponse listRecord(@RequestParam("businessId") String businessId) {
        log.info("查询审批记录,businessId:{}", businessId);
        ActionResponse resp = ActionResponse.getSuccessResp();
        if (StrUtil.isEmpty(businessId)) {
            resp.setStatus(ResponseStatus.ERROR);
            resp.setMessage("businessId不能为空");
            return resp;
        }

        try {
            resp.setData(recordHisService.listRecord(businessId));
        } catch (Exception e) {
            log.error("查询审批记录异常,businessId:{}", businessId, e);
            resp.setStatus(ResponseStatus.ERROR);
        }
        return resp;
    }

    @GetMapping("/delCamp")
    @ApiOperation(value = "删除活动", tags = "删除活动")
    ActionResponse delCamp(@RequestParam(value = "campsegRootId") String campsegRootId,
                           @RequestParam(value = "channelId", required = false) String channelId) {
        log.info("删除活动,campsegRootId:{}", campsegRootId);
        ActionResponse resp = ActionResponse.getSuccessResp();
        if (StrUtil.isEmpty(campsegRootId)) {
            resp.setStatus(ResponseStatus.ERROR);
            resp.setMessage("campsegRootId不能为空");
            return resp;
        }
        try {
            resp.setData(hisDataService.deleteCamp(campsegRootId,channelId));
        } catch (Exception e) {
            log.error("删除活动异常,campsegRootId:{},campsegId:{}", campsegRootId, e);
            resp.setStatus(ResponseStatus.ERROR);
        }
        return resp;
    }


    @GetMapping("/getCampInfo")
    @ApiOperation(value = "获取活动信息-客户通渠道", tags = "获取活动信息-客户通渠道")
    @OutInterface
    public List<Map<String, Object>> getCampInfoList(@RequestParam("stat") int stat) {
        log.info("获取客户通渠道活动信息活动状态stat:{}", stat);
        List<Map<String, Object>> respMap = new ArrayList<>();
        try {
            respMap = hisDataService.queryCampInfoList(stat);
        } catch (Exception e) {
            log.error("获取客户通渠道活动信息失败", e);
        }
        return respMap;
    }


    @GetMapping("/getMcdCampTask")
    @ApiOperation(value = "获取任务信息-客户通渠道", tags = "获取任务信息-客户通渠道")
    @OutInterface
    public Map<String, Object> getMcdCampTask(@RequestParam("campsegId") String campsegId,
                                             @RequestParam("channelId") String channelId) {
        log.info("获取客户通渠道任务信息campsegId:{}, channelId:{}", campsegId,channelId);
        Map<String, Object> respMap = new HashMap<>();
        try {
            respMap = hisDataService.queryCampTaskInfo(campsegId, channelId);
        } catch (Exception e) {
            log.error("获取客户通渠道活动信息失败", e);
        }
        return respMap;
    }

    @GetMapping("/getMcdCampTaskDate")
    @ApiOperation(value = "获取任务信息-客户通渠道", tags = "获取任务信息-客户通渠道")
    @OutInterface
    public Map<String, Object> getMcdCampTaskDate(@RequestParam("taskId") String taskId) {
        log.info("获取客户通渠道任务信息getMcdCampTaskDate,taskId:{}", taskId);
        Map<String, Object> respMap = new HashMap<>();
        try {
            respMap = hisDataService.queryMcdCampTaskDate(taskId);
        } catch (Exception e) {
            log.error("获取客户通渠道活动信息失败", e);
        }
        return respMap;
    }


    @GetMapping("/getMcdCampChannelListInfo")
    @ApiOperation(value = "获取活动渠道信息-客户通渠道", tags = "获取活动渠道信息-客户通渠道")
    @OutInterface
    public Map<String, Object> getMcdCampChannelListInfo(@RequestParam("channelId") String channelId,
                                                         @RequestParam("campsegId") String campsegId) {
        log.info("获取客户通活动和渠道信息getMcdCampChannelListInfo,channelId:{}, campsegId:{}", channelId,campsegId);
        Map<String, Object> respMap = new HashMap<>();
        try {
            respMap = hisDataService.queryMcdCampChannelListInfo(campsegId,channelId);
        } catch (Exception e) {
            log.error("获取客户通活动和渠道信息失败", e);
        }
        return respMap;
    }

    @GetMapping("/getCampFusionPlan")
    @ApiOperation(value = "获取活动融合产品信息-客户通渠道", tags = "获取活动融合产品信息-客户通渠道")
    @OutInterface
    public List<Map<String, Object>> getCampFusionPlan(@RequestParam("campsegId") String campsegId) {
        log.info("获取活动融合产品信息getCampFusionPlan, campsegId:{}", campsegId);
        List<Map<String, Object>> respMapList = new ArrayList<>();
        try {
            respMapList = hisDataService.queryCampFusionPlanList(campsegId);
        } catch (Exception e) {
            log.error("获取活动融合产品信息失败", e);
        }
        return respMapList;
    }

    @GetMapping("/getCampSeriesPlan")
    @ApiOperation(value = "获取活动同系列产品信息-客户通渠道", tags = "获取活动同系列产品信息-客户通渠道")
    @OutInterface
    public List<Map<String, Object>> getCampSeriesPlan(@RequestParam("campsegId") String campsegId) {
        log.info("获取活动同系列产品信息getCampSeriesPlan, campsegId:{}", campsegId);
        List<Map<String, Object>> respMapList = new ArrayList<>();
        try {
            respMapList = hisDataService.queryCampSeriesPlanList(campsegId);
        } catch (Exception e) {
            log.error("获取活动同系列产品信息失败", e);
        }
        return respMapList;
    }

    @GetMapping("/getCampExclusivePlan")
    @ApiOperation(value = "获取活动互斥产品信息-客户通渠道", tags = "获取活动互斥产品信息-客户通渠道")
    @OutInterface
    public List<Map<String, Object>> getCampExclusivePlan(@RequestParam("campsegId") String campsegId) {
        log.info("获取活动互斥产品信息getCampExclusivePlan, campsegId:{}", campsegId);
        List<Map<String, Object>> respMapList = new ArrayList<>();
        try {
            respMapList = hisDataService.queryCampExclusivePlanList(campsegId);
        } catch (Exception e) {
            log.error("获取活动互斥产品信息失败", e);
        }
        return respMapList;
    }



}
