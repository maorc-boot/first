package com.asiainfo.biapp.pec.approve.jx.enterprise.dao;

import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.ApproveUserVo;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.McdCampDef;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.McdCampTask;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.McdCampTaskDate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审批信息查询
 *
 * @author admin
 * @version 1.0
 * @project iop-enterprise
 * @date 2023-06-26 14:22:39
 */
@Mapper
public interface EnterpriseApproveDao extends BaseMapper<McdCampTask> {
	//查询审批人角色ID（字典表）
	String queryDicApproveData() throws Exception;
	//查询审批人信息
	List<ApproveUserVo> queryApprove(@Param("roleId") String roleId) throws Exception;

	McdCampDef queryCampSegInfo(@Param("campSId") String campSId) throws Exception;

	void insertMcdCampsegTaskDate(@Param("taskDate") McdCampTaskDate taskDate);

	/**
	 * IMCD_ZJ 清单表生成成功的时候，更新策略信息清单表表名
	 */
	void updateCampsegInfo(McdCampDef segInfo);

	void updateCampDefInfo(McdCampDef segInfo);

	void updateChannelCampInfo(McdCampDef segInfo);



}
