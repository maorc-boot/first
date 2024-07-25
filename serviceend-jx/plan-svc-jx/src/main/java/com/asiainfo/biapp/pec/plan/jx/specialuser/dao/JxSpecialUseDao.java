package com.asiainfo.biapp.pec.plan.jx.specialuser.dao;

import com.asiainfo.biapp.pec.plan.jx.specialuser.model.JxMcdSpecialUse;
import com.asiainfo.biapp.pec.plan.vo.SpecialUser;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 特殊客户群表 Mapper 接口
 * </p>
 *
 * @author imcd
 * @since 2021-12-06
 */
@Mapper
public interface JxSpecialUseDao extends BaseMapper<JxMcdSpecialUse> {

    /**
     * 查询特殊客户群列表 分页
     * @param pager
     * @param query
     * @return
     */
    IPage<SpecialUser> pageSpecialUse(Page<?> pager, @Param("query") SpecialUseQuery query);

    Long pageSpecialUseCount(@Param("query") SpecialUseQuery query);

    /**
     * 批量插入
     * @param specialUsesList
     */
    void saveBatchSpecialUser(@Param("list") List<JxMcdSpecialUse> specialUsesList);
}
