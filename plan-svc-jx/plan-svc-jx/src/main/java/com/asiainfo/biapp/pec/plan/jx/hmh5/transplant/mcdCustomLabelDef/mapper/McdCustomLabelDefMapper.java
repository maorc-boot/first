package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.mapper;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam.SelectSelfDefinedLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.model.McdCustomLabelDef;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo.SelfDefinedLabelResultConfInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo.SelfDefinedLabelResultInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 标签定义表 Mapper 接口
 * </p>
 *
 * @author chenlin
 * @since 2023-06-26
 */
public interface McdCustomLabelDefMapper extends BaseMapper<McdCustomLabelDef> {

    /**
     * 查询自定义标签列表信息
     *
     * @param objectPage 分页对象
     * @param param 查询入参
     * @param cityId 当前登录用户地市信息
     * @return {@link Page}<{@link SelfDefinedLabelResultConfInfo}>
     */
    Page<SelfDefinedLabelResultConfInfo> selectSelfDefinedLabelConfDetail(Page<Object> objectPage, @Param("param") SelectSelfDefinedLabelParam param, @Param("cityId") String cityId);

    Page<SelfDefinedLabelResultInfo> selectSelfDefinedLabel(Page<Object> objectPage, String cityId, String customLabelName);

    List<SelfDefinedLabelResultInfo> selectSelfDefinedLabelConf(@Param("cityId") String cityId);

    List<SelfDefinedLabelResultInfo> selectSelfDefinedLabelByIds(@Param("cityId") String cityId, @Param("labelIds") Collection<String> labelIds);

    /**
     * 1. 清除模块配置关联表
     * 2. 删除标签定义表数据
     * 3. 删除同步记录表
     *
     * @param labelId 标签id
     */
    void deleteByLabelId(@Param("labelId") String labelId);

    /**
     * 清除自定义标签列
     *
     * @param labelId 标签id
     * @param cityId  地市id
     */
    void deleteLabelCol(@Param("labelId") String labelId, @Param("cityId") String cityId);
}
