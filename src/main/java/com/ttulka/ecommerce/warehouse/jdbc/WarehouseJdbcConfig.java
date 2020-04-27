package com.ttulka.ecommerce.warehouse.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.warehouse.Warehouse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Configuration for JDBC implementation for Warehouse service.
 */
@Configuration
class WarehouseJdbcConfig {

    @Bean
    WarehouseJdbc warehouseJdbc(JdbcTemplate jdbcTemplate) {
        return new WarehouseJdbc(jdbcTemplate);
    }

    @Bean
    GoodsFetchingJdbc goodsFetchingJdbc(Warehouse warehouse, JdbcTemplate jdbcTemplate, EventPublisher eventPublisher) {
        return new GoodsFetchingJdbc(warehouse, jdbcTemplate, eventPublisher);
    }
}
