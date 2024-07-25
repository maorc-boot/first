package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.plan.model.IopUniteStrategyGrantD;
import com.asiainfo.biapp.pec.plan.vo.IopUnite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 统一策略下发接口 IOP-91065 Mapper 接口
 * </p>
 *
 * @author imcd
 * @since 2022-12-20
 */
public interface IopUniteStrategyGrantDJxDao extends BaseMapper<IopUniteStrategyGrantD> {

    IPage<IopUnite> searchIopUnityTacticsInfo(@Param("param") IopUniteStrategyGrantD iopUnite, Page pager);

}
