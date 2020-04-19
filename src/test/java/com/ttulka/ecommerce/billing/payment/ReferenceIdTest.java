package com.ttulka.ecommerce.billing.payment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReferenceIdTest {

    @Test
    void string_id_value() {
        ReferenceId referenceId = new ReferenceId(123L);
        assertThat(referenceId.value()).isEqualTo("123");
    }

    @Test
    void fails_for_null() {
        assertThrows(IllegalArgumentException.class, () -> new ReferenceId(null));
    }
}
