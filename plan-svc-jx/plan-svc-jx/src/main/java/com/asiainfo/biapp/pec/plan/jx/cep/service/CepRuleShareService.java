package com.asiainfo.biapp.pec.plan.jx.cep.service;

import com.asiainfo.biapp.pec.plan.jx.cep.model.CepRuleShare;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 事件共享表 服务类
 * </p>
 *
 * @author mamp
 * @since 2023-02-20
 */
public interface CepRuleShareService extends IService<CepRuleShare> {



    /**
     * 保存或者更新共享信息
     *
     * @param shareInfos
     * @return
     */
    boolean saveOrUpdate(List<CepRuleShare> shareInfos);

}
