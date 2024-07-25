package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.model.McdCampChannelList;

import com.asiainfo.biapp.pec.approve.jx.vo.McdCampChannelDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 策略执行基础属性表 服务类
 * </p>
 *
 * @author imcd
 * @since 2021-11-16
 */
public interface IMcdCampChannelListService extends IService<McdCampChannelList> {

    List<McdCampChannelDetailVo> getDeliveryChannel(String campsegId);
}
