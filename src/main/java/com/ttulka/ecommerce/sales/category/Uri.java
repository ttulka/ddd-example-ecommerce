package com.ttulka.ecommerce.sales.category;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Category URI domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Uri {

    private final @NonNull String uri;

    public Uri(@NonNull String uri) {
        var uriVal = uri.strip();
        if (uriVal.isBlank()) {
            throw new IllegalArgumentException("URI cannot be empty!");
        }
        if (uriVal.length() > 20) {
            throw new IllegalArgumentException("URI cannot be longer than 20 characters!");
        }
        this.uri = uriVal;
    }

    public String value() {
        return uri;
    }
}
