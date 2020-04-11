package com.ttulka.ecommerce.shipping.delivery;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Delivery Item entity.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public final class DeliveryItem {

    private final @NonNull ProductCode productCode;
    private final @NonNull Quantity quantity;

    public ProductCode productCode() {
        return productCode;
    }

    public Quantity quantity() {
        return quantity;
    }
}
