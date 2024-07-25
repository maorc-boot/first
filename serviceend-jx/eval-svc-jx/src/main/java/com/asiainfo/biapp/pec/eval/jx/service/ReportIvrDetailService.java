package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.model.ReportIvrDetail;
import com.asiainfo.biapp.pec.eval.jx.req.IvrPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-12-16
 */
public interface ReportIvrDetailService extends IService<ReportIvrDetail> {

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    IPage<ReportIvrDetail> queryPage(IvrPageQuery query);

    /**
     * 导出
     *
     * @param query
     * @return
     */
    List<List<String>> export(IvrPageQuery query);
}
