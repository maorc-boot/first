package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.jx.model.McdFiveGEffectEvaluate;
import com.asiainfo.biapp.pec.eval.jx.req.G5EvalPageQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 5G效果评估表 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-12-15
 */
@Mapper
public interface McdFiveGEffectEvaluateMapper extends BaseMapper<McdFiveGEffectEvaluate> {

    IPage<McdFiveGEffectEvaluate> queryPage(IPage page, @Param("query") G5EvalPageQuery query);

}
