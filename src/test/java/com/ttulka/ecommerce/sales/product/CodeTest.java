package com.ttulka.ecommerce.sales.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CodeTest {

    private static final String STRING_50_CHARS_LONG = "01234567890123456789012345678901234567890123456789";

    @Test
    void code_value() {
        Code code = new Code("test");
        assertThat(code.value()).isEqualTo("test");
    }

    @Test
    void code_value_is_trimmed() {
        Code code = new Code("   test   ");
        assertThat(code.value()).isEqualTo("test");
    }

    @Test
    void code_fails_for_a_null_value() {
        assertThrows(IllegalArgumentException.class, () -> new Code(null));
    }

    @Test
    void code_fails_for_an_empty_string() {
        assertThrows(IllegalArgumentException.class, () -> new Code(""));
    }

    @Test
    void code_has_50_characters() {
        Code code = new Code(STRING_50_CHARS_LONG);
        assertThat(code.value()).isEqualTo(STRING_50_CHARS_LONG);
    }

    @Test
    void code_fails_for_more_than_50_characters() {
        assertThrows(IllegalArgumentException.class, () -> new Code(STRING_50_CHARS_LONG + "X"));
    }
}
