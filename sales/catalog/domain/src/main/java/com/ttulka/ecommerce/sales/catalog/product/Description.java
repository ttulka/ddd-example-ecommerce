package com.ttulka.ecommerce.sales.catalog.product;

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
        this.description = description != null ? description.strip() : "";
    }

    public String value() {
        return description;
    }
}
