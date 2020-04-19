package com.ttulka.ecommerce.sales.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CodeTest {

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
}
