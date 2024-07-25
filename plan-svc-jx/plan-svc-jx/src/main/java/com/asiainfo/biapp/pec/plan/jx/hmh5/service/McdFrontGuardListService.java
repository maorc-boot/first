package com.asiainfo.biapp.pec.plan.jx.hmh5.service;


import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontGuardUserQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontGuardUserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.servlet.http.HttpServletResponse;


/**
 * 看护工号页面
 * @author ranpf
 *
 */
public interface McdFrontGuardListService {

	IPage<McdFrontGuardUserInfo> queryGuardUserList(McdFrontGuardUserQuery req);
	int getGuardUserCount(McdFrontGuardUserQuery req);
	void exportGuardUserExcel(McdFrontGuardUserQuery req,HttpServletResponse response);

}
