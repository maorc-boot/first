package com.asiainfo.biapp.pec.approve.jx.service.impl;

import com.asiainfo.biapp.pec.approve.common.McdException;
import com.asiainfo.biapp.pec.approve.common.MetaHandler;
import com.asiainfo.biapp.pec.approve.jx.dao.ApproveConfigDao;
import com.asiainfo.biapp.pec.approve.jx.dao.SysUserDao;
import com.asiainfo.biapp.pec.approve.jx.dto.ApproveConfigDTO;
import com.asiainfo.biapp.pec.approve.jx.model.ApproveConfig;
import com.asiainfo.biapp.pec.approve.jx.model.SysUser;
import com.asiainfo.biapp.pec.approve.jx.service.ApproveConfigService;
import com.asiainfo.biapp.pec.approve.jx.vo.McdAppTypeVo;
import com.asiainfo.biapp.pec.approve.model.User;
import com.asiainfo.biapp.pec.approve.util.DataUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 流程配置表 服务实现类
 * </p>
 *
 * @author feify
 * @since 2022-06-07
 */
@Service
public class ApproveConfigServiceImpl extends ServiceImpl<ApproveConfigDao, ApproveConfig> implements ApproveConfigService {


    @Setter(onMethod_ = {@Autowired})
    private SysUserDao sysUserDao;

    @Override
    public IPage<ApproveConfigDTO> queryApproveConfigPageList(ApproveConfigDTO approveConfigDTO) {
        Page<ApproveConfigDTO> page = new Page<>(approveConfigDTO.getCurrent(), approveConfigDTO.getSize());
        return baseMapper.queryApproveConfigPageList(page, Wrappers.query(approveConfigDTO), approveConfigDTO);
    }

    @Override
    public ApproveConfig saveOrUpdateApproveConfig(ApproveConfig approveConfig) {
        int count = count(Wrappers.<ApproveConfig>query().lambda()
                .eq(ApproveConfig::getCityId, approveConfig.getCityId())
                .eq(ApproveConfig::getDeptId, approveConfig.getDeptId())
                .eq(ApproveConfig::getCountyId, approveConfig.getCountyId())
                .eq(ApproveConfig::getGridId, approveConfig.getGridId())
                .eq(ApproveConfig::getSystemId, approveConfig.getSystemId())
                .ne(approveConfig.getId() != null, ApproveConfig::getId, approveConfig.getId()));

        if (count != 0) {
            McdException.throwMcdException(McdException.McdExceptionEnum.APPROVE_CONF_ALREADY_EXISTS);
        }

        if (approveConfig.getId() == null) {
            approveConfig.setId(DataUtil.generateId());
        }

        saveOrUpdate(approveConfig);

        return approveConfig;
    }

    @Override
    public ApproveConfig getApproveConfig(String systemId) {
        User u = MetaHandler.getUser();
        //TODO 2023-07-04 16:56:07 创建临时用户
        if (u == null) {
            log.error("没有获取到登录用户，正在创建虚拟用户！--------------------------------------------------------------");
            u = new User();
            u.setUserId("admin01");
        }
        SysUser user = sysUserDao.selectOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUserId, u.getUserId()));
        Set<String> deptIds = new HashSet<>();
        deptIds.add(user.getDepartmentId());
        Set<String> cityIds = new HashSet<>();
        cityIds.add(user.getCityId());
        Set<String> countyIds = new HashSet<>();
        countyIds.add(user.getCountyId());
        Set<String> gridIds = new HashSet<>();
        gridIds.add(user.getGridId());

        ApproveConfig approveConfig = baseMapper.selectOne(Wrappers.<ApproveConfig>query().lambda()
                .in(ApproveConfig::getDeptId, deptIds)
                .in(ApproveConfig::getCityId, cityIds)
                .in(ApproveConfig::getCountyId, countyIds)
                .in(ApproveConfig::getGridId, gridIds)
                .eq(ApproveConfig::getSystemId, systemId)
                .orderByDesc(ApproveConfig::getDeptId)
                .orderByDesc(ApproveConfig::getCityId)
                .last("LIMIT 1")
        );

        if (approveConfig == null) {
            cityIds.add("-1");
            deptIds.add("-1");
            countyIds.add("-1");
            gridIds.add("-1");
            approveConfig = baseMapper.selectOne(Wrappers.<ApproveConfig>query().lambda()
                    .in(ApproveConfig::getDeptId, deptIds)
                    .in(ApproveConfig::getCityId, cityIds)
                    .in(ApproveConfig::getCountyId, countyIds)
                    .in(ApproveConfig::getGridId, gridIds)
                    .eq(ApproveConfig::getSystemId, systemId)
                    .orderByDesc(ApproveConfig::getDeptId)
                    .orderByDesc(ApproveConfig::getCityId)
                    .orderByDesc(ApproveConfig::getCountyId)
                    .orderByDesc(ApproveConfig::getGridId)
                    .last("LIMIT 1")
            );
        }

        return approveConfig;
    }


    @Override
    public List<McdAppTypeVo> getAppTypeList() {
        return baseMapper.getAppTypeList();
    }
}
