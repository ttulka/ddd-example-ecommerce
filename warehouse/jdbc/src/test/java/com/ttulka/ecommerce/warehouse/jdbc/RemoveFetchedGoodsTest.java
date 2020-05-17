package com.ttulka.ecommerce.warehouse.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.warehouse.RemoveFetchedGoods;
import com.ttulka.ecommerce.warehouse.Warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@JdbcTest
@ContextConfiguration(classes = RemoveFetchedGoodsTest.TestConfig.class)
class RemoveFetchedGoodsTest {

    @Autowired
    private RemoveFetchedGoods removeFetchedGoods;

    @MockBean
    private EventPublisher eventPublisher;

    // TODO

    @Configuration
    static class TestConfig {
        @Bean
        WarehouseJdbc warehouseJdbc(JdbcTemplate jdbcTemplate) {
            return new WarehouseJdbc(jdbcTemplate);
        }
        @Bean
        GoodsFetchingJdbc goodsFetchingJdbc(Warehouse warehouse, JdbcTemplate jdbcTemplate, EventPublisher eventPublisher) {
            return new GoodsFetchingJdbc(warehouse, jdbcTemplate, eventPublisher);
        }
    }
}
