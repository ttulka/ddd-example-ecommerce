package com.ttulka.ecommerce.warehouse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductIdTest {

    @Test
    void product_id_value() {
        ProductId productId = new ProductId("test");
        assertThat(productId.value()).isEqualTo("test");
    }

    @Test
    void product_id_value_is_trimmed() {
        ProductId productId = new ProductId("   test   ");
        assertThat(productId.value()).isEqualTo("test");
    }

    @Test
    void product_id_fails_for_a_null_value() {
        assertThrows(IllegalArgumentException.class, () -> new ProductId(null));
    }

    @Test
    void product_id_fails_for_an_empty_string() {
        assertThrows(IllegalArgumentException.class, () -> new ProductId(""));
    }
}
