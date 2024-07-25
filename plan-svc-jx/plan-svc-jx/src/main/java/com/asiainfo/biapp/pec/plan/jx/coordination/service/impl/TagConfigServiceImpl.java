package com.asiainfo.biapp.pec.plan.jx.coordination.service.impl;


import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.jx.coordination.dao.TagConfigMapper;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCoordinateAccessLabel;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCoordinateCustType;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCoordinateTag;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCoordinateTagModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.TagConfigReq;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.TagConfigService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagConfigServiceImpl extends ServiceImpl<TagConfigMapper,McdCoordinateTag> implements TagConfigService {

    @Resource
    private TagConfigMapper tagConfigMapper;

    /**
     * 分页查询标签配置
     *
     * @param query 查询
     * @return {@link Page}<{@link McdCoordinateTagModel}>
     */
    @Override
    public Page<McdCoordinateTagModel> queryTag(TagConfigReq req) {
        Page<McdCoordinateTagModel> page = new Page<>(req.getCurrent(),req.getSize());
        return tagConfigMapper.queryTag(page,req);

    }

    /**
     * 查询标签系列类型
     *
     * @return {@link List}<{@link McdCoordinateCustType}>
     */
    @Override
    public List<McdCoordinateCustType> queryCustType() {
        return tagConfigMapper.queryCustType();
    }

    /**
     *查询标签配置所有已配置优先级的标签(不分页)
     *
     * @param req
     * @return {@link List}<{@link McdCoordinateTagModel}>
     */
    @Override
    public List<McdCoordinateTagModel> queryAllTag() {
        return tagConfigMapper.queryAllTag();
    }

    @Override
    public List<McdCoordinateTagModel> queryAccessLabel(TagConfigReq req) {
        return tagConfigMapper.queryAccessLabel(req);
    }
}
