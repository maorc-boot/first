package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.model.DwKehutongClInfo;
import com.asiainfo.biapp.pec.eval.jx.req.CityCampPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分策略分地市分渠道报表 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-12-19
 */
public interface DwKehutongClInfoService extends IService<DwKehutongClInfo> {

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    IPage<DwKehutongClInfo> queryPage(CityCampPageQuery query);

    /**
     * 导出
     *
     * @param query
     * @return
     */
    List<List<String>> export(CityCampPageQuery query);
}
