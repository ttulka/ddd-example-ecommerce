package com.ttulka.ecommerce.shipping.delivery;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderIdTest {

    private static final String STR_64_CHARS = "0123456789012345678901234567890123456789012345678901234567890123";

    @Test
    void string_id_value() {
        OrderId orderId = new OrderId(123L);
        assertThat(orderId.value()).isEqualTo("123");
    }

    @Test
    void fails_for_null() {
        assertThrows(IllegalArgumentException.class, () -> new OrderId(null));
    }

    @Test
    void has_max_64_characters() {
        OrderId orderId = new OrderId(STR_64_CHARS);
        assertAll(
                () -> assertThat(orderId.value()).isEqualTo(STR_64_CHARS),
                () -> assertThrows(IllegalArgumentException.class, () -> new OrderId(STR_64_CHARS + "X"))
        );
    }
}
