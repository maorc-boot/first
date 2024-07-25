package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.jx.model.McdFiveGCloudEffectEvaluate;
import com.asiainfo.biapp.pec.eval.jx.req.G5CloudEvalPageQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 5G云卡效果评估mapper
 *
 * @author lvcc
 * @date 2023/10/12
 */
@Mapper
public interface McdFiveGCloudEffectEvaluateMapper extends BaseMapper<McdFiveGCloudEffectEvaluate> {

    IPage<McdFiveGCloudEffectEvaluate> queryPage(IPage page, @Param("query") G5CloudEvalPageQuery query);

}
