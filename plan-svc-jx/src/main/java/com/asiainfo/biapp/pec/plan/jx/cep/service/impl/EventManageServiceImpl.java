package com.asiainfo.biapp.pec.plan.jx.cep.service.impl;

import com.asiainfo.biapp.pec.plan.jx.cep.dao.EventManageDao;
import com.asiainfo.biapp.pec.plan.jx.cep.model.CepEventVo;
import com.asiainfo.biapp.pec.plan.jx.cep.model.TreeNodeDTO;
import com.asiainfo.biapp.pec.plan.jx.cep.req.SearchEventActionQuery;
import com.asiainfo.biapp.pec.plan.jx.cep.service.IEventManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2022/5/5
 */
@Service
public class EventManageServiceImpl implements IEventManageService {

    @Resource
    private EventManageDao eventManageDao;

    @Override
    public List<TreeNodeDTO> queryCepEventTypeList(String keyWords) {
        List<TreeNodeDTO> list = eventManageDao.queryCepEventTypeList(keyWords);
        List<TreeNodeDTO> rootNodes = new ArrayList<>();
        Map<String, TreeNodeDTO> map = new HashMap<>();
        for (TreeNodeDTO node : list) {
            map.put(node.getId(), node);
            String parentId = node.getParentId();
            // 判断是否有父节点 (没有父节点本身就是个父菜单)
            if (parentId.equals("0")) {
                rootNodes.add(node);
                // 找出不是父级菜单的且集合中包括其父菜单ID
            } else if (map.containsKey(parentId)) {
                map.get(parentId).getChildren().add(node);
            }
        }
        return rootNodes;
    }

    @Override
    public IPage<CepEventVo> queryEvents(SearchEventActionQuery query) {

        Page pager = new Page(query.getCurrent(), query.getSize());
        return eventManageDao.queryEvents(pager, query);
    }

    /**
     * 根据创建人ID查询事件
     *
     * @param query
     * @return
     */
    @Override
    public IPage<CepEventVo> queryEventsByUserId(SearchEventActionQuery query) {
        Page pager = new Page(query.getCurrent(), query.getSize());
        return eventManageDao.queryEventsByUserId(pager, query);
    }
}
