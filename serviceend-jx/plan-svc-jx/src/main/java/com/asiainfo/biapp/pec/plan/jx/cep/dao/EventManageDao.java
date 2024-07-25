package com.asiainfo.biapp.pec.plan.jx.cep.dao;

import com.asiainfo.biapp.pec.plan.jx.cep.model.CepEventVo;
import com.asiainfo.biapp.pec.plan.jx.cep.model.TreeNodeDTO;
import com.asiainfo.biapp.pec.plan.jx.cep.req.SearchEventActionQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mamp
 * @date 2022/4/29
 */
public interface EventManageDao extends BaseMapper {
    /**
     * 查询事件分类
     *
     * @return
     */
    List<TreeNodeDTO> queryCepEventTypeList(@Param("keyWords") String keyWords);

    /**
     * 查询事件
     *
     * @return
     */
    IPage<CepEventVo> queryEvents(Page page, @Param("param") SearchEventActionQuery form);

    /**
     * 根据创建人ID查询事件
     *
     * @return
     */
    IPage<CepEventVo> queryEventsByUserId(Page page, @Param("param") SearchEventActionQuery query);
}
