package com.ttulka.ecommerce.billing.payment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentIdTest {

    @Test
    void string_id_value() {
        PaymentId paymentId = new PaymentId(123L);
        assertThat(paymentId.value()).isEqualTo("123");
    }

    @Test
    void fails_for_null() {
        assertThrows(IllegalArgumentException.class, () -> new PaymentId(null));
    }
}
