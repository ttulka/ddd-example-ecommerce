package com.ttulka.ecommerce.warehouse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Test
    void zero_amount_has_zero_value() {
        assertThat(Amount.ZERO.value()).isEqualTo(0);
    }

    @Test
    void amount_is_added() {
        var amount = new Amount(1).add(new Amount(2));
        assertThat(amount.value()).isEqualTo(3);
    }

    @Test
    void amount_is_subtracted() {
        var amount = new Amount(3).subtract(new Amount(1));
        assertThat(amount.value()).isEqualTo(2);
    }

    @Test
    void amount_is_compared() {
        var amountGreater = new Amount(3);
        var amountLess = new Amount(1);
        assertAll(
                () -> assertThat(amountGreater.compareTo(amountLess)).isEqualTo(1),
                () -> assertThat(amountLess.compareTo(amountGreater)).isEqualTo(-1),
                () -> assertThat(amountGreater.compareTo(amountGreater)).isEqualTo(0)
        );
    }
}
