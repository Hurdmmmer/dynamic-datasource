package com.example.dynamicdatasource.config;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DynamicDatasourceConfig {
    public final static String MASTER = "master";
    private final DataSourceProperties dataSourceProperties;
    private static final Logger logger = LoggerFactory.getLogger(DynamicDatasourceConfig.class);
    @Autowired
    public DynamicDatasourceConfig(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean("datasources")
    public Map<Object, Object> dataSourceMap() {
        logger.info("Load data source...");
        Map<String, DatabaseProperties> sources = dataSourceProperties.getSources();
        if (!sources.containsKey(MASTER)) {
            throw new RuntimeException("The 'master' database must exist");
        }
        Map<Object, Object> result = Maps.newHashMap();

        Set<Map.Entry<String, DatabaseProperties>> entries = sources.entrySet();
        for (Map.Entry<String, DatabaseProperties> entry : entries) {
            String key = entry.getKey();
            logger.info("Discover the data source: {}", key);
            DatabaseProperties properties = entry.getValue();
            DataSource source = DataSourceBuilder.create()
                    .driverClassName(properties.getDriverClassName())
                    .url(properties.getJdbcUrl())
                    .username(properties.getUsername())
                    .password(properties.getPassword())
                    .build();
            result.put(key, source);
            if (!key.equalsIgnoreCase(MASTER)) {
                DataSourceHolder.dataSourceIds.add(key);
            }
        }
        return result;
    }


    @Bean("routingDataSource")
    public DataSource routingDataSource(@Qualifier("datasources") Map<Object, Object> dataSourceMap) {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(dataSourceMap.get(MASTER));
        routingDataSource.setTargetDataSources(dataSourceMap);

        return routingDataSource;
    }

}
