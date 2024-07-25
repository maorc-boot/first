package com.asiainfo.biapp.pec.plan.jx.camp.controller;


import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampImportTask;
import com.asiainfo.biapp.pec.plan.jx.camp.req.ImportTaskQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdCampImportTaskService;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/mcd-camp-import-task")
@Slf4j
@Api(value = "江西:批量导入生成活动", tags = "江西:批量导入生成活动")
public class McdCampImportTaskController {
    @Autowired
    private McdCampImportTaskService importTaskService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 批量导入策略
     *
     * @return
     */
    @PostMapping(path = "/batchImportCamp")
    @ApiOperation(value = "批量导入活动", notes = "批量导入活动")
    public ActionResponse<Boolean> uploadPlanExcluFile(@RequestParam("file") MultipartFile file
            , @RequestParam("channelId") String channelId, @RequestParam("taskName") String taskName, @RequestParam("taskDesc") String taskDesc) {
        McdCampImportTask task = new McdCampImportTask();
        task.setId(IdUtils.generateId());
        task.setTaskDescription(taskDesc);
        task.setTaskName(taskName);
        task.setTaskNo(StrUtil.format("I_{}_{}", channelId, task.getId()));
        ActionResponse response = ActionResponse.getSuccessResp();
        try {
            importTaskService.loadCampFromFile(file, task, UserUtilJx.getUser(request));

        } catch (Exception e) {
            log.error("批量导入活动失败:", e);
            response = ActionResponse.getFaildResp(e.getMessage());
        }
        return response;
    }

    /**
     * 批量导入策略
     *
     * @return
     */
    @PostMapping(path = "/queryTask")
    @ApiOperation(value = "分页查询批量导入任务", notes = "分页查询批量导入任务")
    public ActionResponse<Page<McdCampImportTask>> queryTask(@RequestBody ImportTaskQuery query) {

        log.info("queryTask::{}", query);
        ActionResponse response = ActionResponse.getSuccessResp();
        try {
            response.setData(importTaskService.queryTask(query));
        } catch (Exception e) {
            log.error("分页查询批量导入任务失败:", e);
            response = ActionResponse.getFaildResp(e.getMessage());
        }
        return response;
    }

}

