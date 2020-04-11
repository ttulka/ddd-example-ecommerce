package com.ttulka.ecommerce.sales.order.customer;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Customer Name domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Name {

    private final @NonNull String name;

    public Name(@NonNull String name) {
        var nameVal = name.strip();
        if (nameVal.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }
        if (nameVal.length() > 50) {
            throw new IllegalArgumentException("Name cannot be longer than 50 characters!");
        }
        this.name = nameVal;
    }

    public String value() {
        return name;
    }
}
