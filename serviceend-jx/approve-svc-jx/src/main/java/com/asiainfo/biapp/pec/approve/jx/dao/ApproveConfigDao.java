package com.asiainfo.biapp.pec.approve.jx.dao;

import com.asiainfo.biapp.pec.approve.jx.dto.ApproveConfigDTO;
import com.asiainfo.biapp.pec.approve.jx.model.ApproveConfig;
import com.asiainfo.biapp.pec.approve.jx.vo.McdAppTypeVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 流程配置表 Mapper 接口
 * </p>
 *
 * @author feify
 * @since 2022-06-07
 */
@Mapper
public interface ApproveConfigDao extends BaseMapper<ApproveConfig> {

    IPage<ApproveConfigDTO> queryApproveConfigPageList(Page<ApproveConfigDTO> page, @Param(Constants.WRAPPER) Wrapper<ApproveConfigDTO> query, @Param("approveConfigDTO") ApproveConfigDTO approveConfigDTO);



    List<McdAppTypeVo> getAppTypeList ();
}
