package com.asiainfo.biapp.pec.plan.jx.hisdata.service;

import com.asiainfo.biapp.pec.plan.jx.hisdata.model.McdCampExtMap;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 7.0扩展字段与4.0字段映射关系 服务类
 * </p>
 *
 * @author mamp
 * @since 2023-05-22
 */
public interface McdCampExtMapService extends IService<McdCampExtMap> {
    /**
     * 删除预演日志
     *
     * @param campsegId
     */
    void deleteCampPreviewLog(String campsegId);

    /**
     * 删除预演任务
     *
     * @param campsegId
     */
    void deleteCampPreview(String campsegId);

    /**
     * 删除审批实例
     *
     * @param campsegId
     */
    void deleteApproveInstance(String campsegId);

    /**
     * 删除审批记录
     *
     * @param campsegId
     */
    void deleteApproveRecord(String campsegId);
}
