package com.asiainfo.biapp.pec.plan.jx.hmh5.controller;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.enums.Hmh5BlacklistEnum;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdHmh5BlacklistTask;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdHmh5BlackListTaskQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdHmh5BlacklistTaskService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.BlacklistApprRecord;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.BlacklistApproveJxQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.ModifyBlacklistApprStatusParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 客户通黑名单批量导入任务 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2024-05-28
 */


@Api(tags = "客户通黑名导入")
@DataSource("khtmanageusedb")
@RestController
@RequestMapping("/mcd-hmh5-blacklist-task")
@Slf4j
public class McdHmh5BlacklistTaskController {

    @Resource
    private McdHmh5BlacklistTaskService mcdHmh5BlacklistTaskService;

    @PostMapping("/listTask")
    @ApiOperation("任务列表")
    public ActionResponse<IPage<McdHmh5BlacklistTask>> listTask(@RequestBody McdHmh5BlackListTaskQuery req) {
        QueryWrapper<McdHmh5BlacklistTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StrUtil.isNotEmpty(req.getTaskName()), McdHmh5BlacklistTask::getTaskName, req.getTaskName())
                .like(StrUtil.isNotEmpty(req.getTaskId()), McdHmh5BlacklistTask::getTaskId, req.getTaskId())
                .eq(req.getTaskStatus() != null, McdHmh5BlacklistTask::getTaskStatus, req.getTaskStatus())
                .orderByDesc(McdHmh5BlacklistTask::getCreateTime);
                if(StrUtil.isNotEmpty(req.getStartTime())){
                    queryWrapper.lambda().ge(McdHmh5BlacklistTask::getCreateTime, DateUtil.parse(req.getStartTime(), DatePattern.PURE_DATETIME_FORMAT)) ;
                }
                if (StrUtil.isNotEmpty(req.getEndTime())) {
                    queryWrapper.lambda().le(McdHmh5BlacklistTask::getCreateTime, DateUtil.parse(req.getEndTime(), DatePattern.PURE_DATETIME_FORMAT));
                }
        Page page = new Page(req.getCurrentPage(), req.getPageSize());
        return ActionResponse.getSuccessResp(mcdHmh5BlacklistTaskService.page(page, queryWrapper));
    }

    /**
     * 页面导入添加
     *
     * @param multiFile
     * @return
     */
    @ApiOperation(value = "江西：客户通黑名单任务导入", notes = "江西：客户通黑名单任务导入")
    @ApiImplicitParams({@ApiImplicitParam(name = "phoneFile", value = "导入文件"), @ApiImplicitParam(name = "taskId", value = "任务ID"), @ApiImplicitParam(name = "taskName", value = "任务名称"), @ApiImplicitParam(name = "remark", value = "备注")})
    @PostMapping("createOrUpdate")
    public ActionResponse createOrUpdate(@RequestParam(value = "phoneFile") MultipartFile multiFile, @RequestParam(value = "taskId", required = false) String taskId, @RequestParam(value = "taskName") String taskName, @RequestParam(value = "remark") String remark, HttpServletRequest request) {

        Assert.notNull(multiFile, "导入文件不能为空");
        Assert.isTrue(StringUtils.isNotBlank(multiFile.getOriginalFilename()), "导入文件的文件名为空");
        Assert.isTrue(multiFile.getOriginalFilename().endsWith(".xls"), "请使用正确的文件格式导入");
        UserSimpleInfo user = UserUtil.getUser(request);
        return mcdHmh5BlacklistTaskService.createOrUpdate(multiFile, taskId, taskName, remark, user);
    }

    @PostMapping("/delete")
    @ApiOperation("删除任务")
    public ActionResponse delete(@RequestBody McdHmh5BlacklistTask task) {
        return ActionResponse.getSuccessResp(mcdHmh5BlacklistTaskService.removeById(task.getTaskId()));
    }

    @PostMapping("/getTaskDetailByTaskId")
    @ApiOperation("根据任务id查询任务详情")
    public ActionResponse getTaskDetailByTaskId(@RequestBody McdIdQuery query) {
        McdHmh5BlacklistTask byId = mcdHmh5BlacklistTaskService.getById(query.getId());
        if (ObjectUtil.isNotEmpty(byId)) {
            return ActionResponse.getSuccessResp(byId);
        } else {
            return ActionResponse.getSuccessResp("未查询到相关任务信息");
        }
    }


    @PostMapping("/submitBlacklist")
    @ApiOperation(value = "客户通黑名单提交审批", notes = "客户通黑名单提交审批")
    public ActionResponse submitBlacklist(@RequestBody SubmitProcessQuery req, HttpServletRequest request) {
        log.info("【客户通黑名单】submitBlacklist param:{}", JSONUtil.toJsonStr(req));
        final UserSimpleInfo user = UserUtil.getUser(request);
        mcdHmh5BlacklistTaskService.submitBlacklist(req, user);
        String userName = ObjectUtil.isNull(req.getNextNodesApprover()) ? "" : (ObjectUtil.isNotNull(req.getNextNodesApprover().get(0).getApproverUser()) ? req.getNextNodesApprover().get(0).getApproverUser().get(0).getUserName():"");
        mcdHmh5BlacklistTaskService.update(Wrappers.<McdHmh5BlacklistTask>update().lambda()
                .set(McdHmh5BlacklistTask::getTaskStatus, Hmh5BlacklistEnum.APPROVING.getId())
                .set(McdHmh5BlacklistTask::getApproverName, userName)
                .eq(McdHmh5BlacklistTask::getTaskId, req.getBusinessId()));
        return ActionResponse.getSuccessResp();
    }

    @PostMapping("/getBlacklistApprover")
    @ApiOperation(value = "获取客户通黑名单审批流程实例节点下级审批人",notes = "获取客户通黑名单审批流程实例节点下级审批人")
    public ActionResponse<SubmitProcessJxDTO> getBlacklistApprover(@RequestBody SubmitProcessJxDTO submitProcessDTO){
        log.info("【客户通黑名单】getBlacklistApprover param:{}", JSONUtil.toJsonStr(submitProcessDTO));
        return mcdHmh5BlacklistTaskService.getBlacklistApprover(submitProcessDTO);
    }

    @ApiOperation(value = "客户通黑名单审批列表", notes = "客户通黑名单审批列表")
    @PostMapping("/approveBlacklistRecord")
    public IPage<BlacklistApprRecord> approveBlacklistRecord(@RequestBody BlacklistApproveJxQuery req) {
        log.info("客户通关怀短信模板审批列表para:{}", JSONUtil.toJsonStr(req));
        return mcdHmh5BlacklistTaskService.approveBlacklistRecord(req);
    }

    @ApiIgnore // 此注解作用是：可以用在类、方法上，方法参数中，用来屏蔽某些接口或参数，使其不在页面上显示
    @ApiOperation(value = "客户通黑名单审批流转修改状态")
    @PostMapping("/modifyBlacklistApprStatus")
    public ActionResponse modifyBlacklistApprStatus(@RequestBody ModifyBlacklistApprStatusParam blacklistApprStatusParam) {
        log.info("客户通黑名单审批流转修改状态param={}", JSONUtil.toJsonStr(blacklistApprStatusParam));
        if (StringUtils.isEmpty(blacklistApprStatusParam.getApproverName())) {
             blacklistApprStatusParam.setApproverName("");
        }
        mcdHmh5BlacklistTaskService.update(Wrappers.<McdHmh5BlacklistTask>update().lambda()
                .set(McdHmh5BlacklistTask::getTaskStatus, blacklistApprStatusParam.getApprovalStatus())
                .set(McdHmh5BlacklistTask::getApproverName, blacklistApprStatusParam.getApproverName())
                .eq(McdHmh5BlacklistTask::getTaskId, blacklistApprStatusParam.getTaskId()));
        return ActionResponse.getSuccessResp();
    }

    @ApiIgnore
    @GetMapping("/selectBlacklistApprFlowId")
    @ApiOperation(value = "客户通黑名单审批流程ID查询")
    public ActionResponse<String> selectBlacklistApprFlowId(@NotNull(message = "任务编码不可为空！") String taskId) {
        UserSimpleInfo user = UserUtilJx.getUser();
        if (user.getUserId() == null) {
            log.error("没有获取到登录用户，正在创建虚拟用户！--------------------------------------------------------------");
            user.setUserId("admin01");
            user.setCityId("999");
        }
        log.info("用户：{}正在查询客户通黑名单审批流程ID，taskId值为：{}！", user.getUserName(), taskId);
        McdHmh5BlacklistTask mcdHmh5BlacklistTask = mcdHmh5BlacklistTaskService.getOne(Wrappers.<McdHmh5BlacklistTask>query().lambda().eq(McdHmh5BlacklistTask::getTaskId, taskId));
        if (ObjectUtil.isEmpty(mcdHmh5BlacklistTask)) throw new BaseException("无效的taskId值！");
        String approveFlowId = mcdHmh5BlacklistTask.getInstanceId();
        ActionResponse successResp = ActionResponse.getSuccessResp();
        successResp.setData(approveFlowId);
        return successResp;
    }

    @ApiIgnore
    @GetMapping("/saveImportList2DB")
    @ApiOperation(value = "保存任务导入的清单文件号码", notes = "根据任务id将清单文件导入保存到清单表")
    public ActionResponse<String> saveImportList2DB(@NotNull(message = "任务编码不可为空！") String taskId) {
        try {
            UserSimpleInfo user = UserUtilJx.getUser();
            if (user.getUserId() == null) {
                log.error("没有获取到登录用户，正在创建虚拟用户！--------------------------------------------------------------");
                user.setUserId("admin01");
                user.setCityId("999");
            }
            log.info("用户：{}正在保存任务导入的清单文件号码，taskId值为：{}！", user.getUserName(), taskId);
            mcdHmh5BlacklistTaskService.importBlacklistByTaskId(taskId);
            return ActionResponse.getSuccessResp();
        } catch (Exception e) {
           log.error("根据任务id将清单文件导入保存到清单表异常：", e);
            return ActionResponse.getFaildResp("根据任务id将清单文件导入保存到清单表失败");
        }
    }

}

