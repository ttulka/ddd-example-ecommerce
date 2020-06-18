package com.ttulka.ecommerce.warehouse;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Warehouse In Stock Amount domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Amount implements Comparable<Amount> {

    public static final Amount ZERO = new Amount(0);

    private final int amount;

    public Amount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be less than zero!");
        }
        this.amount = amount;
    }

    public int value() {
        return amount;
    }

    public Amount add(Amount addend) {
        return new Amount(amount + addend.amount);
    }

    public Amount subtract(Amount addend) {
        return new Amount(amount - addend.amount);
    }

    @Override
    public int compareTo(Amount other) {
        if (amount > other.amount) return 1;
        if (amount < other.amount) return -1;
        return 0;
    }
}
