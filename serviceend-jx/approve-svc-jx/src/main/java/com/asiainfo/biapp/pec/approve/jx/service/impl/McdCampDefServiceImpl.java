package com.asiainfo.biapp.pec.approve.jx.service.impl;

import com.asiainfo.biapp.client.pec.plan.model.McdPlanDef;
import com.asiainfo.biapp.pec.approve.jx.dao.McdCampDefDao;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampDef;
import com.asiainfo.biapp.pec.approve.jx.model.McdCustgroupDef;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdCampDefService;
import com.asiainfo.biapp.pec.core.common.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 策略定义表 服务实现类
 * </p>
 *
 * @author imcd
 * @since 2021-11-11
 */
@Service
public class McdCampDefServiceImpl extends ServiceImpl<McdCampDefDao, McdCampDef> implements IMcdCampDefService {

    @Resource
    private McdCampDefDao campDefDao;



    @Override
    public String getNameByPid(String campsegId) {
        final McdCampDef campDef = campDefDao.selectById(campsegId);
        Assert.notNull(campDef, "传入的id不正确，请重试！");
        return campDef.getCampsegName();
    }



    @Override
    public List<McdCampDef> listByCampsegRootId(String campsegRootId) {
        final LambdaQueryWrapper<McdCampDef> qry = Wrappers.lambdaQuery();
        qry.eq(McdCampDef::getCampsegId, campsegRootId).or().eq(McdCampDef::getCampsegRootId, campsegRootId);
        qry.orderByAsc(McdCampDef::getCampsegRootId);
        return campDefDao.selectList(qry);
    }

    @Override
    public List<McdCampDef> listChildCampByCampsegRootId(String campsegRootId) {
        final LambdaQueryWrapper<McdCampDef> qry = Wrappers.lambdaQuery();
        qry.eq(McdCampDef::getCampsegRootId, campsegRootId);
        return campDefDao.selectList(qry);
    }


    @Override
    public void delByRootId(String campsegRootId) {
        final LambdaQueryWrapper<McdCampDef> del = Wrappers.lambdaQuery();
        del.eq(McdCampDef::getCampsegId, campsegRootId).or().eq(McdCampDef::getCampsegRootId, campsegRootId);
        campDefDao.delete(del);
    }

    @Override
    public List<Map<String, Object>> getCampsegInfos(int cycleType, String cycleStartDate, String cycleEndDate, int cycleDays) {
        return campDefDao.getCampsegInfos(cycleType,cycleStartDate,cycleEndDate,cycleDays);
    }


    @Override
    public List<Map<String, Object>> queryWarnInfoByTarget(double rate, String sign, String campsegPId ) {
        return campDefDao.queryWarnInfoByTarget(rate,sign,campsegPId);
    }

    /**
     * 根据素材id判断该素材是否有非草稿、预演完成状态的活动使用
     *
     * @param materialId 素材id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> chkHasCampUsedMaterial(String materialId) {
        return campDefDao.chkHasCampUsedMaterial(materialId);
    }

    /**
     * 查询活动扩展信息
     *
     * @param campsegId
     * @return
     */
    @Override
    public List<Map<String, Object>> queryCampExtByCampId(String campsegId) {
        return campDefDao.queryCampExtByCampId(campsegId);
    }

    /**
     * 根据活动ID查询产品详情
     *
     * @param campsegId 子活动ID
     * @return
     */
    @Override
    public List<McdPlanDef> queryPlanByCampsegId(String campsegId) {
        return campDefDao.queryPlanByCampsegId(campsegId);
    }

    /**
     * 根据活动ID查询客户群详情
     *
     * @param campsegId 子活动ID
     * @return
     */
    @Override
    public List<McdCustgroupDef> queryCustByCampsegId(String campsegId) {
        return campDefDao.queryCustByCampsegId(campsegId);
    }


    @Override
    public List<Map<String, Object>> getMtlChannelDefs(String campsegId) {
        return campDefDao.getMtlChannelDefs(campsegId);
    }

    /**
     * 根据主题id查询所有的活动信息
     *
     * @param themeId 主题id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> queryAllCampByThemeId(String themeId) {
        return campDefDao.queryAllCampByThemeId(themeId);
    }
}
