package com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo
 * @className: SelectTacticOverviewResultInfo
 * @author: chenlin
 * @description: 全局预览查询返回结果
 * @date: 2023/6/8 13:59
 * @version: 1.0
 */
@Data
public class SelectTacticOverviewResultInfo implements Serializable {

    private List<Ascription> ascription;

    private List<Product> products;

    private List<CustomerGroup> customerGroups;

    private List<Channel> channels;

    public SelectTacticOverviewResultInfo() {
    }

    public SelectTacticOverviewResultInfo(List<Ascription> ascription, List<Product> products, List<CustomerGroup> customerGroups, List<Channel> channels) {
        this.ascription = ascription;
        this.products = products;
        this.customerGroups = customerGroups;
        this.channels = channels;
    }
}
