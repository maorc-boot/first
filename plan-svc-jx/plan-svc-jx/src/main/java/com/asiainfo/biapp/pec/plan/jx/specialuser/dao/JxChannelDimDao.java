package com.asiainfo.biapp.pec.plan.jx.specialuser.dao;

import com.asiainfo.biapp.pec.plan.jx.specialuser.model.McdDimChannelJx;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
public interface JxChannelDimDao extends BaseMapper<McdDimChannelJx> {
    List<McdDimChannelJx> getAllMcdDimChannel();
}
