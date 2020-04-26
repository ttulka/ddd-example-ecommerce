package com.ttulka.ecommerce.common.money;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Money domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Money {

    private static float MAX_VALUE = 1_000_000.f;

    private final float money;

    public Money(float money) {
        if (money < 0) {
            throw new IllegalArgumentException("Money cannot be less than zero.");
        }
        if (money > MAX_VALUE) {
            throw new IllegalArgumentException("Money cannot be greater than " + MAX_VALUE + ".");
        }
        this.money = money;
    }

    public float value() {
        return money;
    }
}
