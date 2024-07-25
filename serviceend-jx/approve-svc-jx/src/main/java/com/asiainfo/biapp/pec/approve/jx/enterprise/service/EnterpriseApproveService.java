package com.asiainfo.biapp.pec.approve.jx.enterprise.service;

import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.ApproveUserVo;
import com.asiainfo.biapp.pec.approve.jx.enterprise.vo.McdCampDef;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author admin
 * @version 1.0
 * @project iop-enterprise
 * @date 2023-06-26 11:30:19
 */
public interface EnterpriseApproveService {

	List<ApproveUserVo> queryApprove() throws Exception;

	// McdCampDef getCampSegInfo(String campSegId) throws Exception;

	Map<String, Object> updateCampInfo(McdCampDef campDef) throws Exception;

}
