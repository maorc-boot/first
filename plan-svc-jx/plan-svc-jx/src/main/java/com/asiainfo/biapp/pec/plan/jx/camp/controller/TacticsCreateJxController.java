package com.asiainfo.biapp.pec.plan.jx.camp.controller;


import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampChannelListJx;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampExcuteReq;
import com.asiainfo.biapp.pec.plan.jx.camp.req.HisCampInfoReq;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampChannelListJxService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampsegServiceJx;
import com.asiainfo.biapp.pec.plan.jx.camp.utils.FileUploadUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.HisCampInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCampExcuteInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * @author : ranpf
 * @date : 2022-10-08 14:44:49
 * 营销策划，策略创建
 */
@Api(tags = "江西:策略创建")
@RequestMapping("/api/action/tactics/tacticsCreate/jx")
@RestController
@Slf4j
public class TacticsCreateJxController {

    @Autowired
    private HttpServletRequest request;


    @Autowired
    private IMcdCampsegServiceJx campsegService;

    @Autowired
    private FileUploadUtil fileUpload;

    @Autowired
    private IMcdCampChannelListJxService mcdCampChannelListJxService;


    /**
     * 文件扩展名
     *
     * @return {@link List}<{@link String}>
     */
    protected static List<String> permitFileExtension() {
        return Arrays.asList("txt", "doc", "docx", "jpg", "png", "gif", "xls", "xlsx", "jpeg", "zip", "tag", "mp3", "mp4", "avi", "rar", "tar");
    }


    /**
     * 保存策略信息
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "江西:保存策略信息", notes = "江西:保存策略信息")
    @PostMapping("save")
    public ActionResponse<String> save(@RequestBody @Valid TacticsInfoJx req) {
        String campsegId = req.getBaseCampInfo().getCampsegId();
        String campsegName = req.getBaseCampInfo().getCampsegName();
        log.info("saveTactics campsegId={},campsegName={}", campsegId, campsegName);
        boolean checkTactics = campsegService.checkTactics(campsegName, campsegId);
        Assert.isTrue(checkTactics, "策略名重复，保存策略失败");
        campsegService.checkCampEndDate(req);
        return ActionResponse.getSuccessResp((Object) campsegService.saveCamp(req, UserUtil.getUser(request)));
    }

    @PostMapping("/fileUpload")
    @ApiOperation(value = "文件上传", notes = "江西:文件上传")
    public ActionResponse fileUpload(@RequestBody MultipartFile file) {
        if (getComparison(file)) return ActionResponse.getFaildResp("上传的文件类型不符合要求");
        return fileUpload.uploadFile(file);
    }

    /**
     * 匹配能上传的文件类型
     *
     * @param file 文件
     * @return boolean
     */
    private boolean getComparison(MultipartFile file) {
        final String type = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        return permitFileExtension().stream().noneMatch(t -> t.equals(type));
    }


    /**
     * 根据客户群ID和渠道ID查询历史活动
     *
     * @param req 请求入参数
     * @return
     */
    @PostMapping("/queryHisCamp")
    @ApiOperation(value = "江西:根据客户群ID和渠道ID查询历史活动", notes = "江西:据客户群ID和渠道ID查询历史活动,渠道ID如果有多个用英文逗号分隔")
    public ActionResponse<IPage<List<HisCampInfo>>> queryHisCamp(@RequestBody HisCampInfoReq req) {
        log.info("根据客户群ID和渠道ID查询历史活动,custgroupId={},channelId={}", req.getCustgroupId(), req.getChannelIds());
        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            resp.setData(campsegService.queryHisCamp(req));
        } catch (Exception e) {
            log.error("根据客户群ID和渠道ID查询历史活动失败,custgroupId={},channelId={}", req.getCustgroupId(), req.getChannelIds());
            resp = ActionResponse.getFaildResp();
            resp.setMessage("根据客户群ID和渠道ID查询历史活动失败");
        }
        return resp;
    }

    /**
     * 活动审批最后一个节点，统计当前活动所选短信渠道，正在执行中的活动、涉及客群大小信息
     * 短信渠道：	10086短信、视频彩信、内容短信、5G消息、超级SIM卡渠道   （目前涉及渠道）
     *
     * @param req 请求入参数
     * @return
     */
    @PostMapping("/getCampsegStatistics")
    @ApiOperation(value = "江西:根据活动ID查询当前活动所选的短信渠道信息", notes = "江西:统计当前活动所选短信渠道，正在执行中的活动、涉及客群大小信息")
    public ActionResponse<IPage<List<McdCampExcuteInfo>>> getCampsegStatistics(@RequestBody CampExcuteReq req) {
        log.info("getCampsegStatistics,campsegRootId={}", req.getCampsegRootId());
        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            resp.setData(campsegService.getCampExcuteInfos(req));
        } catch (Exception e) {
            log.error("活动执行信息失败,campsegRootId={}", req.getCampsegRootId());
            resp = ActionResponse.getFaildResp();
            resp.setMessage("活动执行信息失败");
        }
        return resp;
    }


    @PostMapping("/queryIopTemplate")
    @ApiOperation(value = "江西:查询集团及IOP优秀案例模板", notes = "江西:查询集团及IOP优秀案例模板")
    public ActionResponse queryIopTemplate() {

        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            resp.setData(campsegService.queryIopTemplate(UserUtilJx.getUserId()));
        } catch (Exception e) {
            log.error("查询集团及IOP优秀模板", e);
            resp = ActionResponse.getFaildResp();
            resp.setMessage("查询集团及IOP优秀模板失败");
        }
        return resp;
    }

    @PostMapping("/getPlanIdByCampsegPId")
    @ApiOperation(value = "根据活动父id获取产品信息", notes = "根据活动父id获取campClass=1的产品信息")
    public ActionResponse getPlanIdByCampsegPId(@RequestParam("campsegPid") String campsegPid) {
        log.info("getPlanIdByCampsegPId-->根据活动父id获取产品信息入参={}", campsegPid);
        McdCampChannelListJx one = mcdCampChannelListJxService.getOne(Wrappers.<McdCampChannelListJx>lambdaQuery()
                .eq(McdCampChannelListJx::getCampsegPid, campsegPid)
                .eq(McdCampChannelListJx::getCampClass, 1));
        log.info("getPlanIdByCampsegPId-->根据活动父id获取产品信息返回={}", JSONUtil.toJsonStr(one));
        return ActionResponse.getSuccessResp(one);
    }

    @PostMapping("/queryCampScene")
    @ApiOperation(value = "江西:查询活动场景", notes = "江西:查询活动场景")
    public ActionResponse queryCampScene() {
        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            resp.setData(campsegService.queryCampScene());
        } catch (Exception e) {
            log.error("查询活动场景异常：", e);
            resp = ActionResponse.getFaildResp();
            resp.setMessage("查询活动场景失败");
        }
        return resp;
    }

}
