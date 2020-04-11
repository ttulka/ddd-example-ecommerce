package com.ttulka.ecommerce.warehouse;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmountTest {

    @Test
    void amount_value() {
        Amount amount = new Amount(123);
        assertThat(amount.value()).isEqualTo(123);
    }

    @Test
    void amount_fails_for_a_value_less_than_zero() {
        assertThrows(IllegalArgumentException.class, () -> new Amount(-1));
    }
}
