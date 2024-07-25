package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service;

import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.McdPrismSceneCamp;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.QueryCampByThemeIdOrTepIdReqVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * description: 智推棱镜场景与策略关系service
 *
 * @author: lvchaochao
 * @date: 2024/4/17
 */
public interface IMcdPrismSceneCampService extends IService<McdPrismSceneCamp> {

    /**
     * 根据主题id批量查询被引用次数(被引用即创建了活动)
     *
     * @param themeIdList 主题id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    List<Map<String, Object>> queryTemplateRefCount(List<String> themeIdList);

    /**
     * 根据主题id查询所有的活动信息
     *
     * @param themeId 主题id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryAllCampByThemeId(String themeId);

    /**
     * 根据主题id查询所有的活动信息以及渠道名称
     *
     * @param reqVO 入参对象
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryAllCampInfoByThemeId(QueryCampByThemeIdOrTepIdReqVO reqVO);

    /**
     * 根据主题或者模版查询下面的活动
     *
     * @param reqVO 入参对象
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryCampInfoByThemeIdOrTemplateId(QueryCampByThemeIdOrTepIdReqVO reqVO);
}
