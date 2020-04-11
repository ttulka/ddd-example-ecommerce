package com.ttulka.ecommerce.sales.order;

import java.time.Instant;
import java.util.List;

import com.ttulka.ecommerce.sales.OrderPlaced;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderPlacedTest {

    @Test
    void when_is_set() {
        OrderPlaced orderPlaced = fakeOrderPlaced();

        assertThat(orderPlaced.when).isNotNull();
    }

    @Test
    void order_id_is_set() {
        OrderPlaced orderPlaced = fakeOrderPlaced();

        assertThat(orderPlaced.orderId).isEqualTo("TEST123");
    }

    @Test
    void order_items_are_set() {
        OrderPlaced orderPlaced = fakeOrderPlaced();
        assertAll(
                () -> assertThat(orderPlaced.orderItems).hasSize(2),
                () -> assertThat(orderPlaced.orderItems.get(0).code).isEqualTo("test-1"),
                () -> assertThat(orderPlaced.orderItems.get(0).title).isEqualTo("Test 1"),
                () -> assertThat(orderPlaced.orderItems.get(0).price).isEqualTo(1.f),
                () -> assertThat(orderPlaced.orderItems.get(0).quantity).isEqualTo(1),
                () -> assertThat(orderPlaced.orderItems.get(1).code).isEqualTo("test-2"),
                () -> assertThat(orderPlaced.orderItems.get(1).title).isEqualTo("Test 2"),
                () -> assertThat(orderPlaced.orderItems.get(1).price).isEqualTo(2.f),
                () -> assertThat(orderPlaced.orderItems.get(1).quantity).isEqualTo(2)
        );
    }

    @Test
    void customer_is_set() {
        OrderPlaced orderPlaced = fakeOrderPlaced();
        assertAll(
                () -> assertThat(orderPlaced.customer.name).isEqualTo("test name"),
                () -> assertThat(orderPlaced.customer.address).isEqualTo("test address")
        );
    }

    private OrderPlaced fakeOrderPlaced() {
        return new OrderPlaced(Instant.now(), "TEST123",
                               List.of(
                                       new OrderPlaced.OrderItemData("test-1", "Test 1", 1.f, 1),
                                       new OrderPlaced.OrderItemData("test-2", "Test 2", 2.f, 2)),
                               new OrderPlaced.CustomerData("test name", "test address"));
    }
}
