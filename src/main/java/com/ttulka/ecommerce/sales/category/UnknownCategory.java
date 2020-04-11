package com.ttulka.ecommerce.sales.category;

import lombok.ToString;

/**
 * Null object implementation for Category entity.
 */
@ToString
public final class UnknownCategory implements Category {

    @Override
    public CategoryId id() {
        return new CategoryId(0);
    }

    @Override
    public Uri uri() {
        return new Uri("unknown");
    }

    @Override
    public Title title() {
        return new Title("unknown category");
    }

    @Override
    public void changeTitle(Title title) {
        // do nothing
    }
}
