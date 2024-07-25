package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.service.IIopUniteStrategyGrantDJxService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IUnityTacticsManageJxService;
import com.asiainfo.biapp.pec.plan.model.*;
import com.asiainfo.biapp.pec.plan.vo.IopUnite;
import com.asiainfo.biapp.pec.plan.vo.req.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * UnityTacticsManageService.java
 *
 * @author [Li.wang]
 * @CreateDate 2022/1/20 0020
 * @see com.asiainfo.biapp.pec.plan.service.impl
 */
@Slf4j
@Service
public class UnityTacticsManageJxServiceImpl implements IUnityTacticsManageJxService {

    @Autowired
    private IIopUniteStrategyGrantDJxService iopUniteStrategyGrantDJxService;

    @Override
    public IPage<IopUnite> searchIopUnityTactics(SearchUnityTacticsQuery form) {
        Page pager = new Page<>(form.getCurrent(), form.getSize());
        IopUniteStrategyGrantD iopUnite = new IopUniteStrategyGrantD();
        iopUnite.setStrategyId(form.getStrategyId());
        iopUnite.setStrategyName(form.getStrategyName());
        iopUnite.setStrategyType(Long.valueOf(form.getStrategyType()));
        iopUnite.setProductCode(form.getProductCode());
        iopUnite.setStrategyStarttime(Long.valueOf(form.getStrategyStartTime()));
        iopUnite.setStrategyEndtime(Long.valueOf(form.getStrategyEndTime()));
        iopUnite.setStrategyBel(form.getStrategyBel());
        return iopUniteStrategyGrantDJxService.searchIopUnityTacticsInfo(iopUnite, pager);

    }


}
