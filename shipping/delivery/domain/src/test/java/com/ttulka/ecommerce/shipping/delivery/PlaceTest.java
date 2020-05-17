package com.ttulka.ecommerce.shipping.delivery;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlaceTest {

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
}
