package com.ttulka.ecommerce.catalogue.cart;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Cart Item entity.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = "productCode")
@ToString
@Builder(toBuilder = true, access = AccessLevel.PACKAGE)
public final class CartItem {

    private final @NonNull String productCode;
    private final @NonNull String title;
    private final @NonNull Quantity quantity;

    public String productCode() {
        return productCode;
    }

    public String title() {
        return title;
    }

    public Quantity quantity() {
        return quantity;
    }

    public CartItem add(Quantity addend) {
        return toBuilder()
                .quantity(quantity.add(addend))
                .build();
    }
}
