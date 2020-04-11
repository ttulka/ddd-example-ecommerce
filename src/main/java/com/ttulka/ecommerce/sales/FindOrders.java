package com.ttulka.ecommerce.sales;

import com.ttulka.ecommerce.sales.order.Order;
import com.ttulka.ecommerce.sales.order.OrderId;

/**
 * Find Orders use-case.
 */
public interface FindOrders {

    /**
     * Finds an order by the order ID.
     *
     * @param id the order ID
     * @return the order
     */
    Order byId(OrderId id);
}
