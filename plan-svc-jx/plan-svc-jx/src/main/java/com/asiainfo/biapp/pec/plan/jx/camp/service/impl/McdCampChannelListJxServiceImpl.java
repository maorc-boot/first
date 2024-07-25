package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;



import com.asiainfo.biapp.pec.plan.jx.camp.dao.McdCampChannelListJxDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CampPriorityOrderJx;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampChannelListJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampChannelListJxService;
import com.asiainfo.biapp.pec.plan.model.McdCampChannelList;

import com.asiainfo.biapp.pec.plan.vo.*;

import com.asiainfo.biapp.pec.plan.vo.req.CampPriorityOrderQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * <p>
 * 策略执行基础属性表 服务实现类
 * </p>
 *
 * @author imcd
 * @since 2021-11-16
 */
@Slf4j
@Service
public class McdCampChannelListJxServiceImpl extends ServiceImpl<McdCampChannelListJxDao, McdCampChannelListJx> implements IMcdCampChannelListJxService {

    @Resource
    private McdCampChannelListJxDao mcdCampChannelListJxDao;


    @Override
    public List<PriorityChannel> queryPriorityChannel(String  channelType) {
        final List<PriorityChannel> priorityChannels = mcdCampChannelListJxDao.queryPriorityChannel(channelType);
        //合并总计
        priorityChannels.forEach(priorityChannel -> {
            AtomicInteger campNum = new AtomicInteger();
            priorityChannel.getPriorityAdivs().forEach(priorityAdiv -> {
                final Integer campNum1 = priorityAdiv.getCampNum();
                if (null == campNum1) {
                    priorityAdiv.setCampNum(0);
                }
                campNum.addAndGet(priorityAdiv.getCampNum());
            });
            priorityChannel.setCampNum(campNum.get());
        });
        return priorityChannels;
    }

    @Override
    public IPage<CampPriorityOrderJx> pagePriorityOrderJx(CampPriorityOrderQuery query) {
        Page pager = new Page(query.getCurrent(), query.getSize());
        return mcdCampChannelListJxDao.pagePriorityOrderJx(pager, query.getChannelId(), query.getAdivId(), query.getKeyWords());
    }
}
