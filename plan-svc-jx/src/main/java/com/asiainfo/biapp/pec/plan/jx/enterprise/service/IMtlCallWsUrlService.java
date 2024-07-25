package com.asiainfo.biapp.pec.plan.jx.enterprise.service;

import com.asiainfo.biapp.pec.plan.jx.enterprise.model.McdSysInterfaceDef;

/**
 * 调用ws url和log类；
 * @author chengxc
 *
 */
public interface IMtlCallWsUrlService {
	/**
	 * 按照url code 找ws url；
	 * @param ws_jndi
	 * @return
	 * @throws Exception
	 */
	public McdSysInterfaceDef getCallwsURL(String ws_jndi) throws Exception;
	
}
