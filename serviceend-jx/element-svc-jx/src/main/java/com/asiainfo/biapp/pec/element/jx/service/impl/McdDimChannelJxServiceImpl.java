package com.asiainfo.biapp.pec.element.jx.service.impl;


import com.asiainfo.biapp.pec.element.dto.request.DimChannelPageListRequest;
import com.asiainfo.biapp.pec.element.jx.entity.McdDimChannelJx;
import com.asiainfo.biapp.pec.element.jx.mapper.McdDimChannelJxDao;
import com.asiainfo.biapp.pec.element.jx.query.DimChannelPageListRequestJx;
import com.asiainfo.biapp.pec.element.jx.service.IMcdDimChannelJxService;
import com.asiainfo.biapp.pec.element.jx.vo.DimChannelListRequestJx;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 渠道表 服务实现类
 * </p>
 *
 * @author ranpf
 * @since 2022-12-13
 */
@Service
public class McdDimChannelJxServiceImpl extends ServiceImpl<McdDimChannelJxDao, McdDimChannelJx > implements IMcdDimChannelJxService {
    
    @Resource
    private McdDimChannelJxDao mcdDimChannelJxDao;

    
    /**
     * 新增或更新渠道信息
     *
     * @param mcdDimChannel 入参对象
     * @return true/false
     */
    @Override
    public boolean saveOrUpdateDimChannel(McdDimChannelJx  mcdDimChannel) {
        int re = 0;
        if (StringUtils.isBlank(mcdDimChannel.getChannelCode())){
            re = mcdDimChannelJxDao.saveChannelInfo(mcdDimChannel);
        } else {
            re = mcdDimChannelJxDao.updateChannel(mcdDimChannel);
        }
        return re == 1;
    }

    /**
     * 渠道(或根据条件)分页查询
     *
     * @param dimChannelPageListRequest 入参对象
     * @return 渠道分页结果集
     */
    @Override
    public IPage<McdDimChannelJx> pagelistDimChannel(DimChannelPageListRequestJx dimChannelPageListRequest) {
        Page<McdDimChannelJx> page =
                new Page<>(dimChannelPageListRequest.getCurrent(), dimChannelPageListRequest.getSize());
        IPage<McdDimChannelJx> dimChannelPage = page(page, Wrappers.<McdDimChannelJx>query().lambda()
                .like(StringUtils.isNotBlank(dimChannelPageListRequest.getKeywords()),
                        McdDimChannelJx::getChannelName,dimChannelPageListRequest.getKeywords())
                .eq(dimChannelPageListRequest.getOnlineStatus() != null,
                        McdDimChannelJx::getOnlineStatus,dimChannelPageListRequest.getOnlineStatus())
                .eq(dimChannelPageListRequest.getChannelTypeZq() != null ,McdDimChannelJx::getChannelTypeZq,dimChannelPageListRequest.getChannelTypeZq())
                .eq(dimChannelPageListRequest.getChannelAffiliation() != null,
                        McdDimChannelJx::getChannelAffiliation,dimChannelPageListRequest.getChannelAffiliation())
                .orderByDesc(McdDimChannelJx::getCreateTime));
        return dimChannelPage;
    }

    /**
     * 根据用户ID查询渠道权限
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> listChannelByUserId(String userId) {
        return mcdDimChannelJxDao.listChannelByUserId(userId);
    }

    @Override
    public List<McdDimChannelJx> getDimChannelList(DimChannelListRequestJx dimChannelListRequest) {
        LambdaQueryWrapper<McdDimChannelJx> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dimChannelListRequest.getOnlineStatus() != null,
                        McdDimChannelJx::getOnlineStatus,dimChannelListRequest.getOnlineStatus())
                .eq(null != dimChannelListRequest.getType(), McdDimChannelJx::getChannelType, dimChannelListRequest.getType())
                .eq(null != dimChannelListRequest.getChannelId(), McdDimChannelJx::getChannelId, dimChannelListRequest.getChannelId())
                .orderByDesc(McdDimChannelJx::getCreateTime);
        return baseMapper.selectList(wrapper);
    }


}
