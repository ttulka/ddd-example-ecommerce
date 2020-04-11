package com.ttulka.ecommerce.billing.payment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void money_value() {
        Money money = new Money(12.34);
        assertThat(money.value()).isEqualTo(12.34);
    }

    @Test
    void money_created_for_a_zero_value() {
        Money money = new Money(0.);
        assertThat(money.value()).isEqualTo(0.);
    }

    @Test
    void money_fails_for_a_negative_value() {
        assertThrows(IllegalArgumentException.class, () -> new Money(-12.34));
    }
}
