package com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig
 * @className: DynamicDataSourceConfig
 * @author: chenlin16
 * @description: TODO
 * @date: 2023/7/12 18:32
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceConfig {

    @Autowired
    private DynamicDataSourceProperties properties;

    @Resource
    private DataSource defaultDataSource;   //按名称注入下列的bean

    /*
     * @param :
     * @return DataSource:
     * @author chenlin16
     * @description 有类继承了AbstractRoutingDataSource之后，就不会再自动配置druid的数据源，但现在需要默认的druid数据源和druid其他配置
     * @date 2023/7/26 10:37
     */
    @Primary    //不加会报错，原因：Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required
    @Bean(autowireCandidate = false)    //如果不设置为false，则数据源会初始化，切换数据源会失效
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource defaultDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @SneakyThrows
    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //默认数据源
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        //设置多个数据源的map
        dynamicDataSource.setTargetDataSources(getDynamicDataSource());
        return dynamicDataSource;
    }

    private Map<Object, Object> getDynamicDataSource() {
        Map<String, DataSourceProperties> dataSourcePropertiesMap = properties.getDatasource();
        Map<Object, Object> targetDataSources = new HashMap<>(dataSourcePropertiesMap.size());
        dataSourcePropertiesMap.forEach((k, v) -> targetDataSources.put(k, buildDruidDataSource(v)));
        return targetDataSources;
    }

    private DruidDataSource buildDruidDataSource(DataSourceProperties properties) {
        //将其他druid数据源的配置设定为默认druid数据源的配置
        //因数据源都是由p6spy代理，也不需要设置dbType等字段
        DruidDataSource druidDataSource = ((DruidDataSource) defaultDataSource).cloneDruidDataSource();
        //用其他数据源覆盖被克隆出来的数据源
        druidDataSource.setDriverClassName(properties.getDriverClassName());
        druidDataSource.setUrl(properties.getUrl());
        druidDataSource.setUsername(properties.getUsername());
        druidDataSource.setPassword(properties.getPassword());
        //将data中设定的值，设定为连接验证查询语句，如果没有配置属性，则是druid的默认配置。如果属性值为空，表示不需要验证查询
        if (properties.getData() != null){
            if (properties.getData().size()==0) {
                druidDataSource.setValidationQuery(null);
            }else if (StringUtils.isNotBlank(properties.getData().get(0))){
                druidDataSource.setValidationQuery(properties.getData().get(0));
            }
        }
        //初始化数据源
        try {
            druidDataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

}
