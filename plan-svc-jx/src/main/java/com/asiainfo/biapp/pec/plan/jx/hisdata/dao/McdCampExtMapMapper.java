package com.asiainfo.biapp.pec.plan.jx.hisdata.dao;

import com.asiainfo.biapp.pec.plan.jx.hisdata.model.McdCampExtMap;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 7.0扩展字段与4.0字段映射关系 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2023-05-22
 */
public interface McdCampExtMapMapper extends BaseMapper<McdCampExtMap> {
    /**
     * 删除预演日志
     *
     * @param campsegId
     */
    void deleteCampPreviewLog(@Param("campsegId") String campsegId);

    /**
     * 删除预演任务
     *
     * @param campsegId
     */
    void deleteCampPreview(@Param("campsegId") String campsegId);

    /**
     * 删除审批实例
     *
     * @param campsegId
     */
    void deleteApproveInstance(@Param("campsegRootId") String campsegId);

    /**
     * 删除审批记录
     *
     * @param campsegId
     */
    void deleteApproveRecord(@Param("campsegRootId") String campsegId);

    List<Map<String, Object>> queryReadyCampList(@Param("stat") int stat);

    Map<String, Object> queryCampTaskInfo(@Param("campsegId") String campsegId,@Param("channelId") String channelId);


}
