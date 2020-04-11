package com.ttulka.ecommerce.warehouse;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Warehouse To Fetch entity.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public final class ToFetch {

    private final @NonNull ProductCode productCode;
    private final @NonNull Amount amount;

    public ProductCode productCode() {
        return productCode;
    }

    public Amount amount() {
        return amount;
    }
}
