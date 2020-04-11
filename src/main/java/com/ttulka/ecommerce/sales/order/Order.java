package com.ttulka.ecommerce.sales.order;

import java.util.List;

import com.ttulka.ecommerce.sales.order.customer.Customer;

/**
 * Order entity.
 */
public interface Order {

    OrderId id();

    List<OrderItem> items();

    Customer customer();

    /**
     * OrderHasNoItemsException is thrown when the Order has no items.
     */
    final class OrderHasNoItemsException extends IllegalStateException {
    }
}
