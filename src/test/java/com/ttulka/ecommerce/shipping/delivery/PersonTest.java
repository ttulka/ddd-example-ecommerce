package com.ttulka.ecommerce.shipping.delivery;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTest {

    private static final String STRING_50_CHARS_LONG = "01234567890123456789012345678901234567890123456789";

    @Test
    void person_value() {
        Person person = new Person("test");
        assertThat(person.value()).isEqualTo("test");
    }

    @Test
    void person_value_is_trimmed() {
        Person person = new Person("   test   ");
        assertThat(person.value()).isEqualTo("test");
    }

    @Test
    void person_fails_for_a_null_value() {
        assertThrows(IllegalArgumentException.class, () -> new Person(null));
    }

    @Test
    void person_fails_for_an_empty_string() {
        assertThrows(IllegalArgumentException.class, () -> new Person(""));
    }

    @Test
    void person_has_50_characters() {
        Person person = new Person(STRING_50_CHARS_LONG);
        assertThat(person.value()).isEqualTo(STRING_50_CHARS_LONG);
    }

    @Test
    void person_fails_for_more_than_50_characters() {
        assertThrows(IllegalArgumentException.class, () -> new Person(STRING_50_CHARS_LONG + "X"));
    }
}
