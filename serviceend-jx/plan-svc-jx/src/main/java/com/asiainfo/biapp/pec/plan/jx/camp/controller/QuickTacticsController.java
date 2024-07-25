package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.iopws.client.IOPComClient;
import com.asiainfo.biapp.pec.iopws.client.impl.IOPComClientImpl;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IQuickTacticsService;
import com.asiainfo.biapp.pec.plan.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.service.IMcdCampDefService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 一级精准推荐
 *
 * @author mamp
 * @date 2023/4/6
 */
@Api(tags = "江西:一级精准推荐")
@RequestMapping("/api/action/tactics/quickTactics")
@RestController
@Slf4j
public class QuickTacticsController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IQuickTacticsService quickTacticsService;

    @Autowired
    private IMcdCampDefService mcdCampDefService;

    @ApiOperation(value = "通过集团模板一键生成一级精准推荐活动", notes = "通过集团模板一键生成一级精准推荐活动")
    @PostMapping("/quickCreateTacticsByIopTemplate")
    public Object quickCreateTacticsByIopTemplate(@RequestParam("templateId") String templateId, @RequestParam("activityId") String activityId) {
        ActionResponse response = ActionResponse.getSuccessResp();
        log.info("templateId=[{}],activityId = [{}]", templateId, activityId);
        UserSimpleInfo user = UserUtilJx.getUser(request);
        try {
            String campsegRootId = quickTacticsService.quickCreateTacticsByIopTemplate(templateId, activityId, user);
            response.setData(campsegRootId);
        } catch (Exception e) {
            log.error("quickCreateTacticsByIopTemplate error,", e);
            response = ActionResponse.getFaildResp();
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "更新一级模板活动状态", notes = "更新一级模板活动状态")
    @PostMapping("/changeActivityStatus")
    public Object changeActivityStatus(@RequestParam("activityId") String activityId, @RequestParam("activityId") String activityStatus) {
        log.info("activityStatus=[{}],activityId = [{}]", activityStatus, activityId);
        ActionResponse response = ActionResponse.getSuccessResp();
        try {
            quickTacticsService.changeActivityStatus(request);
        } catch (Exception e) {
            log.error("更改策略状态失败", e);

        }
        return response;
    }

    /**
     * 素材通知接口
     *
     * @param activityId
     * @param notifyType
     * @return
     */
    @ApiOperation(value = "素材通知接口", notes = "素材通知接口")
    @PostMapping("/materialNotify")
    public Object materialNotify(@RequestParam("activityId") String activityId, @RequestParam("notifyType") String notifyType) {
        log.info("notifyType=[{}],activityId = [{}]", notifyType, activityId);
        ActionResponse response = ActionResponse.getSuccessResp();
        return response;
    }

    /**
     * 调用IOP-90030接口
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "调用IOP-90030接口", notes = "调用IOP-90030接口")
    @PostMapping("/query90030")
    public Object query90030(HttpServletRequest request) {
        String activityId = request.getParameter("activityId");
        if(StrUtil.isEmpty(activityId)){
            activityId = String.valueOf(request.getAttribute("activityId"));
        }
        String channelSourceType = request.getParameter("channelSourceType");
        channelSourceType = StringUtils.isEmpty(channelSourceType) ? "6" : channelSourceType;
        String approveStatus = request.getParameter("approveStatus");
        approveStatus = StringUtils.isEmpty(approveStatus) ? "1" : approveStatus;
        String flow = request.getParameter("flow");
        flow = StringUtils.isEmpty(flow) ? "23" : flow;
        String description = request.getParameter("description");
        description = StringUtils.isEmpty(description) ? "通过" : description;
        IOPComClient iopComClient = new IOPComClientImpl();
        log.info("query90030 param: activityId = {},channelSourceType = {},approveStatus={},flow = {},description = {}", activityId, channelSourceType, approveStatus, flow, description);
        return iopComClient.contentFeedback
                (activityId, channelSourceType, approveStatus, flow, description);

    }

    /**
     * 一键审批
     *
     * @param campsegRootId campsegRootId
     * @throws Exception
     */

    @ApiOperation(value = "一键审批", notes = "一键审批")
    @PostMapping("/qucickApprove")
    public Object qucickApprove(@RequestParam("campsegRootId") String campsegRootId) {
        log.info("一键审批,campsegRootId={}", campsegRootId);
        LambdaUpdateWrapper<McdCampDef> wrapper = new LambdaUpdateWrapper<>();
        // 将活动状态改为审批中
        wrapper.eq(McdCampDef::getCampsegRootId, campsegRootId).or().eq(McdCampDef::getCampsegId, campsegRootId)
                .set(McdCampDef::getCampsegStatId, 40).set(McdCampDef::getApproveFlowId,campsegRootId);
        ActionResponse response = ActionResponse.getSuccessResp();
        try {
            mcdCampDefService.update(wrapper);
            // 先把状态改成审批中，再进行一键审批
            // 调用用集团90030接口
            request.setAttribute("activityId",campsegRootId);
            //一键审批 (发布到执行)
           // quickTacticsService.qucickApprove959Camp(campsegRootId);
            // 调用用集团90030接口
            query90030(request);
        } catch (Exception e) {
            log.error("一键审批失败,campsegRootId={}", campsegRootId, e);
            response = ActionResponse.getFaildResp(e.getMessage());
        }
        return response;
    }



    @ApiOperation(value = "定时更新一级精准营销活动的活动状态任务 ", notes = "定时更新一级精准营销活动的活动状态任务 ")
    @PostMapping("/iopPrecisionMarketingStatusTask")
    public ActionResponse iopPrecisionMarketingStatusTask() {
        log.info("更新一级精准营销活动的活动状态任务开始");
        try {
            quickTacticsService.updateActivityStatus();
            log.info("更新一级精准营销活动的活动状态任务完成");
            return ActionResponse.getSuccessResp();
        } catch (Exception e) {
            log.error("更新一级精准营销活动的活动状态任务异常", e);
            return ActionResponse.getFaildResp();
        }
    }

}
