package com.ttulka.ecommerce.sales.order;

import java.util.Collection;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.sales.order.item.OrderItem;

/**
 * Place Order use-case.
 */
public interface PlaceOrder {

    /**
     * Places a new order.
     *
     * @param orderId the order ID
     * @param items   the order items
     */
    void place(OrderId orderId, Collection<OrderItem> items, Money total);
}
