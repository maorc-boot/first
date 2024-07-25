package com.asiainfo.biapp.pec.plan.jx.custgroup.service;


import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefInfo;
import com.asiainfo.biapp.pec.plan.jx.custgroup.vo.McdCustGroupHttpVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;




public interface ICustGroupInfoService {

     void deleteCustom(String customGrpId);

     McdCustgroupDefInfo searchCustomDetail(String customGrpId);

	 IPage<McdCustgroupDefInfo> searchCustGroup( McdCustGroupHttpVo custGroupHttpVo);

    List<McdCustgroupDefInfo> searchCycleCustGroups();

}

