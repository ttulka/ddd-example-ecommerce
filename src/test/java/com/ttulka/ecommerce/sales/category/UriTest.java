package com.ttulka.ecommerce.sales.category;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UriTest {

    @Test
    void uri_value() {
        Uri uri = new Uri("test");
        assertThat(uri.value()).isEqualTo("test");
    }

    @Test
    void uri_value_is_trimmed() {
        Uri uri = new Uri("   01234567890123456789   ");
        assertThat(uri.value()).isEqualTo("01234567890123456789");
    }

    @Test
    void uri_fails_for_a_null_value() {
        assertThrows(IllegalArgumentException.class, () -> new Uri(null));
    }

    @Test
    void uri_fails_for_an_empty_string() {
        assertThrows(IllegalArgumentException.class, () -> new Uri(""));
    }

    @Test
    void uri_has_20_characters() {
        Uri uri = new Uri("01234567890123456789");
        assertThat(uri.value()).isEqualTo("01234567890123456789");
    }

    @Test
    void uri_fails_for_more_than_20_characters() {
        assertThrows(IllegalArgumentException.class, () -> new Uri("01234567890123456789X"));
    }
}
