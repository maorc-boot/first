package com.asiainfo.biapp.pec.plan.jx.enterprise.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ICocCustGroupSyncMapper  {
    /**
     * 根据客户群ID和数据日期 查询是否有待执行或者执行中的任务，避免重复推送
     *
     * @param customGroupId       客户群ID
     * @param customGroupDataDate 数据日期
     * @param pushType            推送类型： 0- 客户群，1- 客户通标签
     * @return
     */
    int queryCustGroupHis(String customGroupId, String customGroupDataDate, int pushType);

    /**
     * 保存 MCD_CUSTGROUP_PUSH_LOG
     *
     * @param custGroupId  客户群ID
     * @param dataDate     数据日期
     * @param pushType     推送数据类型,0-客户群，1-标签
     * @param isManualPush 手工推送标记:1-手工，2-推送
     * @param custInfoXml  Coc客户群xml报文
     */
    void saveCustPushLog(String custGroupId, String dataDate, int pushType, String isManualPush, String custInfoXml);

    /**
     * 根据客户群ID查询 mcd_custgroup_push_log或 MCD_CUSTGROUP_MANUAL_PUSH_LOG 数据
     *
     * @param custGroupId 客户群ID
     * @return
     */
    @MapKey("id")
    List<Map<String, Object>> selectCustPushLogById(String custGroupId, String table);
}
