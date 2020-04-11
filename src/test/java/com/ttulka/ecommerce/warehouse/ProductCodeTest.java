package com.ttulka.ecommerce.warehouse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductCodeTest {

    private static final String STRING_50_CHARS_LONG = "01234567890123456789012345678901234567890123456789";

    @Test
    void product_code_value() {
        ProductCode productCode = new ProductCode("test");
        assertThat(productCode.value()).isEqualTo("test");
    }

    @Test
    void product_code_value_is_trimmed() {
        ProductCode productCode = new ProductCode("   test   ");
        assertThat(productCode.value()).isEqualTo("test");
    }

    @Test
    void product_code_fails_for_a_null_value() {
        assertThrows(IllegalArgumentException.class, () -> new ProductCode(null));
    }

    @Test
    void product_code_fails_for_an_empty_string() {
        assertThrows(IllegalArgumentException.class, () -> new ProductCode(""));
    }

    @Test
    void product_code_has_50_characters() {
        ProductCode productCode = new ProductCode(STRING_50_CHARS_LONG);
        assertThat(productCode.value()).isEqualTo(STRING_50_CHARS_LONG);
    }

    @Test
    void code_fails_for_more_than_50_characters() {
        assertThrows(IllegalArgumentException.class, () -> new ProductCode(STRING_50_CHARS_LONG + "X"));
    }
}
