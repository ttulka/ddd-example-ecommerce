package com.ttulka.ecommerce.sales.product;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Product Description domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Description {

    private final @NonNull String description;

    public Description(String description) {
        var descriptionVal = description != null ? description.strip() : "";
        if (descriptionVal.length() > 50) {
            throw new IllegalArgumentException("Description cannot be longer than 50 characters!");
        }
        this.description = descriptionVal;
    }

    public String value() {
        return description;
    }
}
