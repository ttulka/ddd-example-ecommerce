package com.ttulka.ecommerce.sales.order.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.sales.FindOrders;
import com.ttulka.ecommerce.sales.order.Order;
import com.ttulka.ecommerce.sales.order.OrderId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ContextConfiguration(classes = OrderJdbcConfig.class)
@Sql("/test-data-sales-find-orders.sql")
class FindOrdersTest {

    @Autowired
    private FindOrders findOrders;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void order_by_id_is_found() {
        Order order = findOrders.byId(new OrderId(1));
        assertAll(
                () -> assertThat(order.id()).isEqualTo(new OrderId(1)),
                () -> assertThat(order.items()).hasSize(2),
                () -> assertThat(order.items().get(0).code()).isEqualTo("001"),
                () -> assertThat(order.items().get(0).title()).isEqualTo("Prod 1"),
                () -> assertThat(order.items().get(0).price()).isEqualTo(123.5f),
                () -> assertThat(order.items().get(0).quantity()).isEqualTo(1),
                () -> assertThat(order.items().get(1).code()).isEqualTo("002"),
                () -> assertThat(order.items().get(1).title()).isEqualTo("Prod 2"),
                () -> assertThat(order.items().get(1).price()).isEqualTo(321.5f),
                () -> assertThat(order.items().get(1).quantity()).isEqualTo(2)
        );
    }

    @Test
    void unknown_product_found_for_an_unknown_code() {
        Order order = findOrders.byId(new OrderId(123));

        assertThat(order.id()).isEqualTo(new OrderId(0));
    }
}
