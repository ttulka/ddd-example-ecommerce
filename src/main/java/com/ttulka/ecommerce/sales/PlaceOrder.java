package com.ttulka.ecommerce.sales;

import java.util.List;

import com.ttulka.ecommerce.sales.order.OrderId;
import com.ttulka.ecommerce.sales.order.OrderItem;

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
    void place(OrderId orderId, List<OrderItem> items);
}
