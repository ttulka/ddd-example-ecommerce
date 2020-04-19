package com.ttulka.ecommerce.shipping.delivery;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTest {

    @Test
    void person_value() {
        Person person = new Person("Test Test");
        assertThat(person.value()).isEqualTo("Test Test");
    }

    @Test
    void person_value_is_trimmed() {
        Person person = new Person("   Test Test   ");
        assertThat(person.value()).isEqualTo("Test Test");
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
    void person_fails_for_invalid_values() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("test")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("test test")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("test Test")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test test")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test Test!")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test Test@")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test Test+")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test Test'")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test0 Test")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test Test0")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test Test0")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test0a Test")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Person("Test Test0a"))
        );
    }

    @Test
    void person_accepts_valid_values() {
        assertAll(
                () -> assertDoesNotThrow(() -> new Person("Alan Turing")),
                () -> assertDoesNotThrow(() -> new Person("John von Neumann")),
                () -> assertDoesNotThrow(() -> new Person("Old McDonald")),
                () -> assertDoesNotThrow(() -> new Person("Jacob O'harra")),
                () -> assertDoesNotThrow(() -> new Person("Ji Lu"))
        );
    }
}
