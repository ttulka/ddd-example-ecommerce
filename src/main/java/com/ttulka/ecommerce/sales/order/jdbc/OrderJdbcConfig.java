package com.ttulka.ecommerce.sales.order.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Configuration for JDBC implementation for Order service.
 */
@Configuration
class OrderJdbcConfig {

    @Bean
    FindOrdersJdbc findOrdersJdbc(JdbcTemplate jdbcTemplate, EventPublisher eventPublisher) {
        return new FindOrdersJdbc(jdbcTemplate, eventPublisher);
    }

    @Bean
    PlaceOrderJdbc placeOrderJdbc(JdbcTemplate jdbcTemplate, EventPublisher eventPublisher) {
        return new PlaceOrderJdbc(jdbcTemplate, eventPublisher);
    }
}
