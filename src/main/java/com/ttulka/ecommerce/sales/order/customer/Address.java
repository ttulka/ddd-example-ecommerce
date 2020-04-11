package com.ttulka.ecommerce.sales.order.customer;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Customer Address domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Address {

    private final @NonNull String address;

    public Address(@NonNull String address) {
        var addressVal = address.strip();
        if (addressVal.isBlank()) {
            throw new IllegalArgumentException("Address cannot be empty!");
        }
        if (addressVal.length() > 100) {
            throw new IllegalArgumentException("Address cannot be longer than 100 characters!");
        }
        this.address = addressVal;
    }

    public String value() {
        return address;
    }
}
