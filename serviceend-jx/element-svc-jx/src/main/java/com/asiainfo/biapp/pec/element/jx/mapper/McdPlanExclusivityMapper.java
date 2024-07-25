package com.asiainfo.biapp.pec.element.jx.mapper;

import com.asiainfo.biapp.pec.element.jx.entity.McdPlanExclusivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产品互斥关系表 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-10-18
 */
@Mapper
public interface McdPlanExclusivityMapper extends BaseMapper<McdPlanExclusivity> {

    IPage<McdPlanExclusivity> getPlanExclusivityList(Page  page,@Param("keyWords") String  keyWords);
    List<McdPlanExclusivity> queryExclusByPlanId( @Param("planId") String  planId);
}
