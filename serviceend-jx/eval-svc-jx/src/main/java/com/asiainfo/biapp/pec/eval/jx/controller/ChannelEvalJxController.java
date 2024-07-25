package com.asiainfo.biapp.pec.eval.jx.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.eval.jx.constants.SpecialNumberJx;
import com.asiainfo.biapp.pec.eval.jx.service.ChannelEvalJxService;
import com.asiainfo.biapp.pec.eval.jx.utils.ExportExcelUtil;
import com.asiainfo.biapp.pec.eval.model.req.ChannelEvaluateReqModel;
import com.asiainfo.biapp.pec.eval.util.DateTool;
import com.asiainfo.biapp.pec.eval.util.Pager;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * description: 渠道效果评估
 *
 * @author: lvchaochao
 * @date: 2023/1/11
 */
@Api(tags = {"江西：渠道效果评估"}, value = "江西：渠道效果评估")
@RequestMapping("/evaluate/channel/jx")
@RestController
@Slf4j
public class ChannelEvalJxController {

    @Autowired
    private ChannelEvalJxService channelEvalJxService;

    @Autowired
    private ExportExcelUtil exportExcelUtil;

    @ApiOperation(value = "查询渠道效果评估信息", notes = "查询渠道效果评估信息")
    @RequestMapping("/list")
    public ActionResponse list(@RequestBody ChannelEvaluateReqModel evaluateModel, HttpServletRequest request) {
        log.info("江西渠道评估查询请求入参：{}", JSONUtil.toJsonStr(evaluateModel));
        try {
            Pager pager = buildChannelEvalParam(evaluateModel, request);
            List<Map<String, Object>> list = channelEvalJxService.getChannelEffectList(evaluateModel, pager);
            pager.getTotalPage();
            pager = pager.pagerFlip();
            pager.setResult(list);
            return ActionResponse.getSuccessResp("渠道评估数据查询成功").setData(pager);
        } catch (Exception e) {
            log.error("江西渠道评估数据查询异常：", e);
        }
        return ActionResponse.getFaildResp("渠道评估数据查询失败");
    }

    @ApiOperation(value = "导出渠道效果评估信息", notes = "导出渠道效果评估信息")
    @RequestMapping("/exportChannelEval")
    public void exportChannelEval(@RequestBody ChannelEvaluateReqModel evaluateModel, HttpServletRequest request, HttpServletResponse response) {
        log.info("江西导出渠道评估请求入参：{}", JSONUtil.toJsonStr(evaluateModel));
        try {
            Pager pager = buildChannelEvalParam(evaluateModel, request);
            List<Map<String, Object>> list = channelEvalJxService.getChannelEffectList(evaluateModel, pager);
            List<Object> rs = new ArrayList<>();
            list.forEach(map -> {
                Object[] obj = new Object[SpecialNumberJx.EIGHT_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = map.get("STAT_TIME");
                obj[SpecialNumberJx.ONE_NUMBER] = map.get("CHANNEL_NAME");
                obj[SpecialNumberJx.TWO_NUMBER] = map.get("CITY_NAME");
                obj[SpecialNumberJx.THREE_NUMBER] = map.get("CAMPSEG_COUNT");
                obj[SpecialNumberJx.FOUR_NUMBER] = map.get("TARGET_USER_NUM");
                obj[SpecialNumberJx.FIVE_NUMBER] = map.get("CAMP_USER_NUM");
                obj[SpecialNumberJx.SIX_NUMBER] = map.get("CAMP_SUCCESS_USER_NUM");
                obj[SpecialNumberJx.SEVEN_NUMBER] = map.get("CAMP_SUCCESS_RADIO");
                rs.add(obj);
            });
            String[] excelHeader = {"日期", "渠道名称", "地市", "营销活动数", "目标用户规模", "营销用户数", "成功用户数", "成功办理率"};
            exportExcelUtil.exportExcel(excelHeader, rs, response, "渠道评估");
        } catch (Exception e) {
            log.error("江西渠道评估导出数据异常: ", e);
        }
    }

    /**
     * 构造渠道评估查询、导出的查询参数
     *
     * @param evaluateModel 评估模型
     * @param request       请求
     * @return {@link Pager}
     * @throws Exception 异常
     */
    private Pager buildChannelEvalParam(@RequestBody ChannelEvaluateReqModel evaluateModel, HttpServletRequest request) throws Exception{
        // UserSimpleInfo user = UserUtil.getUser(request);
        // // 省公司用户地市信息
        // String CENTER_CITYID = RedisUtils.getDicValue("CENTER_CITYID");
        // // true==>当前用户是省公司用户,地市信息以前台传入为准 false==>地市用户 地市信息以当前登录用户的地市信息为准
        // boolean isCenterUser = StringUtil.isNotEmpty(user.getCityId()) && user.getCityId().equals(CENTER_CITYID);
        // if (!isCenterUser) {
        //     Map<String, Object> cityMap = channelEvalJxService.getCityById(user.getCityId());
        //     if (!CollectionUtils.isEmpty(cityMap) && ObjectUtil.isNotEmpty(cityMap.get("CITY_ID"))) {
        //         evaluateModel.setCityId(Convert.toStr(cityMap.get("CITY_ID")));
        //     }
        // }
        if (StringUtils.isBlank(evaluateModel.getDate())) {// 没有传日期：月视图默认为上个月；日视图默认为昨天; 周视图默认为上周
            if ("day".equals(evaluateModel.getDateType())) {
                evaluateModel.setDate(DateTool.getStringDate(DateTool.getPreDate(new Date(), SpecialNumberJx.ONE_NUMBER), "yyyyMMdd"));
            } else if ("week".equals(evaluateModel.getDateType())) {
                evaluateModel.setDate(getLastWeekByNow());
            } else {
                evaluateModel.setDate(DateTool.getStringDate(DateTool.getPreMonth(new Date(), SpecialNumberJx.ONE_NUMBER), "yyyyMM"));
            }
        }
        // String date = evaluateModel.getDate();
        // date = date.replaceAll(StrUtil.DASHED, "");
        // evaluateModel.setDate(date);
        Integer pageSize = evaluateModel.getPageSize();
        Integer pageNum = evaluateModel.getPageNum();
        Pager pager = new Pager();
        pager.setPageSize(pageSize == null ? SpecialNumberJx.TEN_NUMBER : pageSize);
        pager.setPageNum(pageNum == null ? SpecialNumberJx.ONE_NUMBER : pageNum); // 当前页
        pager.setPageFlag("G");
        return pager;
    }

    /**
     * 根据当前时间获取上一个自然周的周一、周日日期
     *
     * @return {@link String}
     */
    private String getLastWeekByNow () throws Exception{
        // 当前时间
        LocalDate now = LocalDate.now().plusDays(SpecialNumberJx.ZERO_NUMBER);
        log.info("当前日期: {}", now);
        // 求这个日期上一周的周一、周日
        LocalDate todayOfLastWeek = now.minusDays(SpecialNumberJx.SEVEN_NUMBER);
        LocalDate monday = todayOfLastWeek.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(SpecialNumberJx.ONE_NUMBER);
        LocalDate sunday = todayOfLastWeek.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(SpecialNumberJx.ONE_NUMBER);
        log.info("根据当前时间获取上一个自然周的周一: {}, 周日：{}", monday, sunday);
        return StrUtil.join(",", monday, sunday);
    }

}
