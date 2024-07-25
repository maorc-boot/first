package com.asiainfo.biapp.pec.eval.jx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.utils.DateUtil;
import com.asiainfo.biapp.pec.eval.jx.constants.EvalConstant;
import com.asiainfo.biapp.pec.eval.jx.dao.EvalHomeDao;
import com.asiainfo.biapp.pec.eval.jx.req.CampStatReq;
import com.asiainfo.biapp.pec.eval.jx.req.EvalHomeChnExecReq;
import com.asiainfo.biapp.pec.eval.jx.req.EvalHomeReq;
import com.asiainfo.biapp.pec.eval.jx.service.EvalHomeService;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeCampStatVO;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeChnExecVO;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mamp
 * @date 2023/5/6
 */
@Service
@Slf4j
public class EvalHomeServiceImpl implements EvalHomeService {

    @Resource
    private EvalHomeDao evalHomeDao;

    /**
     * 总营销数
     *
     * @param req
     * @return
     */
    @Override
    public List<EvalHomeDataVo> queryTotal(EvalHomeReq req) throws Exception {
        validParam(req);
        List<EvalHomeDataVo> list = evalHomeDao.queryTotal(req);
        return handleDateFormat(list, req);
    }

    /**
     * 总营成功数
     *
     * @param req
     * @return
     */
    @Override
    public List<EvalHomeDataVo> querySuccess(EvalHomeReq req) throws Exception {
        validParam(req);
        List<EvalHomeDataVo> list = evalHomeDao.querySuccess(req);
        return handleDateFormat(list, req);
    }

    /**
     * 营销转化率
     *
     * @param req
     * @return
     */
    @Override
    public List<EvalHomeDataVo> querySuccessRate(EvalHomeReq req) throws Exception {
        validParam(req);
        List<EvalHomeDataVo> list = evalHomeDao.querySuccessRate(req);
        //handleDateFormat(list, req);
        return handleDateFormat(list, req);
    }

    /**
     * 评估-效果总览-渠道执行情况
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public List<EvalHomeDataVo> queryChnExec(EvalHomeChnExecReq req) throws Exception {
        req.setCurrentMonth(dateFormat((req.getCurrentMonth())));
        List<EvalHomeChnExecVO> evalHomeChnExecVOS = evalHomeDao.queryChnExec(req);
        List<EvalHomeDataVo> reults = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(evalHomeChnExecVOS)) {
            EvalHomeChnExecVO execVO = evalHomeChnExecVOS.get(0);
            reults.add(new EvalHomeDataVo("总营销数", execVO.getTotal()));
            reults.add(new EvalHomeDataVo("营销成功数", execVO.getSuccess()));
            reults.add(new EvalHomeDataVo("营销成功率", execVO.getSuccessRate()));
            return reults;
        }
        reults.add(new EvalHomeDataVo("总营销数", "0"));
        reults.add(new EvalHomeDataVo("营销成功数", "0"));
        reults.add(new EvalHomeDataVo("营销成功率", "0"));
        return reults;
    }

    /**
     * 评估-效果总览-渠道执行情况-近6个月成功数趋势
     *
     * @param req
     * @return
     */
    @Override
    public List<EvalHomeChnExecVO> queryChnExecSuccess(EvalHomeChnExecReq req) throws Exception {
        String currentMonth = req.getCurrentMonth();
        // 6个月前
        String startMonth = DateUtil.addMonth(currentMonth, "yyyy-MM", "yyyy-MM", -5);
        req.setStartMonth(dateFormat(startMonth));
        req.setEndMonth(dateFormat(currentMonth));
        req.setCurrentMonth(null);
        List<EvalHomeChnExecVO> vos = evalHomeDao.queryChnExec(req);
        Map<String, EvalHomeChnExecVO> tempMap = new HashMap<>();

        for (EvalHomeChnExecVO vo : vos) {
            if (StrUtil.isNotEmpty(vo.getStatTime()) && vo.getStatTime().length() > 7) {
                vo.setStatTime(vo.getStatTime().substring(0, 7));
            }
            tempMap.put(vo.getStatTime(), vo);
        }

        List<EvalHomeChnExecVO> resultList = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            String month = DateUtil.addMonth(req.getEndMonth(), "yyyy-MM", "yyyy-MM", 0 - i);
            EvalHomeChnExecVO vo = tempMap.get(month);
            if (null == vo) {
                vo = new EvalHomeChnExecVO(month, "0", "0", "0");
            }
            resultList.add(vo);
        }
        return resultList;
    }

    /**
     * 前台传的数据为 yyyy-MM 表里的数据为 yyyy-MM-01
     *
     * @param date
     * @return
     */
    private String dateFormat(String date) {
        if (StrUtil.isEmpty(date)) {
            return date;
        }
        return StrUtil.format("{}-01", date);
    }

    /**
     * 评估-效果总览-渠道执行情况-近6个月成功率趋势
     *
     * @param req
     * @return
     */
    @Override
    public List<EvalHomeChnExecVO> queryChnExecSuccessRate(EvalHomeChnExecReq req) throws Exception {
        return queryChnExecSuccess(req);
    }

    /**
     * 评估-效果总览-策略统计
     *
     * @param req
     * @return
     */
    @Override
    public List<EvalHomeCampStatVO> queryCampStatistics(CampStatReq req) throws Exception {
        if (StrUtil.isNotEmpty(req.getCampsegIds())) {
            req.setCampsegIdList(Arrays.asList(req.getCampsegIds().split(",")));
        }
        if (StrUtil.isNotEmpty(req.getStartDate())) {
            req.setStartDate(StrUtil.format("{} 00:00:00", req.getStartDate()));
        }
        if (StrUtil.isNotEmpty(req.getEndDate())) {
            req.setEndDate(StrUtil.format("{} 23:59:59", req.getEndDate()));
        }
        return evalHomeDao.queryCampStatistics(req);
    }

    /**
     * 查询策略列表
     *
     * @return
     */
    @Override
    public List queryCampList(String keyword) {
        return evalHomeDao.queryCampList(keyword);
    }


    /**
     * 参数校验
     *
     * @param req
     * @throws Exception
     */
    private void validParam(EvalHomeReq req) throws Exception {
        List<String> typeList = Arrays.asList(EvalConstant.HOME_EVAL_DATA_ALL, EvalConstant.HOME_EVAL_DATA_MONTH);
        List<String> groupByList = Arrays.asList(EvalConstant.HOME_EVAL_GROUPBY_TIME, EvalConstant.HOME_EVAL_GROUPBY_CHANNEL, EvalConstant.HOME_EVAL_GROUPBY_CITY);
        if (!typeList.contains(req.getType())) {
            throw new Exception("请求参数不合法");
        }
        if (!groupByList.contains(req.getGroupByCol())) {
            throw new Exception("请求参数不合法");
        }

        if (EvalConstant.HOME_EVAL_DATA_MONTH.equals(req.getType())) {
            Date now = new Date();
            String monthStr = DateUtil.formatDateToStr(now, "yyyyMM");
            String firstDay = StrUtil.format("{}01", monthStr);
            String lastDay = DateUtil.getLastDayByMonth(monthStr, "yyyyMM", "yyyyMMdd");
            req.setStartDate(firstDay);
            req.setEndDate(lastDay);
        }
    }

    /**
     * 处理日期格式，将 yyyy-MM-dd 00:00:00 处理为 yyyy-MM-dd
     *
     * @param list
     */
    private List<EvalHomeDataVo> handleDateFormat(List<EvalHomeDataVo> list, EvalHomeReq req) {
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }
        if (EvalConstant.HOME_EVAL_GROUPBY_TIME.equals(req.getGroupByCol())) {
            return handleDate(list, req);
        }
        /*if (EvalConstant.HOME_EVAL_GROUPBY_CITY.equals(req.getGroupByCol())) {
            return handleCity(list, req);
        }*/
        return list;
    }

    private List<EvalHomeDataVo> handleDate(List<EvalHomeDataVo> list, EvalHomeReq req) {
        List<EvalHomeDataVo> resultList = new ArrayList<>();
        Map<String, String> dataMap = new HashMap<>();
        if (EvalConstant.HOME_EVAL_DATA_ALL.equals(req.getType())) {
            for (EvalHomeDataVo dataVo : list) {
                if (StrUtil.isNotEmpty(dataVo.getName()) && dataVo.getName().length() > 8) {
                    dataVo.setName(dataVo.getName().substring(0, 7));
                }
                dataMap.put(dataVo.getName(), dataVo.getValue());
            }
            String now = DateUtil.formatDateToStr(new Date(), "yyyyMM");
            for (int i = 12; i > 0; i--) {
                String month = DateUtil.addMonth(now, "yyyyMM", "yyyyMM", 0 - i);
                String value = dataMap.get(month) == null ? "0" : dataMap.get(month);
                resultList.add(new EvalHomeDataVo(month, value));
            }
        }
        if (EvalConstant.HOME_EVAL_DATA_MONTH.equals(req.getType())) {
            for (EvalHomeDataVo dataVo : list) {
                if (StrUtil.isNotEmpty(dataVo.getName()) && dataVo.getName().length() > 10) {
                    dataVo.setName(dataVo.getName().substring(0, 10));
                }
                dataMap.put(dataVo.getName(), dataVo.getValue());
            }
            String now = DateUtil.formatDateToStr(new Date(), "yyyyMM");
            int lastDayOfTheMonth = DateUtil.getLastDayOfTheMonth(DateUtil.formatDateToStr(new Date(), "yyyy-MM"));
            for (int i = 0; i < lastDayOfTheMonth; i++) {
                String day = DateUtil.addDay(now + "01", "yyyyMMdd", "yyyyMMdd", i);
                String value = dataMap.get(day) == null ? "0" : dataMap.get(day);
                resultList.add(new EvalHomeDataVo(day, value));
            }
        }
        return resultList;
    }

    /**
     * 处理地市数据
     *
     * @param list
     * @param req
     * @return
     */
    private List<EvalHomeDataVo> handleCity(List<EvalHomeDataVo> list, EvalHomeReq req) {
        List<EvalHomeDataVo> resultList = new ArrayList<>();
        Map<String, String> dataMap = new HashMap<>();
        for (EvalHomeDataVo vo : list) {
            dataMap.put(vo.getName(), vo.getValue());
        }
        List<Map<String, String>> citys = evalHomeDao.queryAllCity();
        for (Map<String, String> city : citys) {
            String value = dataMap.get(city.get("CITY_ID"));
            String cityName = city.get("CITY_NAME");
            if (StrUtil.isEmpty(value)) {
                value = "0";
            }
            resultList.add(new EvalHomeDataVo(cityName, value));
        }
        return resultList;
    }


    public static void main(String[] args) {
  /*      Date now = new Date();
        String monthStr = DateUtil.formatDateToStr(now, "yyyy-MM");
        String firstDay = StrUtil.format("{}-01", monthStr);
        String lastDay = DateUtil.getLastDayByMonth(monthStr, "yyyy-MM", "yyyy-MM-dd");
        System.out.println(firstDay);
        System.out.println(lastDay);*/

        String startMonth = DateUtil.addMonth("2013-04", "yyyy-MM", "yyyy-MM", -5);
        System.out.println(startMonth);

        int lastDayOfTheMonth = DateUtil.getLastDayOfTheMonth(DateUtil.formatDateToStr(new Date(), "yyyy-MM"));
        System.out.println(lastDayOfTheMonth);
    }
}
