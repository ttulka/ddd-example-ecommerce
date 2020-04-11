package com.ttulka.ecommerce.sales.category;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Category ID domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class CategoryId {

    private final @NonNull String id;

    public CategoryId(@NonNull Object id) {
        var idVal = id.toString().strip();
        if (idVal.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty!");
        }
        if (idVal.length() > 64) {
            throw new IllegalArgumentException("ID cannot be longer than 64 characters!");
        }
        this.id = idVal;
    }

    public String value() {
        return id;
    }
}
