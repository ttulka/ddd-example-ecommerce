package com.ttulka.ecommerce.common.primitives;

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
    void zero_money_has_a_zero_value() {
        assertThat(Money.ZERO.value()).isEqualTo(0.f);
    }

    @Test
    void money_fails_for_a_negative_value() {
        assertThrows(IllegalArgumentException.class, () -> new Money(-12.34f));
    }

    @Test
    void money_fails_for_a_value_greater_than_one_million() {
        assertThrows(IllegalArgumentException.class, () -> new Money(1_000_000.1f));
    }

    @Test
    void money_is_multiplied() {
        Money money = new Money(12.34f);
        assertThat(money.multi(2)).isEqualTo(new Money(12.34f * 2));
    }

    @Test
    void money_is_added() {
        Money money = new Money(12.34f);
        assertThat(money.add(new Money(2.5f))).isEqualTo(new Money(12.34f + 2.5f));
    }
}
