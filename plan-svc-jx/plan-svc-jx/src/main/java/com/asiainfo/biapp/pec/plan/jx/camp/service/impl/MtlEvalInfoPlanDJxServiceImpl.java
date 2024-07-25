package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.plan.dao.MtlEvalInfoPlanDDao;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.MtlEvalInfoPlanDJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMtlEvalInfoPlanDJxService;
import com.asiainfo.biapp.pec.plan.model.MtlEvalInfoPlanD;
import com.asiainfo.biapp.pec.plan.vo.SaleSituationVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * <p>
 * 产品效果评估（日） 服务实现类
 * </p>
 *
 * @author ranpf
 * @since 2023-5-16
 */
@Service
@Slf4j
public class MtlEvalInfoPlanDJxServiceImpl extends ServiceImpl<MtlEvalInfoPlanDDao, MtlEvalInfoPlanD> implements IMtlEvalInfoPlanDJxService {

    @Resource
    private MtlEvalInfoPlanDJxDao mtlEvalInfoPlanDJxDao;

    @Override
    public SaleSituationVO querySaleSituation(String userCity) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
            //昨日
            Map<String, Object> dayMap = mtlEvalInfoPlanDJxDao.querySaleNumDayAndSaleSuccessNumDay(userCity);
            String saleNumDay = decimalFormat.format(dayMap.get("saleNumDay"));
            String saleSuccessNumDay = decimalFormat.format(dayMap.get("saleSuccessNumDay"));
            //String saleNumDay = dayMap.get("saleNumDay").toString();
            //String saleSuccessNumDay = dayMap.get("saleSuccessNumDay").toString();
            //本月
            Map<String, Object> monMap = mtlEvalInfoPlanDJxDao.querySaleNumMonAndSaleSuccessNumMon(userCity);
            String saleNumMon = decimalFormat.format(monMap.get("saleNumMon"));
            String saleSuccessNumMon = decimalFormat.format(monMap.get("saleSuccessNumMon"));

            //String saleNumMon = monMap.get("saleNumMon").toString();
            //String saleSuccessNumMon = monMap.get("saleSuccessNumMon").toString();
            //往月历史
            Map<String, Object> pastMap = mtlEvalInfoPlanDJxDao.querySaleNumAndSaleSuccessNumPast(userCity);
            String saleNumPast = pastMap.get("saleNumPast").toString();
            String saleSuccessNumPast = pastMap.get("saleSuccessNumPast").toString();
            //总量

            Double v = Double.parseDouble(monMap.get("saleNumMon").toString()) + Double.parseDouble(saleNumPast);
            String totalNum = decimalFormat.format(v);
            //String totalNum = v.toString();
            Double v1 = Double.parseDouble(monMap.get("saleSuccessNumMon").toString() ) + Double.parseDouble(saleSuccessNumPast);
            String totalSuccessNum = decimalFormat.format(v1);
            //String totalSuccessNum = v1.toString();

            SaleSituationVO saleSituationVO = new SaleSituationVO();
            //日触发数
            saleSituationVO.setSaleNumDay(saleNumDay);
            //日办理数
            saleSituationVO.setSaleSuccessNumDay(saleSuccessNumDay);
            //月触发数
            saleSituationVO.setSaleNumMon(saleNumMon);
            //月办理数
            saleSituationVO.setSaleSuccessNumMon(saleSuccessNumMon);
            //总触发营销数
            saleSituationVO.setTotalNum(totalNum);
            //总办理数
            saleSituationVO.setTotalSuccessNum(totalSuccessNum);
            return saleSituationVO;
        }catch (Exception e){
            log.info("查询触发办理情况出错"+e);
            throw new BaseException("查询触发办理情况出错");
        }
    }
}
