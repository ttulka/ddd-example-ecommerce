package com.ttulka.ecommerce.sales.order;

import java.time.Instant;
import java.util.Map;

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
                () -> assertThat(orderPlaced.items).hasSize(2),
                () -> assertThat(orderPlaced.items.get("test-1")).isEqualTo(1),
                () -> assertThat(orderPlaced.items.get("test-2")).isEqualTo(2)
        );
    }

    private OrderPlaced fakeOrderPlaced() {
        return new OrderPlaced(
                Instant.now(),
                "TEST123",
                Map.of("test-1", 1, "test-2", 2), 5.f);
    }
}
