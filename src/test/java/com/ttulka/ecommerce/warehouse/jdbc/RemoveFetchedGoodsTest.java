package com.ttulka.ecommerce.warehouse.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.warehouse.RemoveFetchedGoods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@JdbcTest
@ContextConfiguration(classes = WarehouseJdbcConfig.class)
class RemoveFetchedGoodsTest {

    @Autowired
    private RemoveFetchedGoods removeFetchedGoods;

    @MockBean
    private EventPublisher eventPublisher;

    // TODO
}
