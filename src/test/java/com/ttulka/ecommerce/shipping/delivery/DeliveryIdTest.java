package com.ttulka.ecommerce.shipping.delivery;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryIdTest {

    private static final String STR_64_CHARS = "0123456789012345678901234567890123456789012345678901234567890123";

    @Test
    void string_id_value() {
        DeliveryId deliveryId = new DeliveryId(123L);
        assertThat(deliveryId.value()).isEqualTo("123");
    }

    @Test
    void fails_for_null() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryId(null));
    }

    @Test
    void has_max_64_characters() {
        DeliveryId deliveryId = new DeliveryId(STR_64_CHARS);
        assertAll(
                () -> assertThat(deliveryId.value()).isEqualTo(STR_64_CHARS),
                () -> assertThrows(IllegalArgumentException.class, () -> new DeliveryId(STR_64_CHARS + "X"))
        );
    }
}
