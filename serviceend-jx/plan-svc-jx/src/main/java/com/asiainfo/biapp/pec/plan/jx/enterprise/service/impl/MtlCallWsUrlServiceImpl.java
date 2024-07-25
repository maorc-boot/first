package com.asiainfo.biapp.pec.plan.jx.enterprise.service.impl;


import com.asiainfo.biapp.pec.plan.jx.enterprise.mapper.IMtlCallwsUrlMapper;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.IMtlCallWsUrlService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.model.McdSysInterfaceDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("mtlCallWsUrlService")
public class MtlCallWsUrlServiceImpl implements IMtlCallWsUrlService {
	@Resource
	private IMtlCallwsUrlMapper callwsUrlMapper;
	
	private final Log log = LogFactory.getLog(getClass());
	public IMtlCallwsUrlMapper getMtlCallwsUrlDao() {
		return callwsUrlMapper;
	}
	public void setMtlCallwsUrlDao(IMtlCallwsUrlMapper mtlCallwsUrlDao) {
		this.callwsUrlMapper = mtlCallwsUrlDao;
	}
	@Override
	public McdSysInterfaceDef getCallwsURL(String ws_jndi) throws Exception {
		McdSysInterfaceDef result = null;
		try {
			//获取调用web service的URL
			McdSysInterfaceDef mtlCallwsUrl = new McdSysInterfaceDef();
			mtlCallwsUrl.setCallwsUrlCode(ws_jndi);
			List<McdSysInterfaceDef> objList = callwsUrlMapper.findByCond(mtlCallwsUrl);
			if (objList != null && objList.size() > 0) {
				result = (McdSysInterfaceDef) objList.get(0);
			}
		} catch (Exception e) {
		    log.error("查询接口报错",e);
		}
		return result;
	}
}
