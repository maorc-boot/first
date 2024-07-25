package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dao;

import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.McdPrismChannelDefaultConfig;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.McdPrismPlanTagRelDegreeVO;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.PrismChannelConfigVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * description: 智推棱镜-AI流程dao
 *
 * @author: lvchaochao
 * @date: 2024/5/25
 */
public interface IIntellPushPrismAIDao {

    /**
     * 根据渠道id查询渠道默认配置信息
     *
     * @return {@link List<McdPrismChannelDefaultConfig>}
     */
    List<McdPrismChannelDefaultConfig> getChannelConfigByChannelId();

    /**
     * 获取该产品最近5个活动的成功用户数和转化率
     *
     * @param planId 产品id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getCampEvalTop5ByPlan(@Param("planId") String planId);

    /**
     * 根据产品id查询所有的标签id
     *
     * @param planId 产品id
     * @return {@link List}<{@link String}>
     */
    List<Map<String, Object>> getPlanTagIdsByPlanId(@Param("planId") String planId);

    /**
     * 根据产品id、标签id查询标签值
     *
     * @param tagId 产品id
     * @param planId 产品id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getTagValueByTagIdAndPlanId(@Param("tagId") String tagId, @Param("planId") String planId);

    List<Map<String, Object>> getTagValueByColumnIdAndPlanId(@Param("columnNum") String columnNum, @Param("planId") String planId);

    Map<String, Object> getCorrelationOnOriginTag(@Param("planId") String planId, @Param("columnNum") String columnNum, @Param("remarks") Set<String> collect);

    Map<String, Object> getCorrelationOnNotEq(@Param("planId") String planId, @Param("columnNum") String columnNum, @Param("remarks") Set<String> collect);

    /**
     * 根据产品id、标签id查询关联度
     *
     * @param tagId 产品id
     * @param planId 产品id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> getCorrelationByTagIdAndPlanId(@Param("tagId") String tagId, @Param("planId") String planId);

    /**
     * 查询所有产品id、标签id
     *
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getAllTagIdPlanId();

    /**
     * 根据产品id、标签id查询并计算关联度
     *
     * @param tagId 产品id
     * @param planId 产品id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> calCorrelation(@Param("tagId") String tagId, @Param("planId") String planId);

    /**
     * 保存关联度数据
     *
     * @param degreeVOS degreeVOS
     */
    void saveBatchDegreeInfo2DB(@Param("degreeVOS") List<McdPrismPlanTagRelDegreeVO> degreeVOS);

    /**
     * 删除该产品下原有历史数据
     *
     * @param planIds 产品id集合
     */
    void deleteBatchDegreeInfo(@Param("planIds") Set<String> planIds);

    /**
     * 根据产品id查询近30天的活动效果数据
     *
     * @param planIds 产品id集合
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> getThirtyDaysCampEvalByPlandId(@Param("planIds") Set<String> planIds);

    /**
     * 根据活动id、渠道id查询扩展表数据
     *
     * @param campsegIdList 活动子id集合
     * @return {@link List}<{@link PrismChannelConfigVO}>
     */
    List<PrismChannelConfigVO> getExtByCampsegRootIdAndChannelId(@Param("campsegIdList") Set<String> campsegIdList);
}
