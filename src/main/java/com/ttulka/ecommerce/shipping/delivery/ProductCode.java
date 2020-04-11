package com.ttulka.ecommerce.shipping.delivery;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Delivery Item Product Code domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class ProductCode {

    private final @NonNull String code;

    public ProductCode(@NonNull String code) {
        var codeVal = code.strip();
        if (codeVal.isBlank()) {
            throw new IllegalArgumentException("Product code cannot be empty!");
        }
        if (codeVal.length() > 50) {
            throw new IllegalArgumentException("Product code cannot be longer than 50 characters!");
        }
        this.code = codeVal;
    }

    public String value() {
        return code;
    }
}
