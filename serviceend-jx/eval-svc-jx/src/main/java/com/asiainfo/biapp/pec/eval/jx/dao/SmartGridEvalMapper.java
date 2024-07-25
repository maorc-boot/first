package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.jx.req.SmartGridEvalReq;
import com.asiainfo.biapp.pec.eval.jx.vo.SmartGridEvalVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author mamp
 * @date 2023/1/9
 */
@Mapper
public interface SmartGridEvalMapper {
    /**
     * 活动评估
     *
     * @param req
     * @return
     */
    IPage<SmartGridEvalVo> querySmartGridEffect(Page page, @Param("param") SmartGridEvalReq req);

    /**
     * 活动评估-日视图
     *
     * @param req
     * @return
     */
    IPage<SmartGridEvalVo>   querySmartGridEffectDay(Page page,@Param("param") SmartGridEvalReq req);
    /**
     * 活动评估-月视图
     *
     * @param req
     * @return
     */
    IPage<SmartGridEvalVo>  querySmartGridEffectMonth(Page page,@Param("param") SmartGridEvalReq req);

}
