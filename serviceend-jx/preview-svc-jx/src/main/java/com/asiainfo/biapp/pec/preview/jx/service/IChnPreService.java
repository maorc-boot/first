package com.asiainfo.biapp.pec.preview.jx.service;

/**
 * @author mamp
 * @date 2022/6/9
 */
public interface IChnPreService {

    /**
     * 更新渠道偏好模型数据
     */
    void updateChnPreModel();

    /**
     * 计算客户群渠道偏好数据
     *
     * @param custGroupId  客户群ID
     * @param dataDate     数据日期
     * @param custFileName 客户群清单文件名称
     */
    boolean custChnPreCal(String custGroupId, String dataDate, String custFileName);



}
