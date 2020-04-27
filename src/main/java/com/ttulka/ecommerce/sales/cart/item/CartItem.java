package com.ttulka.ecommerce.sales.cart.item;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Cart Item entity.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"productId", "title", "unitPrice"})
@ToString
public final class CartItem {

    private final @NonNull ProductId productId;
    private final @NonNull Title title;
    private final @NonNull Money unitPrice;
    private final @NonNull Quantity quantity;

    public ProductId productId() {
        return productId;
    }

    public Title title() {
        return title;
    }

    public Money unitPrice() {
        return unitPrice;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Money total() {
        return unitPrice.multi(quantity.value());
    }
}
