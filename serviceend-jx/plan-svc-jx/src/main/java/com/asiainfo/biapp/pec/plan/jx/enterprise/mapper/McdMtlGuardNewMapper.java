package com.asiainfo.biapp.pec.plan.jx.enterprise.mapper;

import com.asiainfo.biapp.pec.plan.jx.enterprise.enums.McdSysEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface McdMtlGuardNewMapper {

    List<McdSysEnum> queryEnumByTypeAndParentId(String enumType, String parentId) throws Exception;

    String queryTaskIdBySubTaskId(String subTaskTypeId);
}
