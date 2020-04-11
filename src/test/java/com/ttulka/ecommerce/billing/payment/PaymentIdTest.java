package com.ttulka.ecommerce.billing.payment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentIdTest {

    private static final String STR_64_CHARS = "0123456789012345678901234567890123456789012345678901234567890123";

    @Test
    void string_id_value() {
        PaymentId paymentId = new PaymentId(123L);
        assertThat(paymentId.value()).isEqualTo("123");
    }

    @Test
    void fails_for_null() {
        assertThrows(IllegalArgumentException.class, () -> new PaymentId(null));
    }

    @Test
    void has_max_64_characters() {
        PaymentId paymentId = new PaymentId(STR_64_CHARS);
        assertAll(
                () -> assertThat(paymentId.value()).isEqualTo(STR_64_CHARS),
                () -> assertThrows(IllegalArgumentException.class, () -> new PaymentId(STR_64_CHARS + "X"))
        );
    }
}
