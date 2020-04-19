package com.ttulka.ecommerce.warehouse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductCodeTest {

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
}
