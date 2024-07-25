package com.asiainfo.biapp.pec.preview.jx.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.preview.jx.query.McdCampUserCountQuery;
import com.asiainfo.biapp.pec.preview.jx.service.McdCampUserCountTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * IOP策略用户数统计
 *
 * @author: mamp
 * @date: 2024/7/18
 */
@Slf4j
@RestController
@Api(tags = "IOP策略用户数统计")
@RequestMapping("/campUserCount")
public class McdCampUserCountController {

    private ExecutorService userCountTaskExecutor = ThreadUtil.newExecutor(1);


    @Resource
    private McdCampUserCountTaskService mcdCampUserCountTaskService;

    @ApiOperation(value = "开启IOP策略用户数统计任务", notes = "开启IOP策略用户数统计任务")
    @PostMapping("/startTask")
    public ActionResponse startPreview(@RequestBody McdCampUserCountQuery query) {
        ActionResponse response = ActionResponse.getSuccessResp();
        try {
            userCountTaskExecutor.execute(() -> {
                Thread.currentThread().setName("IOP策略用户数统计任务");
                mcdCampUserCountTaskService.startTask(query);
            });
        } catch (Exception e) {
            response.setStatus(ResponseStatus.ERROR);
            log.error("IOP策略用户数统计任务启动失败:", e);
        }
        return response;
    }
}
