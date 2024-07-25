package com.asiainfo.biapp.pec.preview.jx.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.service.IBitMap;
import com.asiainfo.biapp.pec.common.jx.service.impl.BitMapRoaringImp;
import com.asiainfo.biapp.pec.common.jx.util.BitmapCacheUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.preview.jx.service.IChnBwlService;
import com.asiainfo.biapp.pec.preview.jx.service.IChnPreService;
import com.asiainfo.biapp.pec.preview.jx.service.ISensitiveCustService;
import com.asiainfo.biapp.pec.preview.jx.service.PlanPreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * @author mamp
 * @date 2023/1/31
 */
@Slf4j
@RestController
@Api(tags = "江西渠道偏好,免打扰,敏感客户群BitMap缓存相关接口")
@RequestMapping("/jx/chnBitMap")
public class ChnBitMapcontroller {


    @Resource
    private IChnPreService chnPreService;
    @Resource
    private IChnBwlService bwlService;
    @Resource
    private ISensitiveCustService sensitiveCustService;
    @Resource
    private PlanPreService planPreService;


    /**
     * 更新第一渠道渠道偏好数据（每月更新一次渠道偏好数据）
     */
    private ExecutorService updateChnFirstPreDataExecutor = ThreadUtil.newExecutor(1);

    /**
     * 更新免打扰数据
     */
    private ExecutorService updateBwlDataExecutor = ThreadUtil.newExecutor(1);

    /**
     * 更新免打扰数据
     */
    private ExecutorService updateSensitiveDataExecutor = ThreadUtil.newExecutor(1);

    @ApiOperation(value = "更新第一渠道偏好模型数据", notes = "更新第一渠道偏好模型数据")
    @PostMapping("/updateFirstChnPre")
    public ActionResponse updateFirstChnPre(@RequestBody JSONObject request) {
        ActionResponse response = ActionResponse.getFaildResp();
        long start = System.currentTimeMillis();
        try {
            updateChnFirstPreDataExecutor.execute(() -> {
                Thread.currentThread().setName("更新第一渠道偏好模型数据任务线程");
                chnPreService.updateChnPreModel();
            });
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.ERROR);
            log.error("调度执行更新第一渠道偏好模型数据任务处理失败，请求参数:{}", "", e);
        } finally {
            log.info("调度执行更新第一渠道偏好模型数据任务请求，请求参数:{}，返回参数:{}，耗时:{}",
                    "",
                    JSONUtil.toJsonStr(response),
                    System.currentTimeMillis() - start);
        }
        return response;
    }

    @ApiOperation(value = "更新第二渠道偏好模型数据", notes = "更新第二渠道偏好模型数据")
    @PostMapping("/updateSecondChnPre")
    public ActionResponse updateSecondChnPre(@RequestBody JSONObject request) {
        ActionResponse response = ActionResponse.getSuccessResp();
        return response;
    }

    @ApiOperation(value = "刷新免打扰缓存bitmap数据", notes = "刷新免打扰缓存bitmap数据")
    @PostMapping("/refreshBwlBitmap")
    public ActionResponse refreshBwlBitmap(@RequestBody JSONObject request) {
        log.info("刷新免打扰缓存bitmap数据开始...");
        ActionResponse response = ActionResponse.getSuccessResp("刷新免打扰缓存bitmap数据成功");
        String channelId = request.getStr("channelId");
        try {
            updateBwlDataExecutor.execute(() -> {
                Thread.currentThread().setName("更新打扰缓存bitmap任务线程");
                bwlService.refreshBwlBitmap(channelId);
            });

        } catch (Exception e) {
            log.error("刷新免打扰缓存bitmap数据异常:", e);
            response = ActionResponse.getFaildResp();
        }
        return response;
    }

    @ApiOperation(value = "刷新敏感客户群缓存bitmap数据", notes = "刷新敏感客户群缓存bitmap数据")
    @PostMapping("/refreshSensitiveBitmap")
    public ActionResponse refreshSensitiveBitmap(@RequestBody JSONObject request) {
        log.info("刷新敏感客户群缓存bitmap数据开始...");
        String channelId = request.getStr("channelId");
        ActionResponse response = ActionResponse.getSuccessResp("刷新敏感客户群缓存bitmap数据成功");
        try {
            updateSensitiveDataExecutor.execute(() -> {
                Thread.currentThread().setName("更新敏感客户群缓存bitmap任务线程");
                try {
                    sensitiveCustService.refreshSensitiveBitmap(channelId);
                } catch (Exception e) {
                    log.error("更新敏感客户群缓存bitmap异常:", e);
                }
            });

        } catch (Exception e) {
            log.error("更新敏感客户群缓存bitmap数据异常:", e);
            response = ActionResponse.getFaildResp();
        }
        return response;
    }

    @ApiOperation(value = "刷新产品偏好bitmap数据", notes = "刷新产品偏好bitmap数据")
    @PostMapping("/refreshPlanPreBitmap")
    public ActionResponse refreshPlanPreBitmap(@RequestBody JSONObject request) {
        log.info("刷新产品偏好bitmap数据开始...");
        String channelId = request.getStr("channelId");
        ActionResponse response = ActionResponse.getSuccessResp("刷新敏感客户群缓存bitmap数据成功");
        try {
            updateSensitiveDataExecutor.execute(() -> {
                Thread.currentThread().setName("更新敏感客户群缓存bitmap任务线程");
                try {
                    planPreService.updatePlanPreModel();
                } catch (Exception e) {
                    log.error("刷新产品偏好bitmap异常:", e);
                }
            });

        } catch (Exception e) {
            log.error("刷新产品偏好bitmap数据异常:", e);
            response = ActionResponse.getFaildResp();
        }
        return response;
    }


    @ApiOperation(value = "测试bitmapUtil", notes = "测试bitmapUtil")
    @PostMapping("/测试bitmapUtil")
    public ActionResponse<String> test(@RequestParam("channelId") String channelId) {
        ActionResponse response = ActionResponse.getSuccessResp();
        try {
            IBitMap bitMap = new BitMapRoaringImp(true);
            bitMap.add(13666666666L);
            BitmapCacheUtil.pushChnPreBitmap(channelId, bitMap);
            response.setData("成功");
        } catch (Exception e) {
            response.setStatus(ResponseStatus.ERROR);
            log.error("", e);
        }
        return response;
    }

}
