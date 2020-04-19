package com.ttulka.ecommerce.sales.order;

import java.util.List;

/**
 * Order entity.
 */
public interface Order {

    OrderId id();

    List<OrderItem> items();

    /**
     * OrderHasNoItemsException is thrown when the Order has no items.
     */
    final class OrderHasNoItemsException extends IllegalStateException {
    }
}
