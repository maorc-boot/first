package com.asiainfo.biapp.pec.approve.jx.service.impl;


import com.asiainfo.biapp.pec.approve.jx.dao.McdCampChannelListDao;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampChannelList;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdCampChannelListService;


import com.asiainfo.biapp.pec.approve.jx.vo.McdCampChannelDetailVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


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
public class McdCampChannelListServiceImpl extends ServiceImpl<McdCampChannelListDao, McdCampChannelList> implements IMcdCampChannelListService {

    @Resource
    private McdCampChannelListDao mcdCampChannelListDao;
    @Override
    public List<McdCampChannelDetailVo> getDeliveryChannel(String campsegId) {
        return mcdCampChannelListDao.getDeliveryChannels(campsegId);
    }
}
