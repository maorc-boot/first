package com.asiainfo.biapp.pec.plan.jx.coordination.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.enums.CampCoordinateStatus;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;

import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCampCoordinationTaskModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.CampCoordinationApproveQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.CampCoordinationStatusQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.CoordinationSubmitProcessQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.response.ApprTaskRecord;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.IMcdCampCoordinationApproveService;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.McdCampCoordinationTaskService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Description : 策略统筹审批服务前端控制器
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-6-13
 */
@RestController
@RequestMapping("/api/campCoordinate/taskApprove")
@Api(value = "江西:策略统筹审批管理服务", tags = {"江西:策略统筹审批管理服务"})
@Slf4j
public class McdCampCoordinationApproveController {
    
    @Resource
    private McdCampCoordinationTaskService mcdCampCoordinationTaskService;
    
    @Resource
    private IMcdCampCoordinationApproveService mcdCampCoordinationApproveService;
    
    @Autowired
    private HttpServletRequest request;


    /**
     * 统筹任务审批列表
     *
     * @param req 关键字搜索
     * @return 统筹任务审批列表
     */
    @ApiOperation(value = "江西:统筹主任务待审批列表查询", notes = "统筹主任务待审批列表查询")
    @PostMapping("/approveTaskRecord")
    public IPage<ApprTaskRecord> approveRecord(@RequestBody CampCoordinationApproveQuery req) {
        IPage<ApprTaskRecord> apprTaskRecordIPage = mcdCampCoordinationApproveService.approveTaskRecord(req);
        // 处理是否有审批中子任务、客群总数逻辑
        delHasChildTask(apprTaskRecordIPage);
        return apprTaskRecordIPage;
    }

    /**
     * 处理是否有审批中子任务、客群总数逻辑
     *
     * @param apprTaskRecordIPage apprTaskRecordIPage
     */
    private void delHasChildTask(IPage<ApprTaskRecord> apprTaskRecordIPage) {
        // 主任务id集合
        List<String> taskIds = apprTaskRecordIPage.getRecords().stream().map(ApprTaskRecord::getTaskId).collect(Collectors.toList());
        // 获取主任务下的子任务信息
        List<ApprTaskRecord> apprTaskRecords = mcdCampCoordinationApproveService.approveChildTaskRecord(taskIds, true, CampCoordinateStatus.APPROVING.getCode());
        // 按taskPId分组  获取子任务信息
        Map<String, List<ApprTaskRecord>> collect = apprTaskRecords.stream().collect(Collectors.groupingBy(ApprTaskRecord::getTaskPId, Collectors.toList()));
        apprTaskRecordIPage.getRecords().forEach(record -> {
            // 子任务信息
            List<ApprTaskRecord> childTasks = collect.get(record.getTaskId());
            if (CollectionUtil.isNotEmpty(childTasks)) {
                record.setHasChildTasks(true); // 该主任务下有审批中子任务信息
                // 待审批子任务客群数求和
                int sum = childTasks.stream().mapToInt(ApprTaskRecord::getCustomTotalNum).sum();
                record.setCustomTotalNum(sum);
            }
        });
    }

    @ApiOperation(value = "江西:统筹子任务待审批列表查询", notes = "根据主任务id查询：统筹子任务待审批列表查询")
    @PostMapping("/approveChildTaskRecord")
    public ActionResponse approveChildTaskRecord(@RequestBody McdIdQuery query) {
        log.info("统筹子任务待审批列表查询入参={}", JSONUtil.toJsonStr(query));
        List<ApprTaskRecord> apprTaskRecords = mcdCampCoordinationApproveService.approveChildTaskRecord(Collections.singletonList(query.getId()), true, CampCoordinateStatus.APPROVING.getCode());
        log.info("统筹子任务待审批列表查询返回={}", JSONUtil.toJsonStr(apprTaskRecords));
        return ActionResponse.getSuccessResp(apprTaskRecords);
    }

    /**
     * 统筹任务提交审批
     */
    @ApiOperation(value = "江西:统筹任务提交审批", notes = "统筹一个或多个子任务提交审批")
    @PostMapping("/subTaskApprove")
    public ActionResponse subTaskApprove(@RequestBody @Valid CoordinationSubmitProcessQuery req) {
        log.info("统筹任务提交审批-->subTaskApprove入参={}", JSONUtil.toJsonStr(req));
         UserSimpleInfo user = UserUtil.getUser(request);
         if (Objects.isNull(user)){
             user.setUserName("管理员1");
             user.setUserId("admin01");
             user.setCityId("999");
         }

        mcdCampCoordinationApproveService.submitTaskAppr(req, user);
        return ActionResponse.getSuccessResp();
    }

    /**
     * 审批流修改状态
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "策略统筹审批流转修改状态")
    @PostMapping("/updateTaskStatus")
    public ActionResponse updateTaskStatus(@RequestBody CampCoordinationStatusQuery req) {
        log.info("策略统筹审批流转修改状态入参={}", JSONUtil.toJsonStr(req));
        boolean flag = false;
        // 更该主任务状态前判断是否还有待审批或审批中以及审批驳回状态子任务   有--不需要改主任务状态
        List<McdCampCoordinationTaskModel> list = chkIsChangeMainTask(req);
        if (CollectionUtil.isEmpty(list)) {
            // 主任务
            flag = updateMainTask(req);
        }
        // 子任务
        ActionResponse actionResponse = updateChildTask(req);
        if (flag || ResponseStatus.SUCCESS.equals(actionResponse.getStatus())) {
            return ActionResponse.getSuccessResp();
        } else {
            return ActionResponse.getFaildResp();
        }
    }

    @ApiOperation(value = "策略统筹子任务修改状态")
    @PostMapping("/updateChildTask")
    public ActionResponse updateChildTask(@RequestBody CampCoordinationStatusQuery req) {
        log.info("updateChildTask-->入参={}", JSONUtil.toJsonStr(req));
        LambdaUpdateWrapper<McdCampCoordinationTaskModel> childUpdateWrapper = new LambdaUpdateWrapper<>();
        childUpdateWrapper.set(McdCampCoordinationTaskModel::getExecStatus, req.getExecStatus())
                .eq(McdCampCoordinationTaskModel::getTaskPId, req.getTaskId())
                .in(McdCampCoordinationTaskModel::getTaskId, Arrays.asList(req.getChildTaskIds().split(StrUtil.COMMA)));
        boolean update = mcdCampCoordinationTaskService.update(childUpdateWrapper);
        if (update) {
            return ActionResponse.getSuccessResp();
        } else {
            return ActionResponse.getFaildResp();
        }
    }

    @ApiOperation(value = "策略统筹主任务修改状态")
    @PostMapping("/updateMainTaskStat")
    public ActionResponse updateMainTaskStat(@RequestBody CampCoordinationStatusQuery req) {
        log.info("updateMainTaskStat-->入参={}", JSONUtil.toJsonStr(req));
        boolean update = false;
        // 1.查询子任务的个数
        int childCount = mcdCampCoordinationTaskService.count(Wrappers.<McdCampCoordinationTaskModel>lambdaQuery()
                .eq(McdCampCoordinationTaskModel::getTaskPId, req.getTaskId()));
        // 2.查询子任务为审批完成--54的个数
        int completeCount = mcdCampCoordinationTaskService.count(Wrappers.<McdCampCoordinationTaskModel>lambdaQuery()
                .eq(McdCampCoordinationTaskModel::getTaskPId, req.getTaskId())
                .eq(McdCampCoordinationTaskModel::getExecStatus, CampCoordinateStatus.APPROVAL_COMPLETED.getId()));
        log.info("updateMainTaskStat-->childCount={}, completeCount={}", childCount, completeCount);
        // 个数相同  则更改主任务状态为审批完成--54
        if (childCount == completeCount) {
            log.info("updateMainTaskStat-->子任务全部审批通过，更改主任务审批通过");
            update = updateMainTask(req);
        }
        if (update) {
            return ActionResponse.getSuccessResp();
        } else {
            return ActionResponse.getFaildResp();
        }
    }

    /**
     * 更新主任务状态
     *
     * @param req 要求事情
     * @return boolean
     */
    private boolean updateMainTask(CampCoordinationStatusQuery req) {
        LambdaUpdateWrapper<McdCampCoordinationTaskModel> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(McdCampCoordinationTaskModel::getExecStatus, req.getExecStatus())
                .eq(McdCampCoordinationTaskModel::getTaskId,req.getTaskId())
                .eq(McdCampCoordinationTaskModel::getTaskPId, Constant.SpecialNumber.ZERO_NUMBER);
        return mcdCampCoordinationTaskService.update(updateWrapper);
    }

    /**
     * 校验是否更改主任务状态
     *
     * @param req 要求事情
     * @return {@link List}<{@link McdCampCoordinationTaskModel}>
     */
    private List<McdCampCoordinationTaskModel> chkIsChangeMainTask(CampCoordinationStatusQuery req) {
        LambdaQueryWrapper<McdCampCoordinationTaskModel> taskModelLambdaQueryWrapper = Wrappers.<McdCampCoordinationTaskModel>lambdaQuery()
                .and(i -> i.eq(McdCampCoordinationTaskModel::getExecStatus, CampCoordinateStatus.APPROVING.getId()) // 审批中
                .or()
                .eq(McdCampCoordinationTaskModel::getExecStatus, CampCoordinateStatus.APPROVAL_PENDIING.getId()) // 待审批
                .or()
                .eq(McdCampCoordinationTaskModel::getExecStatus, CampCoordinateStatus.APPROVE_BACK.getId())) // 审批退回
                .eq(McdCampCoordinationTaskModel::getTaskPId, req.getTaskId())
                .notIn(McdCampCoordinationTaskModel::getTaskId, Arrays.asList(req.getChildTaskIds().split(StrUtil.COMMA)));
        List<McdCampCoordinationTaskModel> list = mcdCampCoordinationTaskService.list(taskModelLambdaQueryWrapper);
        log.info("updateTaskStatus-->是否有审批中或待审批任务={}", JSONUtil.toJsonStr(list));
        return list;
    }

    @ApiOperation(value = "判断是否还有审批中状态的子任务")
    @PostMapping("/chkApprovingTask")
    public ActionResponse chkApprovingTask(@RequestBody CampCoordinationStatusQuery query) {
        log.info("判断是否还有审批中状态的子任务入参={}", JSONUtil.toJsonStr(query));
        LambdaQueryWrapper<McdCampCoordinationTaskModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdCampCoordinationTaskModel::getTaskPId, query.getTaskId()).eq(McdCampCoordinationTaskModel::getExecStatus, CampCoordinateStatus.APPROVING.getId())
                .notIn(McdCampCoordinationTaskModel::getTaskId, Arrays.asList(query.getChildTaskIds().split(StrUtil.COMMA)));
        List<McdCampCoordinationTaskModel> list = mcdCampCoordinationTaskService.list(queryWrapper);
        log.info("判断是否还有审批中状态的子任务返回={}", JSONUtil.toJsonStr(list));
        if (CollectionUtil.isEmpty(list)) {
            // 该主任务没有审批中的子任务信息了
            return ActionResponse.getSuccessResp();
        } else {
            return ActionResponse.getFaildResp();
        }
    }

}
