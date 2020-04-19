package com.ttulka.ecommerce.sales.category;

import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Category URI domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Uri {

    private static final String PATTERN = "[a-z]([a-z0-9-]*[a-z0-9])?";

    private final @NonNull String uri;

    public Uri(@NonNull String uri) {
        var uriVal = uri.strip();
        if (uriVal.isBlank()) {
            throw new IllegalArgumentException("URI cannot be empty!");
        }
        if (!Pattern.matches(PATTERN, uriVal)) {
            throw new IllegalArgumentException("URI value is invalid!");
        }
        this.uri = uriVal;
    }

    public String value() {
        return uri;
    }
}
