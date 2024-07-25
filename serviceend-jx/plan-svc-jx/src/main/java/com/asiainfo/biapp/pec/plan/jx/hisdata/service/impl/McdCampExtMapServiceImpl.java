package com.asiainfo.biapp.pec.plan.jx.hisdata.service.impl;

import com.asiainfo.biapp.pec.plan.jx.hisdata.dao.McdCampExtMapMapper;
import com.asiainfo.biapp.pec.plan.jx.hisdata.model.McdCampExtMap;
import com.asiainfo.biapp.pec.plan.jx.hisdata.service.McdCampExtMapService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 7.0扩展字段与4.0字段映射关系 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2023-05-22
 */
@Service
public class McdCampExtMapServiceImpl extends ServiceImpl<McdCampExtMapMapper, McdCampExtMap> implements McdCampExtMapService {

    @Resource
    private McdCampExtMapMapper mcdCampExtMapMapper;
    /**
     * 删除预演日志
     *
     * @param campsegId
     */
    @Override
    public void deleteCampPreviewLog(String campsegId) {
        mcdCampExtMapMapper.deleteCampPreviewLog(campsegId);
    }

    /**
     * 删除预演任务
     *
     * @param campsegId
     */
    @Override
    public void deleteCampPreview(String campsegId) {
        mcdCampExtMapMapper.deleteCampPreview(campsegId);
    }

    /**
     * 删除审批实例
     *
     * @param campsegId
     */
    @Override
    public void deleteApproveInstance(String campsegId) {
        mcdCampExtMapMapper.deleteApproveInstance(campsegId);
    }

    /**
     * 删除审批记录
     *
     * @param campsegId
     */
    @Override
    public void deleteApproveRecord(String campsegId) {
        mcdCampExtMapMapper.deleteApproveRecord(campsegId);
    }
}
