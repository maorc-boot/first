package com.asiainfo.biapp.pec.eval.jx.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.eval.jx.constants.SpecialNumberJx;
import com.asiainfo.biapp.pec.eval.jx.dao.ChannelEvalJxMapper;
import com.asiainfo.biapp.pec.eval.jx.service.ChannelEvalJxService;
import com.asiainfo.biapp.pec.eval.model.req.ChannelEvaluateReqModel;
import com.asiainfo.biapp.pec.eval.util.CommonUtil;
import com.asiainfo.biapp.pec.eval.util.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * description: 江西渠道评估service实现
 *
 * @author: lvchaochao
 * @date: 2023/1/11
 */
@Service
@Slf4j
public class ChannelEvalJxServiceImpl implements ChannelEvalJxService {

    @Autowired
    private ChannelEvalJxMapper channelEvalJxMapper;

    /**
     * 获取渠道评估数据
     *
     * @param model model
     * @param pager pager
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> getChannelEffectList(ChannelEvaluateReqModel model, Pager pager) {
        // generatorTimeParam(model);
        String[] dateArr = model.getDate().split(StrUtil.COMMA);
        model.setDateDayStart(dateArr[SpecialNumberJx.ZERO_NUMBER]);
        model.setDateDayEnd(dateArr[SpecialNumberJx.ONE_NUMBER]);
        int pageStart = (pager.getPageNum()-1)* pager.getPageSize();
        int pageSize = pager.getPageNum()* pager.getPageSize();

        model.setPageStart(pageStart);
        model.setPageSize(pageSize);
        log.info("江西获取渠道评估数据查询sql入参：{}", JSONUtil.toJsonStr(model));
        List<Map<String, Object>> list = channelEvalJxMapper.getChannelEffectList(model);
        List<String> numFileds = Collections.singletonList("CAMP_SUCCESS_RADIO");
        CommonUtil.convertNumFiled(list, numFileds);
        Integer total = channelEvalJxMapper.getChannelEffectListTotal(model);
        pager.setTotalSize(total == null ? SpecialNumberJx.ZERO_NUMBER : total);
        return list;
    }

    /**
     * 通过地市id获取地市信息
     *
     * @param cityId 地市id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> getCityById(String cityId) {
        return channelEvalJxMapper.getCityById(cityId);
    }

    /**
     * 组装日期参数
     *
     * @param model model
     */
    private static void generatorTimeParam(ChannelEvaluateReqModel model) {
        String date = model.getDate();
        String[] dateList = null;
        try {
            dateList = getDateTypeDay(date);
        } catch (Exception e) {
            log.error("活动效果评估解析时间参数异常：", e);
            return;
        }
        String effectPageDate_day_startS = "";
        String effectPageDate_day_StratE = "";
        if (dateList.length != 0) {
            if (dateList.length == 1) {
                effectPageDate_day_startS = dateList[0];
                effectPageDate_day_StratE = dateList[0];
                if (date.indexOf(",") == date.length() - 1) {
                    model.setDateDayStart(effectPageDate_day_startS);
                } else {
                    model.setDateDayEnd(effectPageDate_day_StratE);
                }
            } else {
                effectPageDate_day_startS = dateList[0];
                effectPageDate_day_StratE = dateList[1];
                model.setDateDayStart(effectPageDate_day_startS);
                model.setDateDayEnd(effectPageDate_day_StratE);
            }
        }
    }

    /**
     * 获取日期数据
     *
     * @param date 日期
     * @return {@link String[]}
     * @throws Exception 异常
     */
    private static String[] getDateTypeDay(String date) throws Exception {
        // 处理日期
        String[] dateArr = date.split(StrUtil.COMMA);
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < dateArr.length; i++) {
            Pattern p = Pattern.compile("[^0-9]");
            String d = p.matcher(dateArr[i]).replaceAll("").trim();
            Calendar c = Calendar.getInstance();
            if (d.length() == 0) {
                continue;
            }
            if (d.length() == 6) {
                if (i <= 0) {
                    c.setTime(DateUtil.parse(d, "yyyy-MM"));
                } else {
                    c.setTime(DateUtil.parse(d, "yyyy-MM"));
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
             } else if (d.length() == 8) {
                SimpleDateFormat isdf = new SimpleDateFormat("yyyy-MM-dd");
                c.setTime(isdf.parse(d));
            }
            dateList.add(DateUtil.format(c.getTime(), "yyyy-MM-dd"));
        }
        return dateList.toArray(new String[dateList.size()]);
    }

    // public static void main(String[] args) {
    //     ChannelEvaluateReqModel model = new ChannelEvaluateReqModel();
    //     model.setDate("2023-01-10,2023-01-10");
    //     generatorTimeParam(model);
    // }
}
