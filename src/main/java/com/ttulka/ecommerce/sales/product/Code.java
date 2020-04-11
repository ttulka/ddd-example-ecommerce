package com.ttulka.ecommerce.sales.product;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Product Code domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Code {

    private final @NonNull String code;

    public Code(@NonNull String code) {
        var codeVal = code.strip();
        if (codeVal.isBlank()) {
            throw new IllegalArgumentException("Code cannot be empty!");
        }
        if (codeVal.length() > 50) {
            throw new IllegalArgumentException("Code cannot be longer than 50 characters!");
        }
        this.code = codeVal;
    }

    public String value() {
        return code;
    }
}
