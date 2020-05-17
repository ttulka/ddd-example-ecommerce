package com.ttulka.ecommerce.shipping.dispatching.jdbc;

import com.ttulka.ecommerce.shipping.delivery.DispatchDelivery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Configuration for Dispatching Saga JDBC.
 */
@Configuration
class DispatchingSagaJdbcConfig {

    @Bean
    DispatchingSagaJdbc dispatchingSagaJdbc(DispatchDelivery dispatchDelivery, JdbcTemplate jdbcTemplate) {
        return new DispatchingSagaJdbc(dispatchDelivery, jdbcTemplate);
    }
}
