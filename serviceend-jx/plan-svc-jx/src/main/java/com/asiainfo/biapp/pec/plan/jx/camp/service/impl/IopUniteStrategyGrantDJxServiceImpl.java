package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.dao.IopUniteStrategyGrantDJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IIopUniteStrategyGrantDJxService;
import com.asiainfo.biapp.pec.plan.model.IopUniteStrategyGrantD;
import com.asiainfo.biapp.pec.plan.vo.IopUnite;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 统一策略下发接口 IOP-91065 服务实现类
 * </p>
 *
 * @author imcd
 * @since 2022-01-20
 */
@Service
public class IopUniteStrategyGrantDJxServiceImpl extends ServiceImpl<IopUniteStrategyGrantDJxDao, IopUniteStrategyGrantD> implements IIopUniteStrategyGrantDJxService {

    @Override
    public IPage<IopUnite> searchIopUnityTacticsInfo(IopUniteStrategyGrantD iopUnite, Page pager) {
        return baseMapper.searchIopUnityTacticsInfo(iopUnite, pager);
    }


}
