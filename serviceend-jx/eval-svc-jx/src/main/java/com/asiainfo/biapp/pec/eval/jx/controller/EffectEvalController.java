package com.asiainfo.biapp.pec.eval.jx.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.utils.DateUtil;
import com.asiainfo.biapp.pec.eval.jx.constants.SpecialNumberJx;
import com.asiainfo.biapp.pec.eval.jx.req.EffectEvalReq;
import com.asiainfo.biapp.pec.eval.jx.service.EffectEvalService;
import com.asiainfo.biapp.pec.eval.jx.utils.ExportExcelUtil;
import com.asiainfo.biapp.pec.eval.util.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description: 营销效果评估控制层
 *
 * @author: lvchaochao
 * @date: 2023/1/12
 */
@Api(tags = {"江西：营销效果评估"}, value = "江西：营销效果评估")
@RequestMapping("/effect/eval")
@RestController
@Slf4j
public class EffectEvalController {

    @Autowired
    private EffectEvalService effectEvalService;

    @Autowired
    private ExportExcelUtil exportExcelUtil;

    @ApiOperation(value = "查询营销效果评估汇总数据", notes = "查询营销效果评估汇总数据")
    @PostMapping("/queryEffectEvalAllCount")
    public ActionResponse queryEffectEvalAllCount(@RequestBody EffectEvalReq req) {
        log.info("查询营销效果评估汇总数据入参：{}", JSONUtil.toJsonStr(req));
        try {
            List<Map<String, Object>> retrunList = effectEvalService.queryEffectEvaluationAllList(req);
            log.info("查询营销效果评估汇总数据sql返回：{}", JSONUtil.toJsonStr(retrunList));
            if ((Long) retrunList.get(SpecialNumberJx.ZERO_NUMBER).get("CAMPSEG_CNT") == SpecialNumberJx.ZERO_NUMBER)
                return ActionResponse.getSuccessResp("营销效果评估汇总数据查询成功").setData(null);
            retrunList.forEach(returnMap -> {
                returnMap.put("QRY_DATE", StrUtil.format("{}至{}", req.getStartDate(), req.getEndDate()));
                returnMap.put("QRY_CITY_ID", req.getCityId());
                if (StringUtils.isBlank(req.getCityId())) {
                    returnMap.put("QRY_CITY_NAME", "全部");
                } else {
                    returnMap.put("QRY_CITY_NAME", returnMap.get("CITY_NAME"));
                }
                if (StringUtils.isBlank(req.getProductType())) {
                    returnMap.put("QRY_ACTIVE_NAME", "全部类型");
                } else {
                    returnMap.put("QRY_ACTIVE_NAME", returnMap.get("LABEL_VALUE"));
                }
                returnMap.put("QRY_CHANNEL_ID", req.getChannelId());
                if (StringUtils.isBlank(req.getChannelId())) {
                    returnMap.put("QRY_CHANNEL_NAME", "全部渠道");
                } else {
                    returnMap.put("QRY_CHANNEL_NAME", returnMap.get("CHANNEL_NAME"));
                }
            });
            log.info("查询营销效果评估汇总数据处理后返回：{}", JSONUtil.toJsonStr(retrunList));
            return ActionResponse.getSuccessResp("营销效果评估汇总数据查询成功").setData(retrunList);
        } catch (Exception e) {
            log.error("营销效果评估汇总数据查询异常：", e);
            return ActionResponse.getFaildResp("营销效果评估汇总数据查询失败").setData(null);
        }
    }

    @ApiOperation(value = "根据下钻类型查询营销效果评估下钻数据", notes = "根据下钻类型查询营销效果评估下钻数据")
    @PostMapping("/getDetailByDrillDownType")
    public Pager getDetailByDrillDownType(@RequestBody EffectEvalReq req) {
        log.info("根据下钻类型查询营销效果评估下钻数据入参：{}", JSONUtil.toJsonStr(req));
        Pager pager = null;
        try {
            pager = new Pager();
            pager.setPageSize(req.getPageSize());
            pager.setPageNum(req.getPageNum()); // 当前页
            pager.setPageFlag("G");
            List<Map<String, Object>> retrunList = effectEvalService.getDetailByDrillDownType(pager, req);
            if (req.isDetail()) {
                // 查询详情
                retrunList.forEach(returnMap -> {
                   if ("month".equals(req.getDateType())) {
                       String yyyymm = (String) returnMap.get("STAT_DATE");
                       String startDateResult = DateUtil.getFirstDayByMonth(yyyymm, "yyyy-MM", "yyyy-MM-dd");
                       String endDateResult = DateUtil.getLastDayByMonth(yyyymm, "yyyy-MM", "yyyy-MM-dd");
                       returnMap.put("START_DATE", startDateResult);
                       returnMap.put("END_DATE", endDateResult);
                   }
                });
            }
            pager.setTotalSize(effectEvalService.getDetailByDrillDownTypeCount(req));
            pager.setResult(retrunList);
        } catch (Exception e) {
            log.error("根据下钻类型查询营销效果评估下钻数据异常：", e);
        }
        return pager;
    }

    @ApiOperation(value = "导出营销效果评估信息", notes = "导出营销效果评估信息")
    @PostMapping("/exportEffectEval")
    public void exportEffectEval(@RequestBody EffectEvalReq req, HttpServletResponse response) {
        log.info("江西导出营销效果评估请求入参：{}", JSONUtil.toJsonStr(req));
        try {
            List<Map<String, Object>> list;
            Pager pager = new Pager();
            pager.setPageSize(req.getPageSize());
            pager.setPageNum(req.getPageNum()); // 当前页
            pager.setPageFlag("G");
            if (req.isDrillDownExport()) { // 下钻
                list = effectEvalService.getDetailByDrillDownType(pager, req);
            } else { // 汇总
                list = effectEvalService.queryEffectEvaluationAllList(req);
            }
            if (req.isDetail()) {
                // 查询详情
                list.forEach(returnMap -> {
                    if ("month".equals(req.getDateType())) {
                        String yyyymm = (String) returnMap.get("STAT_DATE");
                        String startDateResult = DateUtil.getFirstDayByMonth(yyyymm, "yyyy-MM", "yyyy-MM-dd");
                        String endDateResult = DateUtil.getLastDayByMonth(yyyymm, "yyyy-MM", "yyyy-MM-dd");
                        returnMap.put("START_DATE", startDateResult);
                        returnMap.put("END_DATE", endDateResult);
                    }
                });
            }
            // 存放构建后的excel列表数据
            List<Object> rs = new ArrayList<>();
            // 构建导出excel内容数据以及表头信息
            String[] excelHeader = buildExportExcelInfo(req, list, rs);
            exportExcelUtil.exportExcel(excelHeader, rs, response, "营销效果评估");
        } catch (Exception e) {
            log.error("江西导出营销效果评估信息异常: ", e);
        }
    }

    /**
     * 构建导出excel内容数据以及表头信息
     *
     * @param req  req
     * @param list 列表数据
     * @param rs   rs
     * @return {@link String[]}
     */
    private String[] buildExportExcelInfo(EffectEvalReq req, List<Map<String, Object>> list, List<Object> rs) {
        String[] excelHeader;
        if (!req.isDrillDownExport()) { // 汇总导出
            list.forEach(map -> {
                Object[] obj = new Object[SpecialNumberJx.TEN_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = StrUtil.format("{}至{}", req.getStartDate(), req.getEndDate());
                obj[SpecialNumberJx.ONE_NUMBER] = StrUtil.isEmpty(req.getCityId()) ? "全部" : map.get("CITY_NAME");
                obj[SpecialNumberJx.TWO_NUMBER] = StrUtil.isEmpty(req.getProductType()) ? "全部类型" : map.get("LABEL_VALUE");
                obj[SpecialNumberJx.THREE_NUMBER] = StrUtil.isEmpty(req.getChannelId()) ? "全部渠道" : map.get("CHANNEL_NAME");
                obj[SpecialNumberJx.FOUR_NUMBER] = map.get("CAMPSEG_CNT");  // 策略个数
                obj[SpecialNumberJx.FIVE_NUMBER] = map.get("CAMP_USER_NUM_CNT"); // 策略用户数
                obj[SpecialNumberJx.SIX_NUMBER] = map.get("TARGET_USER_NUM_CNT"); // 接触用户数
                obj[SpecialNumberJx.SEVEN_NUMBER] = map.get("CAMP_USER_RATE"); // 接触率
                obj[SpecialNumberJx.EIGHT_NUMBER] = map.get("CAMP_SUCC_NUM_CNT"); // 成功用户数
                obj[SpecialNumberJx.NINE_NUMBER] = map.get("SUCC_RATE"); // 成功率
                rs.add(obj);
            });
            excelHeader = new String[]{"周期", "区域", "产品类型", "渠道", "策略个数", "策略用户数", "接触用户数", "接触率", "成功用户数", "成功率"};
        } else if (req.isDetail()) { // 详情导出
            list.forEach(map -> {
                Object[] obj = new Object[SpecialNumberJx.ELEVEN_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = map.get("STAT_DATE");
                obj[SpecialNumberJx.ONE_NUMBER] = map.get("LABEL_VALUE");
                obj[SpecialNumberJx.TWO_NUMBER] = map.get("CAMPSEG_NAME");
                obj[SpecialNumberJx.THREE_NUMBER] = map.get("PLAN_NAME");
                obj[SpecialNumberJx.FOUR_NUMBER] = map.get("CITY_NAME");
                obj[SpecialNumberJx.FIVE_NUMBER] = map.get("COUNTY_NAME");
                obj[SpecialNumberJx.SIX_NUMBER] = map.get("CAMP_USER_NUM_CNT"); // 策略用户数
                obj[SpecialNumberJx.SEVEN_NUMBER] = map.get("TARGET_USER_NUM_CNT"); // 接触用户数
                obj[SpecialNumberJx.EIGHT_NUMBER] = map.get("CAMP_USER_RATE"); // 接触率
                obj[SpecialNumberJx.NINE_NUMBER] = map.get("CAMP_SUCC_NUM_CNT"); // 成功用户数
                obj[SpecialNumberJx.TEN_NUMBER] = map.get("SUCC_RATE"); // 成功率
                rs.add(obj);
            });
            excelHeader = new String[]{"统计日期", "产品类型", "活动名称", "产品名称", "地市", "区县", "策略用户数", "接触用户数", "接触率", "成功用户数", "成功率"};
        } else if (req.getDrillDownType().equals("time")) { // 周期下钻
            list.forEach(map -> {
                Object[] obj = new Object[SpecialNumberJx.SEVEN_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = map.get("STAT_DATE");
                obj[SpecialNumberJx.ONE_NUMBER] = map.get("CAMPSEG_CNT");
                obj[SpecialNumberJx.TWO_NUMBER] = map.get("CAMP_USER_NUM_CNT");
                obj[SpecialNumberJx.THREE_NUMBER] = map.get("TARGET_USER_NUM_CNT");
                obj[SpecialNumberJx.FOUR_NUMBER] = map.get("CAMP_USER_RATE");
                obj[SpecialNumberJx.FIVE_NUMBER] = map.get("CAMP_SUCC_NUM_CNT");
                obj[SpecialNumberJx.SIX_NUMBER] = map.get("SUCC_RATE");
                rs.add(obj);
            });
            excelHeader = new String[]{"周期", "策略个数", "策略用户数", "接触用户数", "接触率", "成功用户数", "成功率"};
        } else if (req.getDrillDownType().equals("province") || req.getDrillDownType().equals("city")) { // 省级下钻或地市下钻
            list.forEach(map -> {
                Object[] obj = new Object[SpecialNumberJx.SEVEN_NUMBER];
                if (req.getDrillDownType().equals("province")) {
                    obj[SpecialNumberJx.ZERO_NUMBER] = map.get("CITY_NAME"); // 地市名
                } else {
                    obj[SpecialNumberJx.ZERO_NUMBER] = map.get("COUNTY_NAME"); // 区县名
                }
                obj[SpecialNumberJx.ONE_NUMBER] = map.get("CAMPSEG_CNT");
                obj[SpecialNumberJx.TWO_NUMBER] = map.get("CAMP_USER_NUM_CNT");
                obj[SpecialNumberJx.THREE_NUMBER] = map.get("TARGET_USER_NUM_CNT");
                obj[SpecialNumberJx.FOUR_NUMBER] = map.get("CAMP_USER_RATE");
                obj[SpecialNumberJx.FIVE_NUMBER] = map.get("CAMP_SUCC_NUM_CNT");
                obj[SpecialNumberJx.SIX_NUMBER] = map.get("SUCC_RATE");
                rs.add(obj);
            });
            excelHeader = new String[]{"区域", "策略个数", "策略用户数", "接触用户数", "接触率", "成功用户数", "成功率"};
        } else if (req.getDrillDownType().equals("productType")) { // 产品类型下钻
            list.forEach(map -> {
                Object[] obj = new Object[SpecialNumberJx.SEVEN_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = map.get("LABEL_VALUE");
                obj[SpecialNumberJx.ONE_NUMBER] = map.get("CAMPSEG_CNT");
                obj[SpecialNumberJx.TWO_NUMBER] = map.get("CAMP_USER_NUM_CNT");
                obj[SpecialNumberJx.THREE_NUMBER] = map.get("TARGET_USER_NUM_CNT");
                obj[SpecialNumberJx.FOUR_NUMBER] = map.get("CAMP_USER_RATE");
                obj[SpecialNumberJx.FIVE_NUMBER] = map.get("CAMP_SUCC_NUM_CNT");
                obj[SpecialNumberJx.SIX_NUMBER] = map.get("SUCC_RATE");
                rs.add(obj);
            });
            excelHeader = new String[]{"产品类型", "策略个数", "策略用户数", "接触用户数", "接触率", "成功用户数", "成功率"};
        } else if (req.getDrillDownType().equals("channel")) { // 渠道下钻
            list.forEach(map -> {
                Object[] obj = new Object[SpecialNumberJx.SEVEN_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = map.get("CHANNEL_NAME");
                obj[SpecialNumberJx.ONE_NUMBER] = map.get("CAMPSEG_CNT");
                obj[SpecialNumberJx.TWO_NUMBER] = map.get("CAMP_USER_NUM_CNT");
                obj[SpecialNumberJx.THREE_NUMBER] = map.get("TARGET_USER_NUM_CNT");
                obj[SpecialNumberJx.FOUR_NUMBER] = map.get("CAMP_USER_RATE");
                obj[SpecialNumberJx.FIVE_NUMBER] = map.get("CAMP_SUCC_NUM_CNT");
                obj[SpecialNumberJx.SIX_NUMBER] = map.get("SUCC_RATE");
                rs.add(obj);
            });
            excelHeader = new String[]{"渠道", "策略个数", "策略用户数", "接触用户数", "接触率", "成功用户数", "成功率"};
        } else { // 运营位下钻
            list.forEach(map -> {
                Object[] obj = new Object[SpecialNumberJx.EIGHT_NUMBER];
                obj[SpecialNumberJx.ZERO_NUMBER] = map.get("CHANNEL_NAME");
                obj[SpecialNumberJx.ONE_NUMBER] = map.get("ADIV_NAME");
                obj[SpecialNumberJx.TWO_NUMBER] = map.get("CAMPSEG_CNT");
                obj[SpecialNumberJx.THREE_NUMBER] = map.get("CAMP_USER_NUM_CNT");
                obj[SpecialNumberJx.FOUR_NUMBER] = map.get("TARGET_USER_NUM_CNT");
                obj[SpecialNumberJx.FIVE_NUMBER] = map.get("CAMP_USER_RATE");
                obj[SpecialNumberJx.SIX_NUMBER] = map.get("CAMP_SUCC_NUM_CNT");
                obj[SpecialNumberJx.SEVEN_NUMBER] = map.get("SUCC_RATE");
                rs.add(obj);
            });
            excelHeader = new String[]{"渠道", "运营位", "策略个数", "策略用户数", "接触用户数", "接触率", "成功用户数", "成功率"};
        }
        return excelHeader;
    }

    public static void main(String[] args) {
        // double div = NumberUtil.div(10, 50, 4, RoundingMode.DOWN);
        // String format = NumberUtil.decimalFormat("0.00%", div);
        // System.out.println(format);

        // BigDecimal succRate = (10).divide(50);
        // DecimalFormat df = new DecimalFormat("0.00%");
        // String percent = df.format(succRate);
        String str = "2023-01-16";
        String str2 = "2023-01-16";
        // System.out.println(StrUtil.format("{},{}",str, str));
        System.out.println(StrUtil.join(",", str, str));
    }
}
