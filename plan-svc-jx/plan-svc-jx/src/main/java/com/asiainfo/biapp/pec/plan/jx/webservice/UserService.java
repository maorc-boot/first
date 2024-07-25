package com.asiainfo.biapp.pec.plan.jx.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Emis同步用户信息 webservice接口
 */
@WebService
public interface UserService {
    /**
     * 用户相关操作 增 删 改
     *
     * @param infoXml
     * @return
     */
    @WebMethod
    String operUser(String infoXml);

    /**
     * 查询角色
     *
     * @param infoXml
     * @return
     */
    @WebMethod
    String queryRole(String infoXml);

    /**
     * 查询 组织
     *
     * @param infoXml
     * @return
     */
    @WebMethod
    String queryOrg(String infoXml);
}
