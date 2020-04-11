package com.ttulka.ecommerce.shipping.delivery;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Delivery Person entity.
 */
@EqualsAndHashCode
@ToString
public final class Person {

    private final @NonNull String name;

    public Person(@NonNull String name) {
        var nameVal = name.strip();
        if (nameVal.isBlank()) {
            throw new IllegalArgumentException("Person cannot be empty!");
        }
        if (nameVal.length() > 50) {
            throw new IllegalArgumentException("Person cannot be longer than 50 characters!");
        }
        this.name = nameVal;
    }

    public String value() {
        return name;
    }
}
