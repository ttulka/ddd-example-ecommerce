package com.ttulka.ecommerce.sales.order.customer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NameTest {

    private static final String STRING_50_CHARS_LONG = "01234567890123456789012345678901234567890123456789";

    @Test
    void name_value() {
        Name name = new Name("test");
        assertThat(name.value()).isEqualTo("test");
    }

    @Test
    void name_value_is_trimmed() {
        Name name = new Name("   test   ");
        assertThat(name.value()).isEqualTo("test");
    }

    @Test
    void name_fails_for_a_null_value() {
        assertThrows(IllegalArgumentException.class, () -> new Name(null));
    }

    @Test
    void name_fails_for_an_empty_string() {
        assertThrows(IllegalArgumentException.class, () -> new Name(""));
    }

    @Test
    void name_has_50_characters() {
        Name name = new Name(STRING_50_CHARS_LONG);
        assertThat(name.value()).isEqualTo(STRING_50_CHARS_LONG);
    }

    @Test
    void name_fails_for_more_than_50_characters() {
        assertThrows(IllegalArgumentException.class, () -> new Name(STRING_50_CHARS_LONG + "X"));
    }
}
