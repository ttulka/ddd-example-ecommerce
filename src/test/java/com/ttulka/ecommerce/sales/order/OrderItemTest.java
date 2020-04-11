package com.ttulka.ecommerce.sales.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemTest {

    @Test
    void order_item_is_created() {
        OrderItem orderItem = new OrderItem("test-1", "Test 1", 1.f, 1);
        assertThat(orderItem).isNotNull();
    }

    @Test
    void code_cannot_be_null_or_empty() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new OrderItem(null, "Test 1", 1.f, 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new OrderItem("", "Test 1", 1.f, 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new OrderItem("   ", "Test 1", 1.f, 1))
        );
    }

    @Test
    void title_cannot_be_null_or_empty() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new OrderItem("test-1", null, 1.f, 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new OrderItem("test-1", "", 1f, 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new OrderItem("test-1", "   ", 1.f, 1))
        );
    }

    @Test
    void price_cannot_be_less_than_zero() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem("test-1", "Test 1", -1.f, 1));
    }

    @Test
    void quantity_cannot_be_less_than_one() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new OrderItem("test-1", "Test 1", 1.f, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new OrderItem("test-1", "Test 1", 1.f, -1))
        );
    }
}
