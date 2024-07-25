package com.asiainfo.biapp.pec.approve.jx.dao;

import com.asiainfo.biapp.pec.approve.jx.model.McdCampChannelList;


import com.asiainfo.biapp.pec.approve.jx.vo.McdCampChannelDetailVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 策略执行基础属性表 Mapper 接口
 * </p>
 *
 * @author imcd
 * @since 2021-11-12
 */
public interface McdCampChannelListDao extends BaseMapper<McdCampChannelList> {

    List<McdCampChannelDetailVo> getDeliveryChannels(String campsegId);
}
