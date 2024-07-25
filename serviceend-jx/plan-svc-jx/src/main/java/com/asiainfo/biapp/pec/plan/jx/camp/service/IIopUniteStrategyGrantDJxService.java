package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.model.IopUniteStrategyGrantD;
import com.asiainfo.biapp.pec.plan.vo.IopUnite;
import com.asiainfo.biapp.pec.plan.vo.RecomendUnityCampDetail;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 统一策略下发接口 IOP-91065 服务类
 * </p>
 *
 * @author imcd
 * @since 2022-01-20
 */
public interface IIopUniteStrategyGrantDJxService extends IService<IopUniteStrategyGrantD> {

    IPage<IopUnite> searchIopUnityTacticsInfo(IopUniteStrategyGrantD iopUnite, Page pager);


}
