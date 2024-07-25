package com.asiainfo.biapp.pec.approve.jx.dao;

import com.asiainfo.biapp.client.pec.plan.model.McdPlanDef;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampDef;
import com.asiainfo.biapp.pec.approve.jx.model.McdCustgroupDef;
import com.asiainfo.biapp.pec.approve.jx.vo.ApprovalProcessCampDef;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface McdCampDefDao extends BaseMapper<McdCampDef> {

    List<Map<String,Object>> getCampsegInfos(@Param("cycleType") int cycleType,@Param("cycleStartDate") String cycleStartDate ,@Param("cycleEndDate") String cycleEndDate,@Param("cycleDays") int cycleDays);

    /**
     * 根据指标查询效果数据
     * @param rate
     * @return
     */
    List<Map<String, Object>> queryWarnInfoByTarget(@Param("rate")double rate,@Param("compType")String compType,@Param("campsegPId")String campsegPId  );

    /**
     * 根据素材id判断该素材是否有非草稿、预演完成状态的活动使用
     *
     * @param materialId 素材id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> chkHasCampUsedMaterial (@Param("materialId") String materialId);

    /**
     * 根据rootId查询渠道id
     *
     * @param campsegRootId campseg根id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    List<Map<String, Object>> qryChannelIdByCampsegRootId (@Param("campsegRootId")String campsegRootId );

    /**
     * 查询活动扩展信息
     * @param campsegId
     * @return
     */
    List<Map<String, Object>> queryCampExtByCampId (@Param("campsegId")String campsegId );

    /**
     * 根据活动ID查询产品详情
     * @param campsegId 子活动ID
     * @return
     */
    List<McdPlanDef> queryPlanByCampsegId(@Param("campsegId")String campsegId );

    /**
     * 根据活动ID查询客户群详情
     *
     * @param campsegId 子活动ID
     * @return
     */
    List<McdCustgroupDef> queryCustByCampsegId(@Param("campsegId") String campsegId);


    public List<Map<String,Object>> getMtlChannelDefs(String campsegId);

    /**
     * 查询第一波次的活动信息
     *
     * @param campsegRootId 活动根id
     * @return {@link List}<{@link ApprovalProcessCampDef}>
     */
    List<ApprovalProcessCampDef> queryCampInfoByRootId(@Param("campsegRootId") String campsegRootId);

    /**
     * 根据根id查询客群来源值(多波次只查询第一波次)
     *
     * @param campsegRootId 活动根id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> queryCustgroupSourceByRootId(@Param("campsegRootId") String campsegRootId);

    /**
     * 根据主题id查询所有的活动信息
     *
     * @param themeId 主题id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryAllCampByThemeId(@Param("themeId") String themeId);
}
