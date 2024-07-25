package com.asiainfo.biapp.pec.plan.jx.custgroup.dao;


import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefInfo;
import com.asiainfo.biapp.pec.plan.jx.custgroup.vo.McdCustGroupHttpVo;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupDef;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ICustGroupInfoDao extends BaseMapper<McdCustgroupDef> {



	 Map<String,Object>  isCustomDeletable(@Param("custGroupId")String custGroupId,@Param("userId") String userId);

	McdCustgroupDefInfo searchCustomDetail(@Param("custGroupId")String custGroupId);

	IPage<McdCustgroupDefInfo> searchCustGroup(Page page, @Param("param") McdCustGroupHttpVo custGroupHttpVo);


	List<McdCustgroupDefInfo> searchCycleCustGroups();
}
