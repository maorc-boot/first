package com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.tacticOverview.model
 * @className: Product
 * @author: chenlin
 * @description: 产品统计（营销类，非营销类）
 * @date: 2023/6/8 14:07
 * @version: 1.0
 */
@Data
public class Product implements Serializable {

    private String productName;

    private Integer productCount;

}
