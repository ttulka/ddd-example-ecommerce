package com.ttulka.ecommerce.common.money;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void money_value() {
        Money money = new Money(12.34f);
        assertThat(money.value()).isEqualTo(12.34f);
    }

    @Test
    void money_created_for_a_zero_value() {
        Money money = new Money(0.f);
        assertThat(money.value()).isEqualTo(0.f);
    }

    @Test
    void money_fails_for_a_negative_value() {
        assertThrows(IllegalArgumentException.class, () -> new Money(-12.34f));
    }

    @Test
    void money_fails_for_a_value_greater_than_one_million() {
        assertThrows(IllegalArgumentException.class, () -> new Money(1_000_000.1f));
    }
}
