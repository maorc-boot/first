package com.asiainfo.biapp.pec.plan.jx.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 客户群基本信息创建对外接口
 *
 * @author imcd
 */
@WebService
public interface ICustgroupAPIServiceJx {

    /**
     * 保存客户群基本信息
     * url：http://ip:port/plan-svc/services/SendCustInfo?wsdl
     *
     * @param custInfoXml 客户群基本信息
     * @return 保存成功则返回
     */
    @WebMethod
    String sendCustInfoJx(@WebParam(name = "param") String custInfoXml);
}
