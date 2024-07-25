package com.asiainfo.biapp.pec.element.jx.service.impl;

import com.asiainfo.biapp.pec.element.jx.entity.McdDimChannelDesc;
import com.asiainfo.biapp.pec.element.jx.mapper.McdDimChannelDescMapper;
import com.asiainfo.biapp.pec.element.jx.service.McdDimChannelDescService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 渠道说明表 服务实现类
 * </p>
 *
 * @since 2022-10-25
 */
@Service
public class McdDimChannelDescServiceImpl extends ServiceImpl<McdDimChannelDescMapper, McdDimChannelDesc> implements McdDimChannelDescService {

    /**
     * mapper
     */
    @Autowired
    private McdDimChannelDescMapper mcdDimChannelDescMapper;

    @Override
    public McdDimChannelDesc queryById(String channelId) {
        LambdaQueryWrapper<McdDimChannelDesc> lqw = new LambdaQueryWrapper();
        lqw.eq(channelId!= null,McdDimChannelDesc::getChannelId,channelId);
        return mcdDimChannelDescMapper.selectOne(lqw);
    }
}
