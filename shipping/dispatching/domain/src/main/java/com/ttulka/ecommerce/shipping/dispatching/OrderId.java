package com.ttulka.ecommerce.shipping.dispatching;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Dispatching Saga ID domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class OrderId {

    private final @NonNull String id;

    public OrderId(@NonNull Object id) {
        var idVal = id.toString().strip();
        if (idVal.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty!");
        }
        this.id = idVal;
    }

    public String value() {
        return id;
    }
}