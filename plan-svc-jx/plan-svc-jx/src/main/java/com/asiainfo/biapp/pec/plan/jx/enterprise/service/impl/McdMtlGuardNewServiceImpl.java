package com.asiainfo.biapp.pec.plan.jx.enterprise.service.impl;

import com.asiainfo.biapp.pec.plan.jx.enterprise.enums.McdSysEnum;
import com.asiainfo.biapp.pec.plan.jx.enterprise.mapper.McdMtlGuardNewMapper;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.IMcdMtlGuardNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class McdMtlGuardNewServiceImpl implements IMcdMtlGuardNewService {

    @Autowired
    private McdMtlGuardNewMapper mcdMtlGuardNewMapper;
    @Override
    public List<McdSysEnum> queryEnumByTypeAndParentId(String enumType, String parentId) throws Exception {
        return mcdMtlGuardNewMapper.queryEnumByTypeAndParentId(enumType, parentId);
    }

    @Override
    public String queryTaskIdBySubTaskId(String subTaskTypeId) {
        return mcdMtlGuardNewMapper.queryTaskIdBySubTaskId(subTaskTypeId);
    }
}
