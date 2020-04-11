package com.ttulka.ecommerce.shipping.delivery;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlaceTest {

    private static final String STRING_50_CHARS_LONG = "01234567890123456789012345678901234567890123456789";

    @Test
    void place_value() {
        Place place = new Place("test");
        assertThat(place.value()).isEqualTo("test");
    }

    @Test
    void place_value_is_trimmed() {
        Place place = new Place("   test   ");
        assertThat(place.value()).isEqualTo("test");
    }

    @Test
    void place_fails_for_a_null_value() {
        assertThrows(IllegalArgumentException.class, () -> new Place(null));
    }

    @Test
    void place_fails_for_an_empty_string() {
        assertThrows(IllegalArgumentException.class, () -> new Place(""));
    }

    @Test
    void place_has_100_characters() {
        Place place = new Place(STRING_50_CHARS_LONG + STRING_50_CHARS_LONG);
        assertThat(place.value()).isEqualTo(STRING_50_CHARS_LONG + STRING_50_CHARS_LONG);
    }

    @Test
    void place_fails_for_more_than_100_characters() {
        assertThrows(IllegalArgumentException.class, () -> new Place(STRING_50_CHARS_LONG + STRING_50_CHARS_LONG + "X"));
    }
}
