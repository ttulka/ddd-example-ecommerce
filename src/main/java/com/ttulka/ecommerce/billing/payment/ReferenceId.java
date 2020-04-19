package com.ttulka.ecommerce.billing.payment;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Reference ID domain primitive.
 * <p>
 * Refers typically to an order the payment is requested for.
 */
@EqualsAndHashCode
@ToString
public final class ReferenceId {

    private final @NonNull String id;

    public ReferenceId(@NonNull Object id) {
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