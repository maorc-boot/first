package com.asiainfo.biapp.pec.plan.jx.coordination.dao;


import com.asiainfo.biapp.pec.plan.jx.coordination.model.*;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.TagConfigReq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Mapper
public interface TagConfigMapper extends BaseMapper<McdCoordinateTag> {


    /**
     * 分页查询标签配置
     *
     * @param query 查询
     * @return {@link Page}<{@link McdCoordinateTagModel}>
     */
    Page<McdCoordinateTagModel> queryTag(@Param("page") Page<McdCoordinateTagModel> page, @Param("req")TagConfigReq req);

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

    List<McdCoordinateTagModel> queryAccessLabel(@Param("req")TagConfigReq req);

}
