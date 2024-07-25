package com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig
 * @className: DynamicDataSource
 * @author: chenlin16
 * @description: TODO
 * @date: 2023/7/12 18:30
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicContextHolder.peek();
    }
}
