package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.McdManagerFeedbackService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdManagerFeedbackResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@RestController
@RequestMapping("/mcdManagerFeedback")
@Slf4j
@Api(tags = "客户通问题反馈模块详情")
@Validated
public class McdManagerFeedbackController {

    @Autowired
    private McdManagerFeedbackService managerFeedbackService;

    @GetMapping//根据问题反馈的业务id查询详情
    @ApiOperation("根据问题反馈的businessId查询问题详情")
    @ApiImplicitParam(name = "businessId", value = "问题反馈的业务详情编号", required = true, example = "123456788")
    public ActionResponse<McdManagerFeedbackResultInfo> selectManagerFeedbackDetail(
            @NotBlank(message = "业务id不能为空！") String businessId) {
        Integer userId = 1;
        log.info("用户id：{}在查询问题反馈内容，业务反馈id为：{}", userId, businessId);
        McdManagerFeedbackResultInfo feedback = managerFeedbackService.selectManagerFeedbackDetail(businessId);
        if (feedback == null) return ActionResponse.getFaildResp("根据businessId：" + businessId + "没有查找到相应的数据！");
        return ActionResponse.getSuccessResp(feedback);
    }
}

