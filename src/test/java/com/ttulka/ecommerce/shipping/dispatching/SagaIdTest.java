package com.ttulka.ecommerce.shipping.dispatching;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SagaIdTest {

    @Test
    void string_id_value() {
        SagaId sagaId = new SagaId(123L);
        assertThat(sagaId.value()).isEqualTo("123");
    }

    @Test
    void fails_for_null() {
        assertThrows(IllegalArgumentException.class, () -> new SagaId(null));
    }
}
