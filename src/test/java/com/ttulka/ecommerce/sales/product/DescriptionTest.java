package com.ttulka.ecommerce.sales.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DescriptionTest {

    private static final String STRING_50_CHARS_LONG = "01234567890123456789012345678901234567890123456789";

    @Test
    void description_value() {
        Description description = new Description("test");
        assertThat(description.value()).isEqualTo("test");
    }

    @Test
    void description_value_is_trimmed() {
        Description description = new Description("   test   ");
        assertThat(description.value()).isEqualTo("test");
    }

    @Test
    void description_has_50_characters() {
        Description description = new Description(STRING_50_CHARS_LONG);
        assertThat(description.value()).isEqualTo(STRING_50_CHARS_LONG);
    }

    @Test
    void description_fails_for_more_than_50_characters() {
        assertThrows(IllegalArgumentException.class, () -> new Description(STRING_50_CHARS_LONG + "X"));
    }
}
