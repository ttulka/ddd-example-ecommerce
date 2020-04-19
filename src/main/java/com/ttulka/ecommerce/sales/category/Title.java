package com.ttulka.ecommerce.sales.category;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Category Title domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Title {

    private final @NonNull String title;

    public Title(@NonNull String title) {
        var titleVal = title.strip();
        if (titleVal.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty!");
        }
        this.title = titleVal;
    }

    public String value() {
        return title;
    }
}
