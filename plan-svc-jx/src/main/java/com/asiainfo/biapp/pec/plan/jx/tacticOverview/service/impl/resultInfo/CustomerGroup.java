package com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.tacticOverview.model
 * @className: CustomerGroup
 * @author: chenlin
 * @description: 客户群统计(10万以下，10-100万，100-300万，300-500万，500以上）
 * @date: 2023/6/8 14:11
 * @version: 1.0
 */
@Data
public class CustomerGroup implements Serializable {

    private String customerGroupName;

    private Integer customerGroupCount;
}
