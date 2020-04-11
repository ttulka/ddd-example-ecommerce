package com.ttulka.ecommerce.sales.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TitleTest {

    @Test
    void title_value() {
        Title title = new Title("test");
        assertThat(title.value()).isEqualTo("test");
    }

    @Test
    void title_value_is_trimmed() {
        Title title = new Title("   test   ");
        assertThat(title.value()).isEqualTo("test");
    }

    @Test
    void title_fails_for_a_null_value() {
        assertThrows(IllegalArgumentException.class, () -> new Title(null));
    }

    @Test
    void title_fails_for_an_empty_string() {
        assertThrows(IllegalArgumentException.class, () -> new Title(""));
    }

    @Test
    void title_has_20_characters() {
        Title title = new Title("01234567890123456789");
        assertThat(title.value()).isEqualTo("01234567890123456789");
    }

    @Test
    void title_fails_for_more_than_20_characters() {
        assertThrows(IllegalArgumentException.class, () -> new Title("01234567890123456789X"));
    }
}
