package com.asiainfo.biapp.pec.approve.jx.service.impl;

import com.asiainfo.biapp.pec.approve.jx.dao.McdCampChannelExtDao;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampChannelExt;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdCampChannelExtService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 策略渠道运营位扩展属性表 服务实现类
 * </p>
 *
 * @author imcd
 * @since 2021-11-11
 */
@Service
public class McdCampChannelExtServiceImpl extends ServiceImpl<McdCampChannelExtDao, McdCampChannelExt> implements IMcdCampChannelExtService {

    @Resource
    private McdCampChannelExtDao campChannelExtDao;

    @Override
    public void deleteByCampsegPid(String campsegPid) {
        campChannelExtDao.deleteByCampsegPid(campsegPid);
    }

    @Override
    public List<McdCampChannelExt> qryByCampsegPid(String campsegPid) {
        return campChannelExtDao.qryByCampsegPid(campsegPid);
    }

    @Override
    public Boolean validMaterial(String channelId, String adivId, String adivUrl, String campsegId) {
        /* 查询当前素材是否已使用 */
        List<McdCampChannelExt> list = campChannelExtDao.validMaterial(channelId, adivId, adivUrl, campsegId);
        if(CollectionUtils.isEmpty(list)){
            return true;
        }
        return false;
    }

    @Override
    public void deleteByCampsegRootId(String campsegRootId) {
        campChannelExtDao.deleteByCampsegRootId(campsegRootId);
    }

    @Override
    public List<McdCampChannelExt> qryByCampsegRootId(String campsegRootId) {
        return campChannelExtDao.qryByCampsegRootId(campsegRootId);
    }

}
