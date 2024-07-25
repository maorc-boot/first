package com.asiainfo.biapp.pec.plan.jx.custgroup.service.impl;

import com.asiainfo.biapp.pec.plan.jx.custgroup.dao.ICustGroupInfoDao;
import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefInfo;
import com.asiainfo.biapp.pec.plan.jx.custgroup.service.ICustGroupInfoService;
import com.asiainfo.biapp.pec.plan.jx.custgroup.vo.McdCustGroupHttpVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("custGroupInfoService")
public class CustGroupInfoServiceImpl implements ICustGroupInfoService {
	
    protected final Logger log = LoggerFactory.getLogger(getClass());


	@Resource()
	ICustGroupInfoDao custGroupInfoDao;


	@Override
	public McdCustgroupDefInfo searchCustomDetail(String custGroupId) {
		return custGroupInfoDao.searchCustomDetail(custGroupId);
	}

    @Override
    public void deleteCustom(String custGroupId) {
        custGroupInfoDao.deleteById(custGroupId);
    }

	@Override
	public IPage<McdCustgroupDefInfo> searchCustGroup(McdCustGroupHttpVo custGroupHttpVo) {
		Page pager = new Page(custGroupHttpVo.getCurrent(), custGroupHttpVo.getSize());
		IPage<McdCustgroupDefInfo> mcdCustgroupDefInfoIPage = custGroupInfoDao.searchCustGroup(pager, custGroupHttpVo);

		mcdCustgroupDefInfoIPage.getRecords().forEach(record->{
            Map<String,Object> list = custGroupInfoDao.isCustomDeletable(record.getCustomGroupId(),custGroupHttpVo.getUserId());
		    String delFlag = Objects.isNull(list)? "0":"1";
            record.setDelFlag(delFlag);
        });

		return mcdCustgroupDefInfoIPage;
	}

	@Override
	public List<McdCustgroupDefInfo> searchCycleCustGroups() {

		return custGroupInfoDao.searchCycleCustGroups();
	}
}
