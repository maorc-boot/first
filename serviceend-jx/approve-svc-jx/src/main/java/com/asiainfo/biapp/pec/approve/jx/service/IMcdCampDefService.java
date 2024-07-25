package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.client.pec.plan.model.McdPlanDef;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampDef;
import com.asiainfo.biapp.pec.approve.jx.model.McdCustgroupDef;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 策略定义表 服务类
 * </p>
 *
 * @author imcd
 * @since 2021-11-11
 */
public interface IMcdCampDefService extends IService<McdCampDef> {

    /**
     * 获取策略名称
     *
     * @param campsegId
     * @return
     */
    String getNameByPid(String campsegId);



    /**
     * 用根策略id，查询策略列表
     * @param campsegRootId
     * @return
     */
    List<McdCampDef> listByCampsegRootId(String campsegRootId);

    /**
     * 用根策略id，查询子策略列表
     * @param campsegRootId
     * @return
     */
    List<McdCampDef> listChildCampByCampsegRootId(String campsegRootId);



    /**
     * 删除根策略
     * @param campsegRootId
     */
    void delByRootId(String campsegRootId);


    List<Map<String,Object>> getCampsegInfos(int cycleType, String cycleStartDate , String cycleEndDate, int cycleDays);


    /**
     * 根据指标查询效果数据
     * @param rate
     * @return
     */
    List<Map<String, Object>> queryWarnInfoByTarget(double rate,String sign,String campsegPId  );

    /**
     * 根据素材id判断该素材是否有非草稿、预演完成状态的活动使用
     *
     * @param materialId 素材id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> chkHasCampUsedMaterial (String materialId);

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
     * 根据主题id查询所有的活动信息
     *
     * @param themeId 主题id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryAllCampByThemeId(String themeId);

}
