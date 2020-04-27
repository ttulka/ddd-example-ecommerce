package com.ttulka.ecommerce.sales.order.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.order.FindOrders;
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
                () -> assertThat(order.total()).isEqualTo(new Money(1000.f)),
                () -> assertThat(order.items()).hasSize(2),
                () -> assertThat(order.items().get(0).unitPrice()).isEqualTo(new Money(123.5f)),
                () -> assertThat(order.items().get(0).quantity()).isEqualTo(new Quantity(1)),
                () -> assertThat(order.items().get(1).unitPrice()).isEqualTo(new Money(321.5f)),
                () -> assertThat(order.items().get(1).quantity()).isEqualTo(new Quantity(2))
        );
    }

    @Test
    void unknown_product_found_for_an_unknown_id() {
        Order order = findOrders.byId(new OrderId(123));

        assertThat(order.id()).isEqualTo(new OrderId(0));
    }
}
