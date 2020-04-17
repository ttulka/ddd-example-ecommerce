package com.ttulka.ecommerce.shipping.delivery.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.FindDeliveries;
import com.ttulka.ecommerce.shipping.UpdateDelivery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Configuration for JDBC implementation for Delivery domain.
 */
@Configuration
class DeliveryJdbcConfig {

    @Bean
    FindDeliveriesJdbc findDeliveries(JdbcTemplate jdbcTemplate, EventPublisher eventPublisher) {
        return new FindDeliveriesJdbc(jdbcTemplate, eventPublisher);
    }

    @Bean
    PrepareDeliveryJdbc prepareDelivery(JdbcTemplate jdbcTemplate, EventPublisher eventPublisher) {
        return new PrepareDeliveryJdbc(jdbcTemplate, eventPublisher);
    }

    @Bean
    UpdateDelivery updateDelivery(FindDeliveries findDeliveries) {
        return new UpdateDelivery(findDeliveries);
    }
}
