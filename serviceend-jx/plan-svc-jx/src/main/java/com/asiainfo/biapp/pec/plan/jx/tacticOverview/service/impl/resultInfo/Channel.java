package com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.tacticOverview.model
 * @className: Channel
 * @author: chenlin
 * @description: 渠道统计（在线，线上，线下，短信）
 * @date: 2023/6/8 14:09
 * @version: 1.0
 */
@Data
public class Channel implements Serializable {
    /**
     *  渠道分类后的名称
     */
    private String channelName;
    /**
     *  渠道分类统计
     */
    private Integer channelCount;
}
