package com.asiainfo.biapp.pec.plan.jx.enterprise.service;

import java.util.List;
import java.util.Map;

public interface ICustGroupSyncService {


    /**
     * COC调用接口保存客户群到 mcd_cust_push_log表
     *
     * @param custInfoXml WS报文
     * @param pushType    客户群：0；标签：1；
     * @return
     */
    String saveCustPushLog(String custInfoXml, int pushType);


}
