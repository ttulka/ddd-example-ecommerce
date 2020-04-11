package com.ttulka.ecommerce.sales.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductIdTest {

    private static final String STR_64_CHARS = "0123456789012345678901234567890123456789012345678901234567890123";

    @Test
    void string_id_value() {
        ProductId productId = new ProductId(123);
        assertThat(productId.value()).isEqualTo("123");
    }

    @Test
    void fails_for_null() {
        assertThrows(IllegalArgumentException.class, () -> new ProductId(null));
    }

    @Test
    void has_max_64_characters() {
        ProductId productId = new ProductId(STR_64_CHARS);
        assertAll(
                () -> assertThat(productId.value()).isEqualTo(STR_64_CHARS),
                () -> assertThrows(IllegalArgumentException.class, () -> new ProductId(STR_64_CHARS + "X"))
        );
    }
}
