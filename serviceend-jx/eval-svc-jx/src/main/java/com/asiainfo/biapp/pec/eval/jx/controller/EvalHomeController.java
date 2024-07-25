package com.asiainfo.biapp.pec.eval.jx.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.eval.jx.req.CampStatReq;
import com.asiainfo.biapp.pec.eval.jx.req.EvalHomeChnExecReq;
import com.asiainfo.biapp.pec.eval.jx.req.EvalHomeReq;
import com.asiainfo.biapp.pec.eval.jx.service.EvalHomeService;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeCampStatVO;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeChnExecVO;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 首页评估数据controller
 *
 * @author mamp
 * @date 2023/5/6
 */
@RestController
@RequestMapping("/eval/home")
@Slf4j
@Api(value = "江西:首页评估图表数据", tags = "江西:首页评估图表数据")
public class EvalHomeController {


    @Autowired
    private HttpServletRequest request;
    @Resource
    private EvalHomeService evalHomeService;

    @ApiOperation(value = "查询总营销数", notes = "查询总营销数")
    @PostMapping("/total")
    public ActionResponse<List<EvalHomeDataVo>> queryTotal(@RequestBody EvalHomeReq req) {

        req.setCityId(getCurrentLoginCityId());
        log.info("查询总营销数,param:{}", req);

        try {
            return ActionResponse.getSuccessResp(evalHomeService.queryTotal(req));
        } catch (Exception e) {
            log.error("查询总营销数失败:", e);
            return ActionResponse.getFaildResp(e.getMessage());
        }
    }

    @ApiOperation(value = "查询总营销成功数", notes = "查询总营销成功数")
    @PostMapping("/sucess")
    public ActionResponse<List<EvalHomeDataVo>> querySuccess(@RequestBody EvalHomeReq req) {

        req.setCityId(getCurrentLoginCityId());
        log.info("查询总营销成功数,param:{}", req);

        try {
            return ActionResponse.getSuccessResp(evalHomeService.querySuccess(req));
        } catch (Exception e) {
            log.error("查询总营销成功数失败:", e);
            return ActionResponse.getFaildResp("查询总营销成功数失败");
        }
    }

    @ApiOperation(value = "查询营销转化率", notes = "查询营销转化率")
    @PostMapping("/successRate")
    public ActionResponse<List<EvalHomeDataVo>> querySuccessRate(@RequestBody EvalHomeReq req) {

        req.setCityId(getCurrentLoginCityId());
        log.info("查询营销转化率,param:{}", req);

        try {
            return ActionResponse.getSuccessResp(evalHomeService.querySuccessRate(req));
        } catch (Exception e) {
            log.error("查询营销转化率失败:", e);
            return ActionResponse.getFaildResp("查询营销转化率失败");
        }
    }

    private String getCurrentLoginCityId(){
        String userCity = UserUtil.getUserCity(request);
        userCity = StrUtil.equals(userCity,"999")?"":userCity;
        return userCity;
    }

    @ApiOperation(value = "渠道执行情况", notes = "渠道执行情况")
    @PostMapping("/chnExec")
    public ActionResponse<List<EvalHomeDataVo>> queryChnExceSuccessRate(@RequestBody EvalHomeChnExecReq req) {

        log.info("查询渠道执行情况,param:{}", req);
        try {
            return ActionResponse.getSuccessResp(evalHomeService.queryChnExec(req));
        } catch (Exception e) {
            log.error("查询渠道执行情况失败:", e);
            return ActionResponse.getFaildResp("查询渠道执行情况失败");
        }
    }

    @ApiOperation(value = "渠道执行情况-近6个月成功数和成功率趋势", notes = "渠道执行情况-近6个月成功数和成功率趋势")
    @PostMapping("/chnExecSuccess")
    public ActionResponse<List<EvalHomeChnExecVO>> queryChnExceSuccess(@RequestBody EvalHomeChnExecReq req) {
        log.info("渠道执行情况-近6个月成功数和成功率趋势,param:{}", req);
        try {
            return ActionResponse.getSuccessResp(evalHomeService.queryChnExecSuccess(req));
        } catch (Exception e) {
            log.error("查询渠道执行情况-近6个月成功数和成功率趋势失败:", e);
            return ActionResponse.getFaildResp("查询渠道执行情况-近6个月成功数和成功率趋势失败");
        }
    }

    @ApiOperation(value = "策略统计", notes = "策略统计")
    @PostMapping("/campStat")
    public ActionResponse<List<EvalHomeCampStatVO>> queryCampstat(@RequestBody CampStatReq req) {
        log.info("渠道执行情况-近6个月成功数和成功率趋势,param:{}", req);
        try {
            return ActionResponse.getSuccessResp(evalHomeService.queryCampStatistics(req));
        } catch (Exception e) {
            log.error("查询渠道执行情况-近6个月成功数和成功率趋势失败:", e);
            return ActionResponse.getFaildResp("查询渠道执行情况-近6个月成功数和成功率趋势失败");
        }
    }

    @ApiOperation(value = "查询策略列表", notes = "查询策略列表")
    @PostMapping("/queryCampList")
    public ActionResponse queryCampList(@RequestParam(value = "keyword", required = false) String keyword) {
        log.info("查询策略列表,keyword:{}", keyword);
        try {
            return ActionResponse.getSuccessResp(evalHomeService.queryCampList(keyword));
        } catch (Exception e) {
            log.error("查询查询策略列表失败:", e);
            return ActionResponse.getFaildResp("查询查询策略列表失败");
        }
    }

}
