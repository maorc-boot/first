package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.jx.model.ReportChannelExprDt;
import com.asiainfo.biapp.pec.eval.jx.model.ReportProductExprDt;
import com.asiainfo.biapp.pec.eval.jx.req.ReportFormReq;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description: 产品分渠道订购报表mapper
 *
 * @author: lvchaochao
 * @date: 2023/2/8
 */
public interface ReportChannelMapper {

    /**
     * 获取产品分渠道订购报表列表数据
     *
     * @param pager     pager
     * @param opTime    操作时间
     * @param tableName 表名
     * @return {@link IPage}<{@link ReportProductExprDt}>
     */
    IPage<ReportChannelExprDt> getOrderProductDMDataByPage(Page<ReportChannelExprDt> pager, @Param("opTime") String opTime, @Param("tableName") String tableName);

    List<ReportChannelExprDt> getOrderProductDMDataByPage(@Param("opTime") String opTime, @Param("tableName") String tableName);

    /**
     * 判断表是否存在
     *
     * @param tableName tableName
     */
    void getExitTable(@Param(value="tableName") String tableName);
}
