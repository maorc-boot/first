package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller;


import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.AddNewPictureReplyMcdFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.AddNewReplyMcdFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.controller.reqParam.SelectManagerFeedbackParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.McdFeedbackQaService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdFeedbackQaResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.service.impl.resultInfo.McdManagerFeedbackRecentlyReply;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
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
@RequestMapping("/mcdFeedbackQa")
@Slf4j
@Validated
@Api(tags = "客户通-问题反馈")
@DataSource("khtmanageusedb")
public class McdFeedbackQaController {

    @Autowired
    private McdFeedbackQaService feedbackQaService;

    @ApiOperation("根据条件查询问题反馈的最新回复")
    @GetMapping("/getRecentlyReplyFeedback")
    public ActionResponse<Page<McdManagerFeedbackRecentlyReply>> getRecentlyReplyFeedback(SelectManagerFeedbackParam feedbackVO) {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("用户id：{}在查询所有反馈问题最近的回复", userId);
        Page<McdManagerFeedbackRecentlyReply> recentlyReplies = feedbackQaService.getRecentlyReplyFeedback(feedbackVO);
        return ActionResponse.getSuccessResp(recentlyReplies);
    }


    @ApiOperation("根据问题反馈的businessId查询所有回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "查询的当前页", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "查询的页面大小", required = true, example = "10"),
            @ApiImplicitParam(name = "businessId", value = "问题反馈的businessId", required = true, example = "123456788"),
    })
    @GetMapping("/getAllRepliesByBusinessId")
    public ActionResponse<Page> getAllRepliesByBusinessId(
            @RequestParam("pageNum") @Min(value = 1,message = "页码至少为1！") Integer pageNum,
            @RequestParam("pageSize") @Min(value = 1,message = "查询页大小至少为1！") Integer pageSize,
            @RequestParam("businessId") @NotBlank(message = "businessId不能为空！") String businessId
    ) {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("用户id：{}在查询业务编号为{}的全部回复", userId, businessId);
        Page<McdFeedbackQaResultInfo> allReplies = feedbackQaService.getAllRepliesByBusinessId(pageNum, pageSize, businessId);
        return ActionResponse.getSuccessResp(allReplies);
    }

    @ApiOperation("新增回复")
    @PostMapping("/addNewReply")
    public ActionResponse addNewReply(@RequestBody @Validated AddNewReplyMcdFeedbackParam newReplyMcdFeedbackParam) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户id：{}正在回复业务编号为{}的问题反馈，回复内容为：{}",
                user.getUserId(), newReplyMcdFeedbackParam.getBusinessId(),
                newReplyMcdFeedbackParam.getQaContent());
        feedbackQaService.addNewReply(user, newReplyMcdFeedbackParam);
        return ActionResponse.getSuccessResp("回复成功！");
    }

    @ApiOperation("新增图片回复")
    @PostMapping("/addNewReplyPicture")
    public ActionResponse addNewReplyPicture(@Validated AddNewPictureReplyMcdFeedbackParam newReplyMcdFeedbackParam) throws Exception {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("用户id：{}正在回复业务编号为{}的问题反馈，回复图片文件名为：{}",
                user.getUserId(), newReplyMcdFeedbackParam.getBusinessId(),
                newReplyMcdFeedbackParam.getPictureFile().getOriginalFilename());
        feedbackQaService.addNewReplyPicture(user, newReplyMcdFeedbackParam);
        return ActionResponse.getSuccessResp("回复成功！");
    }

    @ApiOperation("根据图片名下载图片")
    @ApiImplicitParam(name = "pictureName", value = "图片名称，从回复中获取", required = true)
    @GetMapping("/downloadImage/{pictureName}")
    public void downloadImage(@PathVariable String pictureName, HttpServletResponse response) throws Exception {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("用户id：{}正在下载图片：{}!", userId, pictureName);
        feedbackQaService.downloadImage(pictureName, response);
    }
}

