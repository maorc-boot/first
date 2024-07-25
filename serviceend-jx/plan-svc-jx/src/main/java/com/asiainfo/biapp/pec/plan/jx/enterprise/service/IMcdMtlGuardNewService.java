package com.asiainfo.biapp.pec.plan.jx.enterprise.service;

import com.asiainfo.biapp.pec.plan.jx.enterprise.enums.McdSysEnum;

import java.util.List;

public interface IMcdMtlGuardNewService {

    List<McdSysEnum> queryEnumByTypeAndParentId(String enumType, String parentId) throws Exception;

    String queryTaskIdBySubTaskId(String subTaskTypeId);
}
