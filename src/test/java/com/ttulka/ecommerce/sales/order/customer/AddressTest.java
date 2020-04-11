package com.ttulka.ecommerce.sales.order.customer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressTest {

    private static final String STRING_100_CHARS_LONG = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";

    @Test
    void address_value() {
        Address address = new Address("test");
        assertThat(address.value()).isEqualTo("test");
    }

    @Test
    void address_value_is_trimmed() {
        Address address = new Address("   test   ");
        assertThat(address.value()).isEqualTo("test");
    }

    @Test
    void address_fails_for_a_null_value() {
        assertThrows(IllegalArgumentException.class, () -> new Address(null));
    }

    @Test
    void address_fails_for_an_empty_string() {
        assertThrows(IllegalArgumentException.class, () -> new Address(""));
    }

    @Test
    void address_has_100_characters() {
        Address address = new Address(STRING_100_CHARS_LONG);
        assertThat(address.value()).isEqualTo(STRING_100_CHARS_LONG);
    }

    @Test
    void address_fails_for_more_than_100_characters() {
        assertThrows(IllegalArgumentException.class, () -> new Address(STRING_100_CHARS_LONG + "X"));
    }
}
