package com.ttulka.ecommerce.shipping.delivery;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AddressTest {

    @Test
    void address_values() {
        Person person = new Person("test");
        Place place = new Place("test");
        Address address = new Address(person, place);

        assertAll(
                () -> assertThat(address.person()).isEqualTo(person),
                () -> assertThat(address.place()).isEqualTo(place)
        );
    }
}
