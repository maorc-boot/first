package com.asiainfo.biapp.pec.approve.jx.controller;


import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.approve.jx.Enum.ConstApprove;
import com.asiainfo.biapp.pec.approve.jx.model.McdBatchApproveRecord;
import com.asiainfo.biapp.pec.approve.jx.po.McdBatchApproveTaskPO;
import com.asiainfo.biapp.pec.approve.jx.service.McdBatchApproveTaskService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 批量审批任务 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2023-11-07
 */
@RestController
@RequestMapping("/batchApprove")
@Api(value = "江西:批量审批", tags = {"江西:批量审批"})
@Slf4j
public class McdBatchApproveTaskController {

    @Resource
    private McdBatchApproveTaskService mcdBatchApproveTaskService;

    @Resource
    private HttpServletRequest request;

    @PostMapping(path = "/submit")
    @ApiOperation(value = "提交批量任务", notes = "提交批量任务")
    public ActionResponse submit(@RequestBody List<McdBatchApproveRecord> list) {
        UserSimpleInfo user = UserUtil.getUser(request);
        if (null == user || StrUtil.isEmpty(user.getUserId())) {
            user = new UserSimpleInfo();
            // 测试用
            user.setUserId("admin01");
        }
        // 防止用户短时间内频繁提交批量任务，每个用户提交批量任务后 加锁，批量任务执行完成后 释放锁
        String key = StrUtil.concat(false, ConstApprove.USER_BATCH_APPROVE_LOCK_PRIFIX, user.getUserId());
        // 锁有效期 默认5分钟
        String redisLock = RedisUtils.getRedisLock(key, 300);
        if (StrUtil.isEmpty(redisLock)) {
            return ActionResponse.getFaildResp("当前用户有批量任务正在执行中，请稍后再提交");
        }
        boolean result = mcdBatchApproveTaskService.submit(list, user);
        return ActionResponse.getSuccessResp(result);

    }
}

