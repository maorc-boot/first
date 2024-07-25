package com.asiainfo.biapp.pec.plan.jx.coordination.service;


import com.asiainfo.biapp.pec.plan.jx.coordination.model.*;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.TagConfigReq;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TagConfigService extends IService<McdCoordinateTag> {

    /**
     * 分页查询标签配置
     *
     * @param query 查询
     * @return {@link Page}<{@link McdCoordinateTagModel}>
     */
    Page<McdCoordinateTagModel> queryTag(TagConfigReq req);

    /**
     * 查询标签系列类型
     *
     * @return {@link List}<{@link McdCoordinateCustType}>
     */
    List<McdCoordinateCustType> queryCustType();

    /**
     *查询标签配置所有已配置优先级的标签(不分页)
     *
     * @param req
     * @return {@link List}<{@link McdCoordinateTagModel}>
     */
    List<McdCoordinateTagModel> queryAllTag();

    List<McdCoordinateTagModel> queryAccessLabel(TagConfigReq req);
}
