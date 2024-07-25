package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontGuardUserQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdFrontGuardUserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


;

/**
 * 看护管理
 * @author ranpf
 *   2023-02-15
 *
 */
public interface McdFrontGuardListDao {

	IPage<McdFrontGuardUserInfo> queryGuardUserList(Page page, @Param("req") McdFrontGuardUserQuery req);
	int getGuardUserCount(@Param("req")McdFrontGuardUserQuery req);
}
