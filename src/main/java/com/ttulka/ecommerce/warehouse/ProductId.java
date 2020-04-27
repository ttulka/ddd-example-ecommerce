package com.ttulka.ecommerce.warehouse;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Warehouse In Stock Product ID domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class ProductId {

    private final @NonNull String id;

    public ProductId(@NonNull String id) {
        var idVal = id.strip();
        if (idVal.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty!");
        }
        this.id = idVal;
    }

    public String value() {
        return id;
    }
}
