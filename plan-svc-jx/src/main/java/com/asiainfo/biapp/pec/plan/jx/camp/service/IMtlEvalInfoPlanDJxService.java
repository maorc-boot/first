package com.asiainfo.biapp.pec.plan.jx.camp.service;


import com.asiainfo.biapp.pec.plan.model.MtlEvalInfoPlanD;
import com.asiainfo.biapp.pec.plan.vo.SaleSituationVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 产品效果评估（日） 服务类
 * </p>
 *
 * @author ranpf
 * @since 2023-5-16
 */
public interface IMtlEvalInfoPlanDJxService extends IService<MtlEvalInfoPlanD> {

    SaleSituationVO querySaleSituation(String userCity);

}
