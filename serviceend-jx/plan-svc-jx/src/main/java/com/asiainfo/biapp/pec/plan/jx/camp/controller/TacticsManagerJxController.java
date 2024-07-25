package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CampManageJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.*;
import com.asiainfo.biapp.pec.plan.jx.camp.service.TacticsManagerJxService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.ApprRecordJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCampCmpApproveProcessRecordJx;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdPrismCampThemeVO;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.IMcdPrismCampThemeService;
import com.asiainfo.biapp.pec.plan.service.IMcdCampsegService;
import com.asiainfo.biapp.pec.plan.vo.CampManage;
import com.asiainfo.biapp.pec.plan.vo.req.CampsegDelayQuery;
import com.asiainfo.biapp.pec.plan.vo.req.CampsegRootIdQuery;
import com.asiainfo.biapp.pec.plan.vo.req.SearchTacticsActionQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author mamp
 * @date 2022/11/9
 */
@Api(tags = "江西:策略管理")
@RequestMapping("/api/action/tactics/tacticsManager/jx")
@RestController
@Slf4j
public class TacticsManagerJxController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TacticsManagerJxService tacticsManagerJxService;

    @Autowired
    private IMcdCampsegService campsegService;
    
    @Autowired
    private IMcdPrismCampThemeService campThemeService;


    /**
     * 策略管理界面查询活动列表
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "江西查询活动列表", notes = "策略管理查询活动列表")
    @PostMapping(value = "/searchIMcdCamp")
    public IPage<CampManage> searchIMcdCamp(@RequestBody SearchTacticsActionQuery form) {
        log.info("开始查询活动列表,参数form={}", form);
        final UserSimpleInfo user = UserUtil.getUser(request);
        form.setUserCity(user.getCityId());
        form.setUserId(user.getUserId());
        if (StrUtil.isNotEmpty(form.getCampEndDay())) {
            form.setCampEndDay(form.getCampEndDay() + " 23:59:59");
        }

        return tacticsManagerJxService.searchIMcdCamp(form);
    }

    /**
     * 策略管理界面查询活动列表
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "江西查询活动列表(个性化)", notes = "江西查询活动列表(个性化)")
    @PostMapping(value = "/searchIMcdCampJx")
    public IPage<CampManage> searchIMcdCampJx(@RequestBody SearchTacticsActionQueryJx form) {
        log.info("开始查询活动列表,参数form={}", form);
        final UserSimpleInfo user = UserUtil.getUser(request);
        // String userCity = user.getCityId();
        // userCity = StrUtil.equals(userCity,"999")?"":userCity;
        // form.setUserCity(userCity);
        // 不进行地市过滤
        form.setUserCity("");
        form.setUserId(user.getUserId());
        if (StrUtil.isNotEmpty(form.getCampEndDay())) {
            form.setCampEndDay(form.getCampEndDay() + " 23:59:59");
        }
        return tacticsManagerJxService.searchIMcdCampJx(form);
    }

    /**
     * 策略管理界面查询活动列表
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "江西查询活动展开子策略接口", notes = "江西查询活动展开子策略接口")
    @PostMapping(value = "/queryMcdSubCampList")
    public List<CampManage> queryMcdSubCampList(@RequestBody McdSubCampByIdQuery req) {
        log.info("江西查询活动展开子策略接口,参数form={}", req);
        if (StringUtils.isEmpty(req.getCampsegRootId())) {
            log.info("江西查询活动展开子策略接口入参为空了");
            return null;
        }
        if (StringUtils.isEmpty(req.getUserId())) {
            final UserSimpleInfo user = UserUtil.getUser(request);
            req.setUserId(user.getUserId());
        }

        return tacticsManagerJxService.queryMcdSubCampList(req);
    }

    /**
     * 策略管理导出(我的策略和全部策略)
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "策略管理导出(我的策略和全部策略)", notes = "策略管理导出(我的策略和全部策略)")
    @PostMapping(value = "/exportCampJx")
    public void exportCampJx(@RequestBody SearchTacticsActionQueryJx form, HttpServletResponse response) {
        log.info("策略管理导出,参数form={}", form);
        final UserSimpleInfo user = UserUtil.getUser(request);
        form.setUserId(user.getUserId());
        // 不进行地市过滤
        form.setUserCity("");
        if (StrUtil.isNotEmpty(form.getCampEndDay())) {
            form.setCampEndDay(form.getCampEndDay() + " 23:59:59");
        }
        tacticsManagerJxService.exportCampJx(form, response);
    }

    /**
     * 策略管理界面查询活动列表
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "江西策略管理我参与的策略查询接口", notes = "江西策略管理我参与的策略查询接口")
    @PostMapping(value = "/searchIMcdMyParticipationCamp")
    public IPage<CampManageJx> searchIMcdMyParticipationCamp(@RequestBody SearchMyParticipationTacticsQuery form) {
        log.info("策略管理我参与的策略查询查询活动列表,参数form={}", form);
        final UserSimpleInfo user = UserUtil.getUser(request);
        form.setLoginUserId(user.getUserId());
        if (StrUtil.isNotEmpty(form.getCampEndDay())) {
            form.setCampEndDay(form.getCampEndDay() + " 23:59:59");
        }

        return tacticsManagerJxService.searchIMcdMyParticipationCamp(form);
    }


    /**
     * 策略管理界面查询活动列表
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "江西策略管理我参与的策略导出", notes = "江西策略管理我参与的策略导出")
    @PostMapping(value = "/exportIMcdMyParticipationCamp")
    public void exportIMcdMyParticipationCamp(@RequestBody SearchMyParticipationTacticsQuery form, HttpServletResponse response) {
        log.info("策略管理我参与的策略导出活动列表,参数form={}", form);
        final UserSimpleInfo user = UserUtil.getUser(request);
        form.setLoginUserId(user.getUserId());
        if (StrUtil.isNotEmpty(form.getCampEndDay())) {
            form.setCampEndDay(form.getCampEndDay() + " 23:59:59");
        }

        tacticsManagerJxService.exportIMcdMyParticipationCamp(form, response);
    }


    /**
     * 营销审批列表
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "江西营销审批列表", notes = "营销审批列表")
    @PostMapping("/approveRecord")
    public IPage<ApprRecordJx> approveRecord(@RequestBody McdCampApproveJxQuery req) {
        return tacticsManagerJxService.approveRecord(req);
    }

    /**
     * 营销待审批列表(包含沟通人)
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "江西营销审批列表(包含沟通人)", notes = "营销审批列表(包含沟通人)")
    @PostMapping("/approveRecordNew")
    public ActionResponse<IPage<McdCampCmpApproveProcessRecordJx>> approveRecordNew(@RequestBody McdCampApproveJxNewQuery req) {
        if (Objects.isNull(req.getUserId())) {
            UserSimpleInfo user = UserUtil.getUser(request);
            req.setUserId(user.getUserId());
        }
        return ActionResponse.getSuccessResp(tacticsManagerJxService.approveRecordNew(req));
    }


    /**
     * 主活动
     *
     * @param campsegRootId
     * @return
     */
    @ApiOperation(value = "策略复制", notes = "策略复制")
    @PostMapping("copyCampseg")
    public ActionResponse<String> copyCampsegJx(@RequestBody @Valid CampsegRootIdQuery campsegRootId) {
        return ActionResponse.getSuccessResp((Object) tacticsManagerJxService.copyCamp(campsegRootId.getCampsegRootId(), null, UserUtil.getUser(request), "1"));
    }

    /**
     * 删除策略信息，主活动，子活动
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "删除策略信息", notes = "策略管理界面")
    @PostMapping("/delCampseg")
    public ActionResponse delCampseg(@RequestBody @Valid McdIdQuery req) {
        log.info("开始删除活动,参数id={}", req.getId());
        tacticsManagerJxService.deleteCampsegInfo(req.getId());
        return ActionResponse.getSuccessResp();
    }
       /**
     * 一键审批
     *
     * @param campsegRootId
     * @return
     */
    @ApiOperation(value = "一键审批", notes = "一键审批")
    @GetMapping("/quickApprove")
    public ActionResponse quickApprove(@RequestParam("campsegRootId") String campsegRootId) {
        log.info("一键审批,campsegRootId={}",campsegRootId);
        tacticsManagerJxService.quickApprove(campsegRootId);
        return ActionResponse.getSuccessResp();
    }

    /**
     * 主活动，子活动
     * @return
     * @title 延期策略信息
     */
    @ApiOperation(value = "延期策略信息", notes = "策略管理界面")
    @PostMapping("/udateCampsegEndDate")
    public ActionResponse updateCampsegEndDate(@RequestBody @Valid CampsegDelayQuery req) {
        campsegService.checkCampEndDate(req.getId(), req.getEndDate(), false);
        log.info("开始活动延期,参数{}", req);
        tacticsManagerJxService.updateCampsegEndDate(req.getId(), req.getEndDate());
        return ActionResponse.getSuccessResp();
    }

    @ApiOperation(value = "条件查询策略主题", notes = "条件查询策略主题")
    @PostMapping("/searchThemeCampList")
    public IPage<McdPrismCampThemeVO> searchThemeCampList(@RequestBody CampThemeQuery req){
        log.info("条件查询策略主题,入参req = {}", req);
        final UserSimpleInfo user = UserUtil.getUser(request);
        req.setUserId(user.getUserId());
        return campThemeService.searchThemeCampList(req);
    }
    
    /**
     * 主题策略导出(我的策略和全部策略)
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "主题策略导出(我的策略和全部策略)", notes = "主题策略导出(我的策略和全部策略)")
    @PostMapping(value = "/exportThemeCamp")
    public void exportThemeCamp(@RequestBody CampThemeQuery form, HttpServletResponse response) {
        log.info("策略管理导出,参数form={}", form);
        final UserSimpleInfo user = UserUtil.getUser(request);
        form.setUserId(user.getUserId());
        tacticsManagerJxService.exportThemeCamp(form, response);
    }

}
