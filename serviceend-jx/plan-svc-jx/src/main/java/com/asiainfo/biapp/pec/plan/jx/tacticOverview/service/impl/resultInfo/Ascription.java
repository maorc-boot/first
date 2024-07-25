package com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.tacticOverview.model
 * @className: Ascription
 * @author: chenlin
 * @description: 归属统计（地市，全省）
 * @date: 2023/6/8 14:05
 * @version: 1.0
 */
@Data
public class Ascription implements Serializable {

    private String cityName;

    private Integer tacticCount;

    public Ascription() {
    }

    public Ascription(String cityName, Integer tacticCount) {
        this.cityName = cityName;
        this.tacticCount = tacticCount;
    }
}
