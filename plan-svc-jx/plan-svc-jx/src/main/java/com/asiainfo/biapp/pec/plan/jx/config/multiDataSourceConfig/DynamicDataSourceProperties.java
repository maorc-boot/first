package com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig
 * @className: DynamicDataSourceProperties
 * @author: chenlin16
 * @description: TODO
 * @date: 2023/7/12 18:33
 */

@Data
@ConfigurationProperties(prefix = "khtusemultidatasource")
public class DynamicDataSourceProperties {
    private Map<String, DataSourceProperties> datasource = new LinkedHashMap<>();
}
