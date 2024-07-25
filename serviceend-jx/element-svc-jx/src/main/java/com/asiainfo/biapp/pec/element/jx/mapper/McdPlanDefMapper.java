package com.asiainfo.biapp.pec.element.jx.mapper;

import com.asiainfo.biapp.pec.element.jx.model.McdPlanDef;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanDefInfo;
import com.asiainfo.biapp.pec.element.jx.query.RequestPlanQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-11-08
 */
@Mapper
public interface McdPlanDefMapper extends BaseMapper<McdPlanDef> {

    IPage<McdPlanDefInfo> queryPlanDefList(Page page,@Param("param") RequestPlanQuery planQuery);
}
