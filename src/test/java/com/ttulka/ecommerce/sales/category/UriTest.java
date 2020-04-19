package com.ttulka.ecommerce.sales.category;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UriTest {

    @Test
    void uri_value() {
        Uri uri = new Uri("test");
        assertThat(uri.value()).isEqualTo("test");
    }

    @Test
    void uri_value_is_trimmed() {
        Uri uri = new Uri("   a01234567890123456789   ");
        assertThat(uri.value()).isEqualTo("a01234567890123456789");
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
    void uri_fails_for_invalid_values() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Uri("Test")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Uri("testTest")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Uri("-test")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Uri("0test")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Uri("test-"))
        );
    }

    @Test
    void uri_accepts_valid_values() {
        assertAll(
                () -> assertDoesNotThrow(() -> new Uri("test")),
                () -> assertDoesNotThrow(() -> new Uri("test0")),
                () -> assertDoesNotThrow(() -> new Uri("test-test")),
                () -> assertDoesNotThrow(() -> new Uri("test-0-test")),
                () -> assertDoesNotThrow(() -> new Uri("test0test")),
                () -> assertDoesNotThrow(() -> new Uri("test-0test")),
                () -> assertDoesNotThrow(() -> new Uri("test0-test"))
        );
    }
}
