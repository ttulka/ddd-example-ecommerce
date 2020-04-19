package com.ttulka.ecommerce.shipping.delivery;

import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Delivery Person entity.
 */
@EqualsAndHashCode
@ToString
public final class Person {

    private static final String PATTERN = "([A-Z][a-zA-Z]+)( [a-zA-Z]+)?( [A-Z][']?[a-zA-Z]+)+";

    private final @NonNull String name;

    public Person(@NonNull String name) {
        var nameVal = name.strip();
        if (nameVal.isBlank()) {
            throw new IllegalArgumentException("Person cannot be empty!");
        }
        if (!Pattern.matches(PATTERN, nameVal)) {
            throw new IllegalArgumentException("Person value is invalid!");
        }
        this.name = nameVal;
    }

    public String value() {
        return name;
    }
}
