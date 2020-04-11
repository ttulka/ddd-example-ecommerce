package com.ttulka.ecommerce.catalogue.cart;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Quantity domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Quantity {

    private final int quantity;

    public Quantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be less than zero!");
        }
        this.quantity = quantity;
    }

    public int value() {
        return quantity;
    }

    public Quantity add(Quantity addend) {
        return new Quantity(quantity + addend.value());
    }
}
