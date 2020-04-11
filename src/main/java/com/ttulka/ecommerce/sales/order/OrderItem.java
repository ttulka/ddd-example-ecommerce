package com.ttulka.ecommerce.sales.order;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Order Item entity.
 */
@EqualsAndHashCode
@ToString
public final class OrderItem {

    private final @NonNull String code;
    private final @NonNull String title;
    private final @NonNull Float price;
    private final @NonNull Integer quantity;

    public OrderItem(@NonNull String code, @NonNull String title, @NonNull Float price, @NonNull Integer quantity) {
        if (code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be empty!");
        }
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty!");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be less than zero!");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity cannot be less than one!");
        }
        this.code = code;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public String code() {
        return code;
    }

    public String title() {
        return title;
    }

    public Float price() {
        return price;
    }

    public Integer quantity() {
        return quantity;
    }
}
