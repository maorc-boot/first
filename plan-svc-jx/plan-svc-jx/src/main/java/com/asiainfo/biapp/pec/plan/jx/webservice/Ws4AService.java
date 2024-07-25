package com.asiainfo.biapp.pec.plan.jx.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "UpdateAppAcctSoap")
public interface Ws4AService {
	 String UpdateAppAcctSoap(@WebParam(name = "RequestInfo") String infoXml);
}
