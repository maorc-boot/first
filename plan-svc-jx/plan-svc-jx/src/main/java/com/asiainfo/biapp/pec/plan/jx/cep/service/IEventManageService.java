package com.asiainfo.biapp.pec.plan.jx.cep.service;

import com.asiainfo.biapp.pec.plan.jx.cep.model.CepEventVo;
import com.asiainfo.biapp.pec.plan.jx.cep.model.TreeNodeDTO;
import com.asiainfo.biapp.pec.plan.jx.cep.req.SearchEventActionQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author mamp
 * @date 2022/4/29
 */
public interface IEventManageService {
    /**
     * 查询事件分类
     *
     * @return
     */
    List<TreeNodeDTO> queryCepEventTypeList(String keyWords);

    /**
     * 查询所有可用事件
     *
     * @return
     */
    IPage<CepEventVo> queryEvents(SearchEventActionQuery query);

    /**
     * 根据创建人ID查询事件
     *
     * @return
     */
    IPage<CepEventVo> queryEventsByUserId(SearchEventActionQuery query);
}
