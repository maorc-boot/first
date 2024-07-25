package com.asiainfo.biapp.pec.preview.jx.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.client.jx.preview.model.McdCampPreview;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampPreviewExecResult;
import com.asiainfo.biapp.pec.preview.jx.model.McdCampPreviewResult;
import com.asiainfo.biapp.pec.preview.jx.model.PreveiwResultReq;
import com.asiainfo.biapp.pec.preview.jx.service.IChnPreService;
import com.asiainfo.biapp.pec.preview.jx.service.McdCampPreviewService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author mamp
 * @date 2022/9/22
 */
@Slf4j
@RestController
@Api(tags = "江西预演Api")
@RequestMapping("/jx/preview")
public class PreviewJxController {

    @Autowired
    private McdCampPreviewService campPreviewService;
    @Autowired
    private IChnPreService chnPreService;


    private ExecutorService previewExecutor = ThreadUtil.newExecutor(1);
    /**
     * 客户群渠道偏好数据计算
     */
    private ExecutorService custChnPreCalExecutor = ThreadUtil.newExecutor(1);



    @ApiOperation(value = "开启预演任务", notes = "开启预演任务")
    @PostMapping("/startPreview")
    public ActionResponse startPreview() {
        ActionResponse response = ActionResponse.getSuccessResp();
        try {
            previewExecutor.execute(() -> {
                Thread.currentThread().setName("预演任务线程");
                campPreviewService.startPreview();
            });
        } catch (Exception e) {
            response.setStatus(ResponseStatus.ERROR);
            log.error("开启预演任务失败:", e);
        }
        return response;
    }



    @ApiOperation(value = "客户群偏好数据计算", notes = "客户群偏好数据计算")
    @PostMapping("/custChnPreCal")
    public ActionResponse custChnPreCal(@RequestParam("custGroupId") String custGroupId, @RequestParam("dataDate") String dataDate, @RequestParam("custFileName") String custFileName) {
        log.info("客户群渠道偏好计算,custgroupId:{},dataDate:{},custFileName:{}", custGroupId, dataDate, custFileName);
        ActionResponse response = ActionResponse.getFaildResp();
        long start = System.currentTimeMillis();
        try {
            custChnPreCalExecutor.execute(() -> {
                Thread.currentThread().setName("客户群偏好数据计算任务线程");
                chnPreService.custChnPreCal(custGroupId, dataDate, custFileName);
            });
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.ERROR);
            log.error("调度执行客户群偏好数据计算任务处理失败，请求参数:{}", "", e);
        } finally {
            log.info("调度执行客户群偏好数据计算任务请求，请求参数:{}，返回参数:{}，耗时:{}",
                    "",
                    JSONUtil.toJsonStr(response),
                    System.currentTimeMillis() - start);
        }
        return response;
    }



    @ApiOperation(value = "通过活动rootId删除预演数据", notes = "通过活动rootId删除预演数据")
    @PostMapping("/deletePreviewByRootId")
    public ActionResponse deletePreviewByRootId(@RequestParam("campsegRootId") String campsegRootId) {
        log.info("通过活动rootId删除预演数据,campsegRootId:{}", campsegRootId);
        ActionResponse response = ActionResponse.getSuccessResp("通过活动rootId删除预演数据成功");
        try {
            LambdaQueryWrapper<McdCampPreview> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(McdCampPreview::getCampsegRootId,campsegRootId);
            campPreviewService.remove(queryWrapper);
        } catch (Exception e) {
            log.error("通过活动rootId删除预演数据数据异常,campsegRootId:{}",campsegRootId, e);
            response = ActionResponse.getFaildResp(e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "保存预演数据", notes = "保存预演数据")
    @PostMapping("/savePreview")
    public ActionResponse createPreview(@RequestBody List<McdCampPreview> mcdCampPreviews) {
        log.info("保存预演入参:{}", mcdCampPreviews);
        ActionResponse response = ActionResponse.getSuccessResp("保存预演数据成功");
        try {
            campPreviewService.saveBatch(mcdCampPreviews);
        } catch (Exception e) {
            log.error("保存预演数据异常:", e);
            response = ActionResponse.getFaildResp(e.getMessage());
        }
        return response;
    }


    @ApiOperation(value = "执行前预演结果查询", notes = "执行前预演结果查询,入参数campsegRootId:根活动ID")
    @PostMapping("/queryPreviewLog")
    public ActionResponse<List<McdCampPreviewResult>> queryPreviewLog(@RequestParam("campsegRootId") String campsegRootId, @RequestParam(value = "campsegId", required = false) String campsegId) {
        log.info("执行前预演结果查询,campsegRootId :{}", campsegRootId);
        ActionResponse response = ActionResponse.getSuccessResp("执行前预演结果查询成功");
        try {
            response.setData(campPreviewService.queryPreviewLog(campsegRootId, campsegId));
        } catch (Exception e) {
            log.error("执行前预演结果查询异常:", e);
            response = ActionResponse.getFaildResp(e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "执行中预演结果查询", notes = "执行中预演结果查询")
    @PostMapping("/queryPreviewExecLog")
    public ActionResponse<List<McdCampPreviewExecResult>> queryPreviewExecLog(@RequestBody PreveiwResultReq param) {
        log.info("执行中预演结果查询,param :{}", param);
        ActionResponse<List<McdCampPreviewExecResult>> response = ActionResponse.getSuccessResp("执行中预演结果查询成功");
        try {
            response.setData(campPreviewService.queryPreviewExecLog(param));
        } catch (Exception e) {
            log.error("执行中预演结果查询异常:", e);
            response = ActionResponse.getFaildResp(e.getMessage());
        }
        return response;
    }

}
