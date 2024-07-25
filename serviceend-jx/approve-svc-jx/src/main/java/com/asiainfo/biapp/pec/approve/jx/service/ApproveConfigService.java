package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.dto.ApproveConfigDTO;
import com.asiainfo.biapp.pec.approve.jx.model.ApproveConfig;
import com.asiainfo.biapp.pec.approve.jx.vo.McdAppTypeVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程配置表 服务类
 * </p>
 *
 * @author feify
 * @since 2022-06-07
 */
public interface ApproveConfigService extends IService<ApproveConfig> {

    /**
     * 查询流程配置列表
     * @param approveConfigDTO 查询流程配置的参数
     * @return
     */
    IPage<ApproveConfigDTO> queryApproveConfigPageList(ApproveConfigDTO approveConfigDTO);

    /**
     * 保存流程配置
     * @param approveConfig
     * @return
     */
    ApproveConfig saveOrUpdateApproveConfig(ApproveConfig approveConfig);

    /**
     * 查询流程配置
     * @param systemId
     * @return
     */
    ApproveConfig getApproveConfig(String systemId);



    List<McdAppTypeVo> getAppTypeList ();
}
