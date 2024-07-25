package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.impl;

import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dao.IMcdPrismSceneCampDao;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.McdPrismSceneCamp;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.service.IMcdPrismSceneCampService;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.QueryCampByThemeIdOrTepIdReqVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * description: 智推棱镜场景与策略关系service实现
 *
 * @author: lvchaochao
 * @date: 2024/4/17
 */
@Service
public class McdPrismSceneCampServiceImpl extends ServiceImpl<IMcdPrismSceneCampDao, McdPrismSceneCamp> implements IMcdPrismSceneCampService {

    @Autowired
    private IMcdPrismSceneCampDao mcdPrismSceneCampDao;

    /**
     * 根据主题id批量查询被引用次数(被引用即创建了活动)
     *
     * @param themeIdList 主题id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public List<Map<String, Object>> queryTemplateRefCount(List<String> themeIdList) {
        return mcdPrismSceneCampDao.queryTemplateRefCount(themeIdList);
    }

    /**
     * 根据主题id查询所有的活动信息
     *
     * @param themeId 主题id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> queryAllCampByThemeId(String themeId) {
        return mcdPrismSceneCampDao.queryAllCampByThemeId(themeId);
    }

    /**
     * 根据主题id查询所有的活动信息以及渠道名称
     *
     * @param reqVO 入参对象
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> queryAllCampInfoByThemeId(QueryCampByThemeIdOrTepIdReqVO reqVO) {
        return mcdPrismSceneCampDao.queryAllCampInfoByThemeId(reqVO);
    }

    /**
     * 根据主题或者模版查询下面的活动
     *
     * @param reqVO 入参对象
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    @Override
    public List<Map<String, Object>> queryCampInfoByThemeIdOrTemplateId(QueryCampByThemeIdOrTepIdReqVO reqVO) {
        return mcdPrismSceneCampDao.queryCampInfoByThemeIdOrTemplateId(reqVO);
    }
}
