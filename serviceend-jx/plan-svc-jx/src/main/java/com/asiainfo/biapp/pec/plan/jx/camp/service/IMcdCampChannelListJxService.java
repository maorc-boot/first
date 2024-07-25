package com.asiainfo.biapp.pec.plan.jx.camp.service;


import com.asiainfo.biapp.pec.plan.jx.camp.model.CampPriorityOrderJx;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampChannelListJx;
import com.asiainfo.biapp.pec.plan.model.McdCampChannelList;
import com.asiainfo.biapp.pec.plan.vo.*;

import com.asiainfo.biapp.pec.plan.vo.req.CampPriorityOrderQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 * 策略执行基础属性表 服务类
 * </p>
 *
 * @author imcd
 * @since 2021-11-16
 */
public interface IMcdCampChannelListJxService extends IService<McdCampChannelListJx> {

    /**
     * 优先级渠道策略个数
     *
     * @return
     */
    List<PriorityChannel> queryPriorityChannel(String channelType);

    /**
     * 优先级列表
     *
     * @param query
     * @return
     */
    IPage<CampPriorityOrderJx> pagePriorityOrderJx(CampPriorityOrderQuery query);
}
