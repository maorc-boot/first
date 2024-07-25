package com.asiainfo.biapp.pec.preview.jx.model;

/**
 * @author mamp
 * @date 2022/10/23
 */

import lombok.Data;

/**
 * 预演结果封装Bean
 */
@Data
public class McdPreviewLogResult {

    /**
     * 活动ID
     */
    private String campsegId;
    /**
     * 渠道ID
     */
    private String chnannelId;

    /**
     * 原始客户群数量
     */
    private int custgroupTotal;
    // 渠道偏好过滤数量
    private int chnPreFilterCnt = 0;
    // 免打扰过滤数量
    private int bwlFilterCnt = 0;
    // 敏感客户群过滤数量
    private int sensitiveCustFilterCnt = 0;
    // 已订购过滤数量
    private int orderedFilterCnt = 0;
    // 已订购互斥过滤
    private int excludeFilterCnt = 0;
    // 已经推荐过滤数量
    private int recommendFilterCnt = 0;
    //  频次过滤数量
    private int fqcFilterCnt = 0;

    // 预演剩余用户数量
    private int currentCustNum = 0;

}
