package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdMarketingHistoryDao;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdChanAanPlanMarketingHistoryQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdPlanMarketingHistoryQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdMarketingHistoryService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdChanPlanSuccessMarketingHistoryInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdPlanMarketingHistoryInfo;
import com.asiainfo.biapp.pec.plan.jx.utils.DateTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class McdMarketingHistoryServiceImpl implements McdMarketingHistoryService {


    private static final String YYMMDD = "yyyy-MM-dd";
    private static final int  DAY_NUM = 30;

    @Resource
    private McdMarketingHistoryDao mcdMarketingHistoryDao;
    @Override
    public List<McdPlanMarketingHistoryInfo> queryPlanMarketingHistory(McdPlanMarketingHistoryQuery req) {
        String nowDay = DateTool.getDateFormatStr(new Date(),YYMMDD);
        String targetDate = DateTool.getTargetDateByDate(nowDay,DAY_NUM);
        req.setStartDate(targetDate);
        req.setEndDate(nowDay);
        return mcdMarketingHistoryDao.queryPlanMarketingHistory(req);
    }


    @Override
    public List<McdChanPlanSuccessMarketingHistoryInfo> queryChanAndPlanMarketingHistory(McdChanAanPlanMarketingHistoryQuery req) {
        String nowDay = DateTool.getDateFormatStr(new Date(),YYMMDD);
        String targetDate = DateTool.getTargetDateByDate(nowDay,DAY_NUM);
        req.setStartDate(targetDate);
        req.setEndDate(nowDay);
        List<McdChanPlanSuccessMarketingHistoryInfo>  resultList   = mcdMarketingHistoryDao.queryChanAndPlanMarketingHistory(req);
        double totalNum = 0;
        for (McdChanPlanSuccessMarketingHistoryInfo historyInfo : resultList) {
             totalNum = totalNum + historyInfo.getCampsegNum();
        }

        if (totalNum == 0){
            return resultList;
        }

        for (McdChanPlanSuccessMarketingHistoryInfo historyInfo : resultList) {
            historyInfo.setSuccessRate(formatData((historyInfo.getCampsegNum()/totalNum)* 100 +"","0.00"));
        }

        return resultList;
    }



    private  String formatData(String data, String format) {
        if (StringUtils.isEmpty(format)) {
            // 默认保留两位小数
            format = "0.00";
        }
        DecimalFormat df = new DecimalFormat(format);
        try {
            if (StringUtils.isEmpty(data)) {
                return df.format(Double.valueOf("0"));
            }
            if (data.startsWith(".")) {
                data = "0" + data;
            }
            return df.format(Double.valueOf(data));
        } catch (Exception e) {
            log.error("formatData error！", e);
            data = df.format(Double.valueOf("0"));
        }
        return data;
    }
}
